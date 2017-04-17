(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('ThreadDetailController', ThreadDetailController);

    ThreadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Thread', 'Forum', 'User'];

    function ThreadDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Thread, Forum, User) {
        var vm = this;

        vm.thread = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('bsbmsoneApp:threadUpdate', function(event, result) {
            vm.thread = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
