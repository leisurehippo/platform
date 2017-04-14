/**
 * Created by OlyLis on 16-7-18.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('RegisterService', RegisterService);

    RegisterService.$inject = ['$uibModal'];

    function RegisterService($uibModal){
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open(operation, onSuccess, onFail) {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/account/register/register.html',
                controller: 'RegisterController',
                controllerAs: 'vm',
                resolve: {
                    operation: function () {
                        return operation;
                    },
                    onSuccess: function () {
                        return onSuccess;
                    },
                    onFail: function () {
                        return onFail;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        $translatePartialLoader.addPart('login');
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
