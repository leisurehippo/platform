/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelAdminController',DataLabelAdminController);

DataLabelAdminController.$inject = ['$scope','$http', '$state', '$injector','Labelchangeservice','Labelmergeservice','Labeldelservice','Labelrenameservice','Labeldrawservice','Labeljoinservice','Labelcreateservice','Initservice'];
function DataLabelAdminController($scope,$http, $state,$injector, Labelchangeservice,Labelmergeservice,Labeldelservice,Labelrenameservice,Labeldrawservice,Labeljoinservice,Labelcreateservice,Initservice) {
    $scope.submit = submit;
    $scope.draw=draw;
    $scope.range =range;
    $scope.labelcontrol="null";
    $scope.showtree=false;
    $scope.isdelall=false;
    $scope.ischangeall=false;
    $scope.check_merge_num=true;
    $scope.select_parent=false;
    $scope.new_label="";
    $scope.new_parent_label="";
    $scope.merge_label="";
    $scope.join_target="";
    $scope.change_label="";
    $scope.change_tlabel="";
    $scope.old_name="";
    $scope.new_name="";
    $scope.del_label="";
    $scope.tree_url="tree.png";
    $scope.tree_id=0;
    $scope.labels=[];
    $scope.rootlabels=[];
    $scope.merge_num=1;
    $scope.join_num=1;
    init();
    function submit()
    {
        if($scope.labelcontrol=="merge")
        {
            label_merge();
        }
        else if($scope.labelcontrol=="change")
        {
            label_change();
        }
        else if($scope.labelcontrol=="del")
        {
            label_del();
        }
        else if($scope.labelcontrol=="rename")
        {
            label_rename();
        }
        else if($scope.labelcontrol=="join")
        {
            label_join();
        }
        else if($scope.labelcontrol=="create")
        {
            label_create();
        }

    }

    /**
         range函数，前端的ng-repeat用于循环固定次数n
     */
    function range(n) {
        console.log($("#0").find("option:selected").text());
        if(isNaN(n)){//非数字
            n=0;
        }else{
            n=parseInt(n);
        }
        return new Array(n);
    };

    /**
         初始化参数,获取root标签
     */
    function init()
    {
        $scope.labels=[];
        $scope.rootlabels=[];
        Initservice.get({get_dbname_type:0},function success(result)
        {
            for(label in result.all_label)
            {
                $scope.labels.push(label);
                if(result.all_label[label]){
                    $scope.rootlabels.push(label);
                }

           }
        },function failure(result){
            alert('参数初始化失败！');
        })
    }

    function label_merge()
    {
          var merged_labels=new Array(parseInt($scope.merge_num));
          for(var i=0;i<merged_labels.length;i++)
          {
            var id="#"+i;
            merged_labels[i]=$(id).find("option:selected").text();
          }
          console.log("-->"+merged_labels.length);
          Labelmergeservice.get({merge_label:$scope.merge_label,merged_labels:merged_labels},function success(result){
                if(result.status)
                    alert("操作成功！");
                 else
                    alert("操作失败！");
          },function fail(result){
                alert("请求失败！");
          }
          )
    }

    function label_change()
    {
         Labelchangeservice.get({change_label:$scope.change_label,change_tlabel:$scope.change_tlabel,ischangeall:$scope.ischangeall},function success(result){
            if(result.status)
                alert("操作成功！");
             else
                alert("操作失败！");
          },function fail(result){
               alert("请求失败！");
          }
          )
    }

    function label_del()
    {
     Labeldelservice.get({del_label:$scope.del_label,isdelall:$scope.isdelall},function success(result){
        if(result.status)
            alert("操作成功！");
         else
            alert("操作失败！");
      },function fail(result){
           alert("请求失败！");
      }
      )
    }

    function label_rename()
    {
        Labelrenameservice.get({old_name:$scope.old_name,new_name:$scope.new_name},function success(result)
        {
        if(result.status)
            alert("操作成功！");
         else
            alert("操作失败！");
        },function fail(result)
        {
            alert("请求失败！");
        })
    }

    function label_join(){
         var joined_labels=new Array(parseInt($scope.join_num));
          for(var i=0;i<joined_labels.length;i++)
          {
            var id="#join"+i;
            joined_labels[i]=$(id).find("option:selected").text();
          }
          console.log("-->"+joined_labels.length);
          Labeljoinservice.get({join_target:$scope.join_target,joined_labels:joined_labels},function success(result){
                if(result.status)
                    alert("操作成功！");
                 else
                    alert("操作失败！");
          },function fail(result){
                alert("请求失败！");
          }
          )
    }

    function label_create(){
          Labelcreateservice.get({new_label:$scope.new_label,new_parent_label:$scope.new_parent_label,select_parent:$scope.select_parent},function success(result){
                if(result.status)
                    alert("操作成功！");
                 else
                    alert("操作失败！");
          },function fail(result){
                alert("请求失败！");
          }
          )
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
        init();
    }

    function checkparam()
    {
       if(isNaN($scope.merge_num))
       {
            $scope.check_merge_num=false;

       }
       else if(parseInt($scope.merge_num)<2)
       {
            $scope.check_merge_num=false;
       }
       return true
    }

}
