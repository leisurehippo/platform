(function() {
    'use strict';

    angular
        .module('app')
        .controller('NoticeDeleteController',NoticeDeleteController);

    NoticeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Notice'];

    function NoticeDeleteController($uibModalInstance, entity, Notice) {
        var vm = this;

        vm.notice = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Notice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
