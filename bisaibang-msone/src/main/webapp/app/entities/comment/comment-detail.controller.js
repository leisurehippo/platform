(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('CommentDetailController', CommentDetailController);

    CommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Comment', 'User', 'Article'];

    function CommentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Comment, User, Article) {
        var vm = this;

        vm.comment = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('bsbmsoneApp:commentUpdate', function(event, result) {
            vm.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
