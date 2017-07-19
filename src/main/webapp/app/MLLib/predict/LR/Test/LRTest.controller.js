/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('LRTestController',LRTestController);

LRTestController.$inject = ['$scope', '$http', '$state', 'GetHdfsFile', 'GetModels', 'Predict'];
function LRTestController($scope, $http, $state, GetHdfsFile, GetModels, Predict) {
    var vm = this;
    vm.alMLLib = "lr";
    vm.fileData = [];
    vm.dataName = "";
    vm.modelName = "";
    vm.models = [];
    vm.result = [];
    vm.test = test;
    GetHdfsFile.get({}, function (res) {
        console.log(res);
        vm.fileData = res;
    }, function (res) {
        console.log(res);
    });
    GetModels.get({}, function (res) {
        console.log(res);
        vm.models = res;
    }, function (res) {
        console.log(res);
    });

    function test() {
        Predict.get({DataName: vm.dataName, ModelName:vm.modelName, Algorithm:vm.alMLLib}, function (res) {
            console.log(res);
            vm.result =res;
        }, function (res) {
            console.log(res);
        });
    }

}
