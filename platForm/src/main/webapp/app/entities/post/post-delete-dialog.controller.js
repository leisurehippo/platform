(function() {
    'use strict';

    angular
        .module('app')
        .controller('PostDeleteController',PostDeleteController);

    PostDeleteController.$inject = ['$uibModalInstance', 'entity', 'Post'];

    function PostDeleteController($uibModalInstance, entity, Post) {
        var vm = this;

        vm.post = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Post.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
