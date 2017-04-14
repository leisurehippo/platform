(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Person', 'User'];

    function PersonDetailController($scope, $rootScope, $stateParams, previousState, entity, Person, User) {
        var vm = this;

        vm.person = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bsbmsoneApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
