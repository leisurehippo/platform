/**
 * Created by Gsy on 2017/7/21.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('ProjectManageController',ProjectManageController);

ProjectManageController.$inject = ['$scope', '$http', '$state', 'CreateProject', 'HdfsUpload', 'GetServerProject', '$timeout', 'DeleteProject', 'EditProject', 'GetProjectDes'];
function ProjectManageController($scope, $http, $state, CreateProject, HdfsUpload, GetServerProject, $timeout, DeleteProject, EditProject, GetProjectDes) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    vm.paramtersDes = null;
    vm.projects = [];
    vm.projectDes = null;
    vm.projectLimit = null;
    vm.projectName = null;
    vm.projectDesEdit = null;
    vm.projectLimitEdit = null;
    vm.editIndex = null;
    vm.projectDes = null;
    vm.createProject = createProject;
    vm.editProject = editProject;
    vm.delProject = delProject;

    $scope.$on('destroy', function () {
        console.log("ddd");
    });

    GetServerProject.get({}, function (res) {
        for (var k = 0; k < res.length; k++) {
            vm.projects[k] = new Array();
            vm.projects[k][0] = res[k];
        }
        GetProjectDes.get({}, function (res) {
            for (var k = 0; k < vm.projects.length; k++) {
                // console.log(res[vm.projects[k]]);
                vm.projects[k][1] = res[vm.projects[k]];
                // vm.projectDes[i] = res[vm.projects[k]];
            }

        },function (res) {

        });
        console.log(vm.projects);

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
        var projectName = vm.projects[index][0];
        EditProject.get({ProjectName:projectName, ProjectDescribe:vm.projectDesEdit, DataFormatLimit:vm.projectLimitEdit}, function (res) {
            console.log(res);
            $("#myModal1").modal("hide");
            var a = $timeout(function () {
                $state.go('projectManage', {}, { reload: true });
            },1000);
        }, function (res) {
            console.log(res);});

    }

    function delProject(index) {
        var projectName = vm.projects[index][0];
        DeleteProject.get({ProjectName:projectName}, function (res) {
            console.log(res);
        }, function (res) {
            console.log(res);
        });
        $state.go('projectManage', {}, { reload: true });
    }



}
