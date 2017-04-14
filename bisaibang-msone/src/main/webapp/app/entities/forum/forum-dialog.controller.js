(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('ForumDialogController', ForumDialogController);

    ForumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Forum'];

    function ForumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Forum) {
        var vm = this;

        vm.forum = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.forum.id !== null) {
                Forum.update(vm.forum, onSaveSuccess, onSaveError);
            } else {
                Forum.save(vm.forum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bsbmsoneApp:forumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
