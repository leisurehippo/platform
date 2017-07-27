/**
 * Created by Gsy on 2017/7/26.
 */
(function() {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .controller('RunAlController', RunAlController);

    RunAlController.$inject = ['$state', '$scope', '$stateParams', 'GetServerProject', 'GetAlgorithmData'];

    function RunAlController ($state, $scope, $stateParams, GetServerProject, GetAlgorithmData) {
        var vm = this;
        vm.projects = [];
        vm.projectName = null;

        GetServerProject.get({}, function (res) {
            vm.projects = res;
            console.log(vm.projects);
            if ($stateParams.projectName == null && vm.projects.length > 0) {
                vm.projectName = vm.projects[0];
                console.log(vm.projectName);
            } else
                vm.projectName = $stateParams.projectName;

        }, function (res) {

        });

        var type="Algorithm";
        $scope.$watch('vm.projectName', function () {
            if (vm.projectName != null) {
                GetAlgorithmData.get({ProjectName:vm.projectName},function (res) {
                    console.log(res);
                    vm.algrithmData = res;
                }, function (result) {
                });
            }

        });


    }
})();
