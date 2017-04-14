(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('TermDeleteController',TermDeleteController);

    TermDeleteController.$inject = ['$uibModalInstance', 'entity', 'Term'];

    function TermDeleteController($uibModalInstance, entity, Term) {
        var vm = this;

        vm.term = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Term.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
