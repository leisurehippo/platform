(function() {
    'use strict';

    angular
        .module('app')
        .controller('ForumDetailController', ForumDetailController);

    ForumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Forum'];

    function ForumDetailController($scope, $rootScope, $stateParams, previousState, entity, Forum) {
        var vm = this;

        vm.forum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('app:forumUpdate', function(event, result) {
            vm.forum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
