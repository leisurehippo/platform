(function() {
    'use strict';

    angular
        .module('app')
        .controller('ThreadDeleteController',ThreadDeleteController);

    ThreadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Thread'];

    function ThreadDeleteController($uibModalInstance, entity, Thread) {
        var vm = this;

        vm.thread = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Thread.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
