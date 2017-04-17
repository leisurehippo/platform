(function() {
    'use strict';

    angular
        .module('app')
        .controller('ThreadDialogController', ThreadDialogController);

    ThreadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Thread', 'Forum', 'User'];

    function ThreadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Thread, Forum, User) {
        var vm = this;

        vm.thread = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.forums = Forum.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.thread.id !== null) {
                Thread.update(vm.thread, onSaveSuccess, onSaveError);
            } else {
                Thread.save(vm.thread, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('app:threadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.editDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
