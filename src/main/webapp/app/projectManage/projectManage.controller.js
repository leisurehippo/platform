/**
 * Created by Gsy on 2017/7/21.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('ProjectManageController',ProjectManageController);

ProjectManageController.$inject = ['$scope', '$http', '$state', 'GetAlgorithmData', 'HdfsUpload', 'GetServerProject', '$stateParams', '$timeout'];
function ProjectManageController($scope, $http, $state, GetAlgorithmData, HdfsUpload, GetServerProject, $stateParams, $timeout) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    vm.projectName = $stateParams.projectName;
    vm.paramtersDes = null;
    vm.projects = [];

    GetServerProject.get({}, function (res) {
        vm.projects = res;
    }, function (res) {

    });

}
