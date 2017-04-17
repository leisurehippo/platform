(function() {
    'use strict';

    angular
        .module('app')
        .controller('ArticleDeleteController',ArticleDeleteController);

    ArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Article'];

    function ArticleDeleteController($uibModalInstance, entity, Article) {
        var vm = this;

        vm.article = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Article.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
