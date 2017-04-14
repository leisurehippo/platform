/**
 * Created by arslan on 1/31/17.
 */
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardPostVideoController', DashboardPostVideoController);

    DashboardPostVideoController.$inject = ['GetAllVideoTag', 'YoukuUtils', 'AvatarUploadService', 'CreateVideo', 'EditVideo', 'toaster'];

    function DashboardPostVideoController(GetAllVideoTag, YoukuUtils, AvatarUploadService, CreateVideo, EditVideo, toaster) {

        var vm = this;
        vm.Upload = Upload;
        vm.submit = submit;
        vm.thumbnailUploadStatus = '';
        if(!vm.video) {
            //若修改vm.video初始化数据，请在dashboard-sidebar.controller.js中同时进行修改
            vm.video = {
                abondon: false,
                introduction: '',
                name: '',
                thumbnailUrl: '',
                url: '',
                tag: ''
            };
        }
        GetAllVideoTag.get(function (result) {
            vm.tags = result;
            if (vm.video.tag != '') {
                vm.video.tag.split('+').forEach(function (item) {
                    $.grep(vm.tags, function(e){ return e.id == parseInt(item); })[0].checked = true;
                });
            }
        });

        function Upload() {
            //vm.thumbnailUploadStatus = '正在上传，请稍后';
            AvatarUploadService.open("ArticleImage", {aspectRatio:16/9,compress:true, width:350},
                function (result) {
                    if(result){
                        vm.video.thumbnailUrl = 'http://omhemfx8a.bkt.clouddn.com/' + result;
                        vm.thumbnailUploadStatus = '上传成功';
                    }
                }, function () {
                    vm.thumbnailUploadStatus = '发生虾米事情了?上传未成功';
                });
        }

        function submit() {
            if (YoukuUtils.getVideoIdFromURL(vm.video.url).length < 3) {
                toaster.pop('error', " ", 'URL无效');
            } else {
                vm.video.tag = '';
                for (var i = 0; i < vm.tags.length; i++) {
                    if (vm.tags[i].checked == true) {
                        if (vm.video.tag.length > 0)
                            vm.video.tag += '+';
                        vm.video.tag += vm.tags[i].id;
                    }
                }
                if (vm.video.id){
                    EditVideo.save({id:vm.video.id},vm.video, function success(result) {
                        toaster.pop('success', " ", '已成功');
                        vm.video = null;
                    }, function error(result) {
                        toaster.pop('error', " ", '失败');
                    });
                }else {
                    CreateVideo.save({},vm.video, function success(result) {
                        toaster.pop('success', " ", '已成功');
                        vm.video = null;
                    }, function error(result) {
                        toaster.pop('error', " ", '失败');
                    });
                }
            }
        }
    }
})();
