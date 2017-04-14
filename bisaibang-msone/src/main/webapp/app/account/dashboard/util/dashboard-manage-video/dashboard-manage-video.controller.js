/**
 * Created by arslan on 1/31/17.
 */
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardManageVideoController', DashboardManageVideoController);

    DashboardManageVideoController.$inject = ['DeleteVideoById', '$scope', 'VideoGetAll', 'VideoGetAllByTag', 'toaster', 'AlertService',
        'PaginationUtil', 'paginationConstants', 'SweetAlert'];

    function DashboardManageVideoController(DeleteVideoById, $scope, VideoGetAll, VideoGetAllByTag, toaster, AlertService,
                                            PaginationUtil, paginationConstants, SweetAlert) {
        var vm = this;
        vm.page = 1;
        vm.videos = [];
        var params = {
            page: vm.page,
            sort: 'id,asc',
            search: null
        };
        var pagingParams = {
            page: PaginationUtil.parsePage(params.page),
            sort: params.sort,
            predicate: PaginationUtil.parsePredicate(params.sort),
            ascending: PaginationUtil.parseAscending(params.sort),
            search: params.search
        };
        vm.confirmDelete = confirmDelete;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.gotoEdit = gotoEdit;

        $scope.$watch('vm.refresh', function () {
            if (vm.refresh == 'manageVideo')
                loadAll();
        });
        $scope.$watch('vm.selectedVideoTag', function () {
            if (vm.refresh == 'manageVideo')
                loadAll();
        });

        function confirmDelete(id) {
            SweetAlert.swal({
                    title: "您要删除视频吗？",
                    text: "删除视频后不可恢复",
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
                        DeleteVideoById.save({id: id}, {}, function (response) {
                            toaster.pop('success', '', '删除成功');
                            //console.log(response);

                            loadAll();
                        }, function (response) {
                            //console.log(response);
                        });
                    }

                }
            );

        }

        function loadAll() {
            if (vm.selectedVideoTag == null)
                VideoGetAll.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            else
                VideoGetAllByTag.query({
                    id:vm.selectedVideoTag.id,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                //vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.videos = data.content;
                //console.log(vm.videos);
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            params.page = vm.page;
            params.sort = vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc');
            params.search = vm.currentSearch;
            loadAll();
        }

        function gotoEdit(video) {
            vm.video = video;
            vm.refresh = 'postVideo';
        }
    }
})();
