(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('ForumDetailController', ForumDetailController);

    ForumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Forum'];

    function ForumDetailController($scope, $rootScope, $stateParams, previousState, entity, Forum) {
        var vm = this;

        vm.forum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bsbmsoneApp:forumUpdate', function(event, result) {
            vm.forum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
