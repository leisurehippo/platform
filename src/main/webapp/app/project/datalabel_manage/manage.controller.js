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
    $scope.when_change=when_change;
    $scope.dbcontrol="";
    $scope.look_db="";
    $scope.input_db="";
    $scope.dbs=[];
    $scope.labels=[];
    $scope.label_num=1;
    $scope.create_people="";
    $scope.del_dbs=[];
    $scope.db_comment=[];
    $scope.showtree=false;
    $scope.tree_url="";
    $scope.tree_id=0;
    init();
      /**
             初始化参数,获取标签
         */
        function init()
        {
            $scope.dbs=[];
            $scope.labels=[];
            $scope.del_dbs=[];
            $scope.db_comment=[];
            Initservice.get({get_dbname_type:2},function success(result)
            {
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
            if($scope.dbcontrol=='look')
                dblook();
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
            DBlookservice.get({look_db:$scope.look_db},function success(result)
            {

            },function fail(result)
            {
                alert("请求失败！");
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
               alert("标签树显示失败！");
           })

       }
}
