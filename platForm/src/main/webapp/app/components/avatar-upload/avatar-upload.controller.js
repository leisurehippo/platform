(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('AvatarUploadController', AvatarUploadController);

    AvatarUploadController.$inject = ['$scope', '$uibModalInstance', '$timeout', 'Cropper', 'QiniuUploadService',
        'filePrefix', 'options', 'onSuccess', 'onFail'];

    function AvatarUploadController($scope, $uibModalInstance, $timeout, Cropper, QiniuUploadService,
                                    filePrefix, options, onSuccess, onFail) {
        var vm = this;
        // 图像上传
        vm.inputImage = new Image();
        vm.edit = false;
        vm.dataUrl = null;
        vm.AvatarChangeResult = null;
        vm.hidePreview = options.preview;
        vm.isAvatar = options.isAvatar;
        vm.defaultAvatarURL = '';
        vm.changeDefaultAvatar = changeDefaultAvatar;
        vm.selectDefaultAvatar = selectDefaultAvatar;

        vm.cancel = cancel;
        vm.giveup = giveup;
        vm.clickInputButton = clickInputButton;
        vm.onFile = onFile;
        var file, data;

        //主程序结束
        function cancel() {
            $uibModalInstance.dismiss('cancel');
        }

        //选择文件
        function clickInputButton() {
            // console.log('clicked');
            document.getElementById('openssme').click();
        }

        // 放弃修改头像
        function giveup() {
            $uibModalInstance.dismiss('cancel');
        }

        //上传头像
        vm.upload = function () {
            vm.AvatarChangeResult = '';
            var result_image = new Image();
            if (options.compress == false){
                var file = dataUrlToFile(vm.inputImage.src);
                QiniuUploadService.upload(file, function (status, blkRet) {//七牛云就把OSSUploadService改为QiniuUploadService,然后修改注入
                    if (status == true) {
                        onSuccess(file.file.name);
                    } else {
                        onFail();
                    }
                });
            } else {
                if (vm.fileType === 'png') {
                    result_image.src = jic.compress(vm.inputImage, 80, 'png').src;
                } else {
                    result_image.src = jic.compress(vm.inputImage, 80, 'jpg').src;
                }
                result_image.onload = function () {
                    var file = dataUrlToFile(result_image.src);
                    QiniuUploadService.upload(file, function (status, blkRet) {//七牛云就把OSSUploadService改为QiniuUploadService,然后修改注入
                        if (status == true) {
                            onSuccess(file.file.name);
                        } else {
                            onFail();
                        }
                    });
                };
            }
            vm.cancel();
        };

        //图像
        var dataUrlToFile = function (dataUrl) {
            var arr = String(dataUrl).split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }
            var blob = new Blob([u8arr], {type: mime});
            var type = mime.split('/')[1];

            var date = new Date();

            var key = filePrefix + "." + date.getTime().toString() + "." + type;
            return {file: new File([blob], key, {type: 'image/' + type}), blob: blob};
        };

        /**
         * Method is called every time file input's value changes.
         * Because of Angular has not ng-change for file inputs a hack is needed -
         * call `angular.element(this).scope().onFile(this.files[0])`
         * when input's event is fired.
         */
        function onFile(blob) {
            // console.log('got file');
            vm.fileType = blob.type.split('/')[1];
            Cropper.encode((file = blob)).then(function (dataUrl) {
                vm.dataUrl = dataUrl;
                $timeout(hideCropper);
                $timeout(showCropper);  // wait for $digest to set image's src
            });
        }

        /**
         * Croppers container object should be created in controller's scope
         * for updates by directive via prototypal inheritance.
         * Pass a full proxy name to the `ng-cropper-proxy` directive attribute to
         * enable proxing.
         */
        vm.cropper = {};
        vm.cropperProxy = 'vm.cropper.first';

        /**
         * When there is a cropped image to show encode it to base64 string and
         * use as a source for an image element.
         */
        vm.preview = function () {
            if (!file || !data) return;
            Cropper.crop(file, data)
                .then(function (blob) {
                    var maxWidth = options.width || 150;
                    if (options.compress == false || data.width < maxWidth)
                        return blob;
                    else
                        return Cropper.scale(blob, {width: maxWidth});
                })
                .then(Cropper.encode)
                .then(function (dataUrl) {
                    (vm.preview || (vm.preview = {})).dataUrl = dataUrl;
                    vm.inputImage.src = dataUrl;
                });
        };

        /**
         * Use cropper function proxy to call methods of the plugin.
         * See https://github.com/fengyuanchen/cropper#methods
         */
        vm.clear = function (degrees) {
            if (!vm.cropper.first) return;
            vm.cropper.first('clear');
        };

        /**
         * Object is used to pass options to initalize a cropper.
         * More on options - https://github.com/fengyuanchen/cropper#options
         */
        vm.options = {
            maximize: true,
            aspectRatio: options.aspectRatio || null,
            crop: function (dataNew) {
                data = dataNew;
            }
        };

        /**
         * Showing (initializing) and hiding (destroying) of a cropper are started by
         * events. The scope of the `ng-cropper` directive is derived from the scope of
         * the controller. When initializing the `ng-cropper` directive adds two handlers
         * listening to events passed by `ng-cropper-show` & `ng-cropper-hide` attributes.
         * To show or hide a cropper `$broadcast` a proper event.
         */
        vm.showEvent = 'show';
        vm.hideEvent = 'hide';

        function showCropper() {
            $scope.$broadcast(vm.showEvent);
        }

        function hideCropper() {
            $scope.$broadcast(vm.hideEvent);
        }

        // end 上传

        function changeDefaultAvatar(e) {
            vm.defaultAvatarURL = angular.element(e.target).attr('src')
        }

        function selectDefaultAvatar() {
            onSuccess(vm.defaultAvatarURL);
            vm.cancel();
        }
    }
})();
