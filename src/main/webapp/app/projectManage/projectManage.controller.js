/**
 * Created by Gsy on 2017/7/21.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('ProjectManageController',ProjectManageController);

ProjectManageController.$inject = ['$scope', '$http', '$state', 'CreateProject', 'HdfsUpload', 'GetServerProject', '$timeout', 'DeleteProject', EditProject];
function ProjectManageController($scope, $http, $state, CreateProject, HdfsUpload, GetServerProject, $timeout,DeleteProject) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    vm.paramtersDes = null;
    vm.projects = [];
    vm.projectDes = null;
    vm.projectLimit = null;
    vm.projectName = null;
    vm.createProject = createProject;
    vm.editProject = editProject;
    vm.delProject = delProject;

    GetServerProject.get({}, function (res) {
        vm.projects = res;
    }, function (res) {

    });
    function createProject() {
        CreateProject.get({ProjectName:vm.projectName, ProjectDescribe:vm.projectDes, DataFormatLimit:vm.projectLimit}, function (res) {
            console.log(res);
        }, function (res) {
            console.log(res);
        });
        $("#myModal").modal("hide");
        var a = $timeout(function () {
            $state.go('projectManage', {}, { reload: true });
        },1000);
    }

    function editProject( index ) {

    }

    function delProject(index) {
        var projectName = vm.projects[index];
        DeleteProject.get({ProjectName:projectName}, function (res) {
            console.log(res);
        }, function (res) {
            console.log(res);
        });
        $state.go('projectManage', {}, { reload: true });
    }



}
