/**
 * Created by gsy on 2017/3/6.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('deleteThreadService', deleteThreadService);

    deleteThreadService.$inject = ['$uibModal'];

    function deleteThreadService($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open(threadId) {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/components/deleteThreadModal/delete-thread.html',
                controller: 'DeleteThreadController',
                controllerAs: 'vm',
                resolve: {
                    threadId: function() {
                        return threadId;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        // $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
                },
                backdrop: "static",
                windowClass: "small-dialog"
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();

