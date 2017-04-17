(function () {
    'use strict';

    angular
        .module('app')
        .factory('AvatarUploadService', AvatarUploadService);

    AvatarUploadService.$inject = ['$uibModal'];

    function AvatarUploadService($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;
        /**
         * @param filePrefix 上传的文件名前缀，比如用户头像的前缀就为PersonAvatar + 用户ID
         * @param onSuccess 上传成功之后执行的函数，参数为上传成功的文件名，应该以该文件名存到后端数据库里
         * @param options 宽高比，上换头像需要正方形，所以应该为1，上传图片无需固定形状所以应该为null
         * @param onFail 上传失败执行的函数,应该前端提示之类的。
         */
        function open(filePrefix,options, onSuccess, onFail) {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/components/avatar-upload/avatar-upload.html',
                controller: 'AvatarUploadController',
                controllerAs: 'vm',
                resolve: {
                    filePrefix: function () {
                        return filePrefix;
                    },
                    options: function () {
                        return options;
                    },
                    onSuccess: function () {
                        return onSuccess;
                    },
                    onFail: function () {
                        return onFail;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }]
                },
                backdrop: "static",
                windowClass: "middle-dialog"
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
