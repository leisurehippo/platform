(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('RegistrationDetailController', RegistrationDetailController);

    RegistrationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Registration', 'User'];

    function RegistrationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Registration, User) {
        var vm = this;

        vm.registration = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('bsbmsoneApp:registrationUpdate', function(event, result) {
            vm.registration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
