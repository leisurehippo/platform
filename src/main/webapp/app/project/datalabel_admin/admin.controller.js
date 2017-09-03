/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelAdminController',DataLabelAdminController);

DataLabelAdminController.$inject = ['$scope','$http', '$state', '$injector','Labelchangeservice','Labelmergeservice','Labeldelservice','Initservice'];
function DataLabelAdminController($scope,$http, $state,$injector, Labelchangeservice,Labeldelservice,Labelmergeservice,Initservice) {
    $scope.submit = submit;
    $scope.range =range;
    $scope.labelcontrol="null";
    $scope.showtree=false;
    $scope.isdelall=false;
    $scope.ischangeall=false;
    $scope.check_merge_num=true;

    $scope.merge_label="";
    $scope.change_label="";
    $scope.change_tlabel="";
    $scope.del_label="";
    $scope.labels=[];
    $scope.rootlabels=[];
    $scope.merge_num=2;
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
        Initservice.get({get_dbname:false},function success(result)
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
          var merged_labels=new Array($scope.merge_num);
          for(var i=0;i<merged_labels.length;i++)
          {
            var id="#"+i;
            merged_labels[i]=$(id).find("option:selected").text();
          }

          Labelmergeservice.get({merge_label:$scope.merge_label,merged_labels:merged_labels},function success(){

          },function fail(result){

          }
          )
    }

    function label_change()
    {
         Labelchangeservice.get({change_label:$scope.change_label,change_tlabel:$scope.change_tlabel,ischangeall:$scope.ischangeall},function success(){

          },function fail(result){

          }
          )
    }

    function label_del()
    {
     Labelmergeservice.get({del_label:$scope.del_label,isdelall:$scope.isdelall},function success(){

      },function fail(result){

      }
      )
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
