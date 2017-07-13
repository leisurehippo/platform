/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('LRTrainController',LRTrainController);

LRTrainController.$inject = ['$scope', '$http', '$state', 'GetHdfsFile', 'GetParameter', 'TrainModel'];
function LRTrainController($scope, $http, $state, GetHdfsFile, GetParameter, TrainModel) {
    var vm = this;
    vm.fileData = [];
    vm.param = [];
    vm.newParam = [];
    vm.alMLLib = "lr";
    vm.dataName = "";
    vm.modelName = "";
    vm.train = train;
    GetHdfsFile.get({}, function (res) {
        console.log(res);
        vm.fileData = res;
    }, function (res) {
        console.log(res);
    });
    GetParameter.get({Algorithm: vm.alMLLib},  function (res) {
        for (var i = 0; i< res.length; i++) {
            vm.param[i] = res[i].split(":");
        }

        angular.copy(vm.param, vm.newParam);
        console.log( vm.newParam);
    }, function (res) {
        console.log(res);
    });

    $scope.$watch('vm.alMLLib', function (newValue, oldValue) {
        console.log( vm.alMLLib);
        GetParameter.get({Algorithm: vm.alMLLib},  function (res) {
            vm.param = [];
            for (var i = 0; i< res.length; i++) {
                vm.param[i] = res[i].split(":");
            }
            angular.copy(vm.param, vm.newParam);
        }, function (res) {
            console.log(res);
        });
    });
    
    function train() {
        console.log( vm.dataName);
        var params = "{";
        var count = 0;
        for( var i = 0; i< vm.param.length; i++) {
            if (vm.param[i][1] != vm.newParam[i][1]) {
                count ++;
                if (count == 1)
                    params += vm.newParam[i][0] + ":" + vm.newParam[i][1];
                else
                    params += "," + vm.newParam[i][0] + ":" + vm.newParam[i][1];

            }
        }
        params += "}";
        console.log(params);
        TrainModel.get({DataName:vm.dataName, ModelName:vm.modelName,Parameters:params, Algorithm:vm.alMLLib }, function (res) {
            console.log(res);

        },function (res) {
            console.log(res);
        });

    }
    
    





}
