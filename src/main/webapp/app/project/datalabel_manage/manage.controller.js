/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelManageController',DataLabelManageController);

DataLabelManageController.$inject = ['$scope', '$http', '$state', '$injector','DBlookservice','DBcreateservice','DBdelservice','Labeldrawservice','Initservice'];
function DataLabelManageController($scope, $http, $state,$injector, DBlookservice,DBcreateservice,DBdelservice,Labeldrawservice,Initservice) {
    $scope.submit=submit;
    $scope.range=range;
    $scope.draw=draw;
    $scope.jump=jump;
    $scope.when_change=when_change;
    $scope.radio_change=radio_change;
    $scope.dbcontrol="";
    $scope.look_db="";
    $scope.input_db="";
    $scope.del_db="";
    $scope.check_label=true;
    $scope.check_people=true;
    $scope.check_exist=false;
    $scope.check_num=true;
    $scope.check_name=true;
    $scope.dbs=[];
    $scope.labels=[];
    $scope.label_num=1;
    $scope.create_people="";
    $scope.del_dbs=[];
    $scope.all_dbnames=[];
    $scope.db_comment=[];
    $scope.showtree=false;
    $scope.tree_url="";
    $scope.tree_id=0;
    $scope.jump_page="";
    $scope.dataset=[];
    $scope.$on("$destroy",rm_dialog);
    $('#myModal').modal({keyboard:true,backdrop:'static',show:false});
    var perpage=15;
    var element = $('#page');
    var totalPages=1;
    var currentPage=1;
    var options=null;
    var show_page_num=10;
    init();
    function show_content(result)
   {
        $scope.dataset=result.dataset;
        totalPages=result.page;
        if(totalPages<1)//不能为0
           totalPages=1;
         if(currentPage>totalPages)
            currentPage=totalPages;
         console.log(totalPages+" "+currentPage);
          options = {
             bootstrapMajorVersion:3,
             currentPage: currentPage,
             numberOfPages: show_page_num,
             totalPages:totalPages,
             /**
                  翻页点击回调函数
             */
             onPageClicked: function(e,originalEvent,type,page)
             {
                    currentPage=page;
                    dblook();
             }
          }
          element.bootstrapPaginator(options);
   }

    /**
         初始化参数,获取标签
     */
    function init()
    {
        $scope.look_db="";
        $scope.input_db="";
        $scope.del_db="";
        $scope.dbs=[];
        $scope.labels=[];
        $scope.label_num=1;
        $scope.create_people="";
        $scope.del_dbs=[];
        $scope.all_dbnames=[];
        $scope.db_comment=[];
        Initservice.get({init_type:2},function success(result)
        {
            $scope.all_dbnames=result.all_dbnames;
            for(i in result.all_dbnames)
            {
                var name=result.all_dbnames[i];

                var comment_list=result.comment_content[i].split("#");
                var comment=comment_list[0];
                if(comment!="")
                    name=name+"("+comment+")";
                if(comment_list.length==3)
                {
                    $scope.db_comment[name]="该表由 "+comment_list[1]+" 创建于 "+comment_list[2];
                }
                $scope.dbs.push(name);
                if(name.indexOf('label_data_set')<0)//现在固定了表名，后续需要修改
                {
                    $scope.del_dbs.push(name);
                }

           }
           for(i in result.all_label)
           {
                $scope.labels.push(i);
           }

        },function failure(result){
            alert("参数初始化失败！");
        })
    }

    function when_change(num)
    {
        var db=$("#db"+num).find("option:selected").text();
        console.log($scope.db_comment);
        console.log(db);
        var comment="";
        if(!!$scope.db_comment[db]){
            comment=$scope.db_comment[db];
        }
        console.log($("#text"+num));
        $("#text"+num).text(comment);
    }

    /**
         range函数，前端的ng-repeat用于循环固定次数n
     */
    function range(n) {
        console.log($("#target0").find("option:selected").text());
        if(isNaN(n)){//非数字
            n=0;
        }else{
            n=parseInt(n);
        }
        return new Array(n);
    };

    function submit()
    {
        if(!check_param())
            return;
        if($scope.dbcontrol=='look')
        {
            currentPage=1;
            dblook();
        }
        if($scope.dbcontrol=='create')
        {
            dbcreate();

        }
         if($scope.dbcontrol=='del')
            dbdel();
    }


    function dbcreate()
    {
         var target_labels=new Array(parseInt($scope.label_num));
          for(var i=0;i<target_labels.length;i++)
          {
            var id="#target"+i;
            target_labels[i]=$(id).find("option:selected").text();
          }
        DBcreateservice.get({create_name:'label_'+$scope.input_db,create_people:$scope.create_people,target_labels:target_labels},function success(result)
        {
             init();
             if(result.status)
                    alert("操作成功！");
                 else
                    alert("操作失败！");
        },function fail(result)
        {
            alert("请求失败！");
        })
    }

    function dblook()
    {
        DBlookservice.get({look_db:$scope.look_db,cur_page:currentPage,per_page:perpage},function success(result)
        {
            init();
            if(result.response_code==-1){
                alert("服务器出现未知错误！");
                result=new Array();
                result['dataset']=[];
                result['page']=0;
                show_content(result);
            }
             else{
                show_content(result);
             }
            $('#myModal').modal('show');

        },function fail(result)
        {
            alert("请求失败！");
            result=new Array();
            result['dataset']=[];
            result['page']=0;
            show_content(result);
        })
    }

    function dbdel()
   {
       DBdelservice.get({del_db:$scope.del_db},function success(result)
       {
             init();
             if(result.status)
                    alert("操作成功！");
                 else
                    alert("操作失败！");
       },function fail(result)
       {
           alert("请求失败！");
       })
   }

   function draw()
   {
       Labeldrawservice.get({},function success(result)
       {
           console.log(result.status);
           if(result.status){
               $scope.showtree=true;
               $scope.tree_id=($scope.tree_id+1) % 10000;
               $scope.tree_url="tree.png?id="+$scope.tree_id;
               }
           else{
               alert("查看标签图失败！");
           }
       },function fail()
       {
           alert("请求失败！");
       })

   }



   function jump(){
       if($scope.jump_page!="")
                if(!isNaN($scope.jump_page))
                {
                    var p = parseInt($scope.jump_page);
                    if(p>totalPages)
                    {
                        p=totalPages;
                    }
                    if(p<1)
                    {
                        p=1;
                    }
                    $('#page').bootstrapPaginator("show",p);//只改变页码，但是没有改变页面内容，没有出发回调函数
                    options.onPageClicked("","","",p);
                 }
   }

   function rm_dialog()
    {
        $('#myModal').remove();
    }

    function check_param()
    {
        if($scope.dbcontrol=='look')
        {
            $scope.check_label=true;
            if($scope.look_db=="")
                $scope.check_label=false;
             if($scope.check_label)
                return true;
        }
        if($scope.dbcontrol=='create')
        {
            $scope.check_label=true;
            $scope.check_people=true;
            $scope.check_exist=false;
            $scope.check_num=true;
            $scope.check_name=true;
            if($scope.input_db=="")
                $scope.check_label=false;
             else if($scope.input_db.indexOf("data_set")>=0)
                $scope.check_name=false;
             else if($scope.all_dbnames.indexOf('label_'+$scope.input_db)>=0)
                $scope.check_exist=true;
             if($scope.create_people=="")
                $scope.check_people=false;
             if(isNaN($scope.label_num) || parseInt($scope.label_num)<1)
                $scope.check_num=false;
            if($scope.check_label&&$scope.check_people&&!$scope.check_exist&&$scope.check_num&&$scope.check_name)
                return true;

        }
         if($scope.dbcontrol=='del')
          {
                $scope.check_label=true;
                if($scope.del_db=="")
                    $scope.check_label=false;

                if($scope.check_label)
                    return true;
          }
        return false;
    }

    function radio_change()
    {
        $scope.check_label=true;
        $scope.check_people=true;
        $scope.check_exist=false;
        $scope.check_num=true;
    }
}
