/**
 * Created by arslan on 3/27/2017.
 */
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardSideBarController', DashboardSideBarController);

    DashboardSideBarController.$inject = ['GetAllVideoTag', 'DeleteVideoTag', 'SweetAlert'];

    function DashboardSideBarController(GetAllVideoTag, DeleteVideoTag, SweetAlert) {
        var vm = this;
        vm.deleteTag = deleteTag;
        vm.clearVideoData = clearVideoData;
        vm.clearArticleData = clearArticleData;

        GetAllVideoTag.get(function (result) {
            vm.tags = result;
        });

        function deleteTag(tag) {
            SweetAlert.swal({
                    title: "您要删除标签吗？",
                    text: "删除标签后不可恢复",
                    type: "warning",
                    showCancelButton: true,
                    backgroundColor: "#292e3a",
                    confirmButtonColor: "#cb6228",
                    confirmButtonText: "确认删除",
                    //cancelButtonColor: "#2a2e39",
                    cancelButtonText: "放弃"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        DeleteVideoTag.save({id:tag.id},null,function () {
                            GetAllVideoTag.get(function (result) {
                                vm.tags = result;
                            });
                        })
                    }

                }
            );
        }

        function clearVideoData() {
            //若修改vm.video初始化数据，请在dashboard-post-video.controller.js中同时进行修改
            vm.video = {
                abondon: false,
                name: '',
                thumbnailUrl: '',
                url: '',
                tag: ''
            };
        }

        function clearArticleData() {
            //若修改vm.article初始化数据，请在dashboard-post-article.controller.js中同时进行修改
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

    }
})();
