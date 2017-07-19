/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('AllFileDataController',AllFileDataController);

AllFileDataController.$inject = ['$scope', '$http', '$state', 'GetAllData', 'GetAlgorithmData', 'HdfsUpload'];
function AllFileDataController($scope, $http, $state, GetAllData, GetAlgorithmData, HdfsUpload) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    GetAllData.get({}, function (result) {
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
