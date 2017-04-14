/**
 * Created by gsy on 2017/3/6.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DeleteThreadController', DeleteThreadController);

    DeleteThreadController.$inject = ['$scope', '$uibModalInstance', '$state', 'deleteForumsThread', 'threadId'];

    function DeleteThreadController($scope, $uibModalInstance, $state, deleteForumsThread, threadId) {
        var vm = this;
        vm.deleteThreadId = threadId;
        vm.cancel = cancel;
        vm.confirm = confirm;
        vm.text = {
            create: {
                title: "您要删除帖子吗？",
                text: "请再次确认，删除后不可恢复",
                confirmButtonText: "确认删除",
                cancelButtonText: "取消"
            }
        };

        vm.page = vm.text.create;


        function cancel() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirm() {
            deleteForumsThread.update({id: vm.deleteThreadId}, {}, function (response) {
                 //console.log(vm.deleteThreadId);
                vm.cancel();
                 $state.reload($state.current.name);

            }, function (result) {
                 //console.log(result);
             });
        }

    }
})();
