/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('AllFileDataController',AllFileDataController);

AllFileDataController.$inject = ['$scope', '$http', '$state', 'GetAllData', 'GetAlgorithmData'];
function AllFileDataController($scope, $http, $state, GetAllData, GetAlgorithmData) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    GetAllData.post({}, function (result) {
        for (var i = 0; i< result.length; i++) {
            vm.fileData[i] = result[i].split("+");
            if (vm.fileData[i][1] == '0') {
                vm.fileData[i][1] = false;
            }else
                vm.fileData[i][1] = true;

        }
        console.log(vm.fileData);
    }, function (result) {
    });

    var type="Algorithm";
    GetAlgorithmData.get({Type:type},function (res) {
        console.log(res);
        vm.algrithmData = res;
    }, function (result) {
    });

}
