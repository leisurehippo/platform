/**
 * Created by gsy on 2017/3/25.
 */

(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardManageComplaintSectionController', DashboardManageComplaintSectionController);

    DashboardManageComplaintSectionController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'PaginationUtil',
                                                            'paginationConstants', 'ThreadsBatchDelete', 'createThread', 'ThreadEditTop', 'ChangeThreadsColor', 'forumsGetAllThreadByState', 'SweetAlert'];

    function DashboardManageComplaintSectionController( $scope, $state, ParseLinks, AlertService, PaginationUtil,
                                                        paginationConstants, ThreadsBatchDelete, createThread, ThreadEditTop, ChangeThreadsColor, forumsGetAllThreadByState, SweetAlert) {
        var vm = this;
        vm.page = 1;
        // vm.selected = [];
        // vm.threadSelected = [];
        vm.isShowItem = false;
        var params = {
            page: vm.page,
            sort: 'id,asc',
            search: null
        };

        vm.note = {
            isHtmlShow: false,
            isEditShow: true,
            isConfirmEditButtonShow: true
        };
        var pagingParams = {
            page: PaginationUtil.parsePage(params.page),
            sort: params.sort,
            predicate: PaginationUtil.parsePredicate(params.sort),
            ascending: PaginationUtil.parseAscending(params.sort),
            search: params.search
        };

        function confirmEdit() {
            openHtmlShow(vm.note);
            submit();
        }

        function openHtmlShow(object) {
            object.isHtmlShow = true;
            object.isEditShow = false;
            object.isConfirmEditButtonShow = false;
        }

        vm.confirmDelete = confirmDelete;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isChecked = isChecked;
        vm.updateSelection = updateSelection;
        vm.confirmAdd = confirmAdd;
        vm.cancelNewTheme = cancelNewTheme;

        function isChecked(index) {
            return vm.selected.indexOf(index) >= 0;
        }

        function updateSelection($event, index) {
            var checkbox = $event.target ;
            var checked = checkbox.checked ;
            if(checked){
                vm.selected.push(index) ;
                vm.threadSelected.push(vm.normalThreadList[index].id);
            }else{
                var idx = vm.selected.indexOf(index);
                vm.selected.splice(idx,1) ;
                vm.threadSelected.splice(idx,1);
            }
            //console.log(vm.threadSelected);
        }
        function cancelNewTheme() {
            vm.isShowItem = false;
            //console.log(vm.isShow);
        }
        $scope.$watch('vm.refresh',function () {
            if (vm.refresh == 'manageComplaintSection') {
                loadAll();
                //console.log(vm.refresh);
            }

        });

        function confirmDelete () {
            SweetAlert.swal({
                    title: "您要删除帖子吗？",
                    text: "删除帖子后不可恢复",
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
                        ThreadsBatchDelete.update(vm.threadSelected, function (response) {
                            //console.log("success");
                            loadAll();
                        }, function () {
                            //console.log("error");
                        });
                    }
                }
            );
        }

        function confirmAdd() {
            vm.isShowItem = true;
        }

        function loadAll() {
            vm.selected = [];
            vm.threadSelected = [];
            vm.topSelected = [];
            vm.topThreadSelected = [];
            forumsGetAllThreadByState.post({
                state: 'normal',
                page: vm.page - 1,
                size: 7,
                sort: 'asc'
            }, '3', onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(result, headers) {
                var data = result.data;
                ///console.log(data);
                vm.normalThreadList = data.normalThreads.content;
                vm.topThreadList = data.topThreads;
                // vm.links = ParseLinks.parse(headers('link'));

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

        // 发帖

        vm.confirmEdit = confirmEdit;
        vm.cancelNewTheme = cancelNewTheme;

        vm.thread = {
            "authorName": null,
            "content": "",
            "forum": {
                "id": 1
            },
            "creator": {
                "nickName": "string"
            },
            "state": "normal",
            "title": "",
            "section": "3",
            "color": "black"
        };
        function confirmEdit() {
            createThread.post(vm.thread, function onSuccess(result) {
                // console.log(result);
                if(result.data.message == 'thread创建成功') {
                    vm.thread.title = "";
                    vm.thread.content = "";
                    vm.thread.color = "black";
                    vm.thread.state = "normal";
                    cancelNewTheme();
                    loadAll();
                    // console.log('thread创建成功');
                }
            });
        }

        function cancelNewTheme() {
            vm.isShowItem = false;
        }

        // 字体颜色
        vm.selectColors = ["red", "blue", "green", "black"];
        vm.selectedColor = null;
        $scope.$watch('vm.selectedColor',function () {
            for (var i=0; i < vm.selected.length; i++) {
                //console.log(vm.selected[i]);
                angular.element('#title' + vm.selected[i]).css('color', vm.selectedColor);
                ChangeThreadsColor.post({color: vm.selectedColor}, vm.threadSelected, function (result) {
                    //console.log(result);
                    loadAll();
                }, function (result) {
                    //console.log(result);
                });
            }
        });

        // 帖子置顶
        vm.confirmTop = confirmTop;
        function confirmTop() {
            var newThread = vm.normalThreadList[vm.selected[0]];
            newThread.state = "top";
            newThread.color = "red";
            ThreadEditTop.post({id:newThread.id}, newThread, function (result) {
                //console.log(result);
                loadAll();

            }, function (result) {
                //console.log(result);
            });


        }

        // 取消置顶
        vm.cancelTop = cancelTop;
        vm.isTopChecked = isTopChecked;
        vm.updateTopSelection = updateTopSelection;
        // vm.topSelected = [];
        // vm.topThreadSelected = [];
        function updateTopSelection($event, index) {
            var checkbox = $event.target ;
            var checked = checkbox.checked ;
            if(checked){
                vm.topSelected.push(index) ;
                vm.topThreadSelected.push(vm.topThreadList[index].id);
            }else{
                var idx = vm.topSelected.indexOf(index);
                vm.topSelected.splice(idx,1) ;
                vm.topThreadSelected.splice(idx,1);
            }
            //console.log(vm.topThreadList[vm.topSelected[0]]);
        }
        function isTopChecked(index) {
            return vm.topSelected.indexOf(index) >= 0;
        }
        function cancelTop() {
            var newThread = vm.topThreadList[vm.topSelected[0]];
            //console.log(newThread);
            newThread.state = "normal";
            newThread.color = "black";
            ThreadEditTop.post({id:newThread.id}, newThread, function (result) {
                //console.log(result);
                loadAll();

            }, function (result) {
                //console.log(result);
            });
        }
    }
})();

