(function() {
    'use strict';

    angular
        .module('app')
        .controller('TermDetailController', TermDetailController);

    TermDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Term'];

    function TermDetailController($scope, $rootScope, $stateParams, previousState, entity, Term) {
        var vm = this;

        vm.term = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('app:termUpdate', function(event, result) {
            vm.term = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
