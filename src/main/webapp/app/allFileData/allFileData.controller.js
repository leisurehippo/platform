/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('AllFileDataController',AllFileDataController);

AllFileDataController.$inject = ['$scope', '$http', '$state', 'GetHdfsData', 'GetAlgorithmData', 'HdfsUpload', 'GetServerData'];
function AllFileDataController($scope, $http, $state, GetHdfsData, GetAlgorithmData, HdfsUpload, GetServerData) {
    var vm = this;
    vm.hdfsData = [];
    vm.serverData = [];
    vm.algrithmData = [];
    GetServerData.get({}, function (result) {
        for (var i = 0; i< result.length; i++) {
            vm.serverData[i] = result[i].split("+");
            if (vm.serverData[i][1] == '0') {
                vm.serverData[i][1] = false;
            }else
                vm.serverData[i][1] = true;

        }
        console.log(vm.serverData);
    }, function (result) {
    });

    GetHdfsData.get({}, function (res) {
        vm.hdfsData = res;
    });

    var type="Algorithm";
    GetAlgorithmData.get({Type:type},function (res) {
        console.log(res);
        vm.algrithmData = res;
    }, function (result) {
    });

    vm.alFileUpload = alFileUpload;
    function alFileUpload() {
        $state.go('fileUpload', {fileType:1});
    }

    vm.dataFileUpload = dataFileUpload;
    function dataFileUpload() {
        $state.go('fileUpload',  {fileType:0});
    }

    vm.fileUpHdfs = fileUpHdfs;
    function fileUpHdfs(index) {
        console.log(index);
        HdfsUpload.get({DataName:vm.fileData[index][0]}, function (res) {
            console.log(res);
            $state.go('allFileData', null, { reload: true });
        }, function (res) {
            console.log(res);
        });
    }

}
