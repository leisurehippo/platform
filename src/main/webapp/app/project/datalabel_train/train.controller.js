/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelTrainController',DataLabelTrainController);

DataLabelTrainController.$inject = ['$scope', '$http', '$state', '$injector','Labeltrainservice','Initservice'];
function DataLabelTrainController($scope, $http, $state,$injector, Labeltrainservice,Initservice) {
    $scope.submit=submit;
    $scope.train_label="";
    $scope.train_all=false;
    $scope.labels=[];
    $scope.uptimes=[];
    $scope.check_train_label=true;
    $scope.check_train_all=true;
    $('#waitModal').modal({keyboard:false,backdrop:'static',show:false});
    $scope.$on("$destroy",rm_dialog);
    init();


      /**
             初始化参数,获取标签
         */
        function init()
        {
           $scope.train_label="";
           $scope.train_all=false;
           $scope.labels=[];
           $scope.uptimes=[];
            Initservice.get({init_type:-1},function success(result)
            {
                for(i in result.all_label)
                {
                    $scope.labels.push(i);
                    if(result.all_label[i]=="no")
                        $scope.uptimes[i]="该标签还未训练模型";
                    else
                        $scope.uptimes[i]="上次更新时间:"+result.all_label[i];
               }
               $scope.labels.push("all");
            },function failure(result){})
        }


        function submit()
        {
            if(!check_param())
                return;
            $('#waitModal').modal('show');
            Labeltrainservice.get({train_label:$scope.train_label},function success(result)
            {
                init();
                $('#waitModal').modal('hide');
                if(result.status)
                    alert("训练成功！");
                 else
                    alert("训练失败！");
            },function fail(result)
            {
                $('#waitModal').modal('hide');
                alert("请求失败！");
            })
        }

        function check_param()
        {
            if($scope.train_label=="")
                $scope.check_train_label=false;
             else
                $scope.check_train_label=true;
//             if($scope.train_all=="")
//                $scope.check_train_all=false;
//             else
//                $scope.check_train_all=true;
             if($scope.check_train_label)
                return true;
              return false;
        }

        function rm_dialog()
         {
                $('#waitModal').remove();
         }
}
