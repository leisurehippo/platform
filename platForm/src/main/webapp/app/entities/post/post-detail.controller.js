(function() {
    'use strict';

    angular
        .module('app')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Post', 'User', 'Thread'];

    function PostDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Post, User, Thread) {
        var vm = this;

        vm.post = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('app:postUpdate', function(event, result) {
            vm.post = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
