(function() {
    'use strict';

    angular
        .module('app')
        .controller('RegistrationDeleteController',RegistrationDeleteController);

    RegistrationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Registration'];

    function RegistrationDeleteController($uibModalInstance, entity, Registration) {
        var vm = this;

        vm.registration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Registration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
