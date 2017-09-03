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

    init();


      /**
             初始化参数,获取标签
         */
        function init()
        {
            $scope.labels=[];
            Initservice.get({get_dbname:false},function success(result)
            {
                for(i in result.all_label)
                {
                    label=result.all_label[i];
                    $scope.labels.push(label);
               }
            },function failure(result){})
        }


        function submit()
            {
                console.log($scope.train_all);

            }
}
