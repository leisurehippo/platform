/**
 * Created by arslan on 1/31/17.
 */
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardPostArticleController', DashboardPostArticleController);

    DashboardPostArticleController.$inject = ['EditArticle', 'CreateArticle', 'toaster', 'AvatarUploadService'];

    function DashboardPostArticleController(EditArticle, CreateArticle, toaster, AvatarUploadService) {
        var vm = this;
        vm.upload = Upload;
        vm.confirmEdit = confirmEdit;
        if (vm.article == undefined || vm.article == null || vm.article.id == null){
            //若修改vm.article初始化数据，请在dashboard-sidebar.controller.js中同时进行修改
            vm.article = {
                authorName: null,
                introduction: '',
                thumbnailUrl: '',
                content: '',
                contentContentType: null,
                createDate: new Date(),
                editDate: null,
                id: null,
                isAbandon: null,
                name: null,
                state: null,
                title: '',
                type: null,
                term:{id:1}
            };
        }

        vm.note = {
            isHtmlShow: false,
            isEditShow: true,
            isConfirmEditButtonShow: true
        };

        function Upload() {
            //vm.thumbnailUploadStatus = '正在上传，请稍后';
            AvatarUploadService.open("ArticleImage", {aspectRatio: 16 / 9, compress: true, width:260},
                function (result) {
                    if (result) {
                        vm.article.thumbnailUrl = 'http://omhemfx8a.bkt.clouddn.com/' + result;
                        vm.thumbnailUploadStatus = '上传成功';
                    }
                }, function () {
                    vm.thumbnailUploadStatus = '发生虾米事情了?上传未成功';
                });
        }

        // 确认编辑
        function confirmEdit() {
            openHtmlShow(vm.note);
            submit();
        }

        function openHtmlShow(object) {
            object.isHtmlShow = false;
            object.isEditShow = true;
            object.isConfirmEditButtonShow = true;
        }

        function submit() {
            if (vm.article.id == null)
                CreateArticle.save(vm.article, function success(result) {
                    toaster.pop('success', " ", '已成功');
                    vm.article = {
                        authorName: null,
                        introduction: '',
                        thumbnailUrl: '',
                        content: '',
                        contentContentType: null,
                        createDate: new Date(),
                        editDate: null,
                        id: null,
                        isAbandon: null,
                        name: null,
                        state: null,
                        title: '',
                        type: null,
                        term:{id:1}
                    };
                }, function error(result) {
                    toaster.pop('error', " ", '失败');
                });
            else
                EditArticle.save({articleid:vm.article.id},vm.article, function success(result) {
                    toaster.pop('success', " ", '已成功');
                    vm.article = null;
                }, function error(result) {
                    toaster.pop('error', " ", '失败');
                });
        }
    }
})();
