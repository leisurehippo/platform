(function() {
    'use strict';

    angular
        .module('app')
        .controller('ForumDeleteController',ForumDeleteController);

    ForumDeleteController.$inject = ['$uibModalInstance', 'entity', 'Forum'];

    function ForumDeleteController($uibModalInstance, entity, Forum) {
        var vm = this;

        vm.forum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Forum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
