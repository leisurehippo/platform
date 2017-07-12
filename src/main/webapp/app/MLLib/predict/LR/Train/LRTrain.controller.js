/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('LRTrainController',LRTrainController);

LRTrainController.$inject = ['$scope', '$http', '$state', 'GetHdfsFile'];
function LRTrainController($scope, $http, $state, GetHdfsFile) {
    var vm = this;
    vm.fileData = [];
    GetHdfsFile.get({}, function (res) {
        console.log(res);
        vm.fileData = res;
    }, function (res) {
        console.log(res);
    });


}
