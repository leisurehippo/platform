(function() {
    'use strict';

    angular
        .module('app')
        .controller('NoticeDetailController', NoticeDetailController);

    NoticeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Notice', 'User'];

    function NoticeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Notice, User) {
        var vm = this;

        vm.notice = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('app:noticeUpdate', function(event, result) {
            vm.notice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
