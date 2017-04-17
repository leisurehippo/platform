(function() {
    'use strict';

    angular
        .module('app')
        .controller('NoticeDialogController', NoticeDialogController);

    NoticeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Notice', 'User'];

    function NoticeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Notice, User) {
        var vm = this;

        vm.notice = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notice.id !== null) {
                Notice.update(vm.notice, onSaveSuccess, onSaveError);
            } else {
                Notice.save(vm.notice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('app:noticeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
