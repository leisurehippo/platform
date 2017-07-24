/**
 * Created by Gsy on 2017/7/21.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('ProjectManageController',ProjectManageController);

ProjectManageController.$inject = ['$scope', '$http', '$state', 'CreateProject', 'HdfsUpload', 'GetServerProject', '$timeout'];
function ProjectManageController($scope, $http, $state, CreateProject, HdfsUpload, GetServerProject, $timeout) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    vm.paramtersDes = null;
    vm.projects = [];
    vm.createProject = createProject;

    function createProject() {
        // CreateProject.get()
    }

    GetServerProject.get({}, function (res) {
        vm.projects = res;
    }, function (res) {

    });

}
