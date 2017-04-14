
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardManagePlayerInfoController', DashboardManagePlayerInfoController);

    DashboardManagePlayerInfoController.$inject = ['$scope', 'adminGetAllPlayersInfo', 'ParseLinks', 'AlertService', 'PaginationUtil', 'paginationConstants', 'DeleteTeam'];

    function DashboardManagePlayerInfoController($scope, adminGetAllPlayersInfo, ParseLinks, AlertService, PaginationUtil, paginationConstants, DeleteTeam) {
        var vm = this;
        vm.page = 1;
        vm.selected = [];
        vm.playerSelected = [];
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
        vm.isChecked = isChecked;
        vm.updateSelection = updateSelection;
        vm.confirmDelete = confirmDelete;

        function isChecked(index) {
            return vm.selected.indexOf(index) >= 0;
        }

        function updateSelection($event, index) {
            var checkbox = $event.target ;
            var checked = checkbox.checked ;
            if(checked){
                vm.selected.push(index) ;
                vm.playerSelected.push(vm.playersList[index].id);
            }else{
                var idx = vm.selected.indexOf(index);
                vm.selected.splice(idx,1) ;
                vm.playerSelected.splice(idx,1);
            }
            //console.log(vm.playerSelected);
        }

        function confirmDelete() {
            DeleteTeam.update(vm.playerSelected, function (result) {
                //console.log(result);
                loadAll();

            }, function (result) {
                //console.log(result);

            })
        }

        $scope.$watch('vm.refresh',function () {
           if (vm.refresh == 'managePlayerInfo') {
               loadAll();
               //console.log(vm.refresh);
           }

        });



        function loadAll() {
            adminGetAllPlayersInfo.get({
                page: vm.page - 1,
                size: 12,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(result, headers) {
                // vm.links = ParseLinks.parse(headers('link'));
                var data = result.data;
                //console.log(data);
                vm.playersList = data.content;
                vm.videos = data;
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
    }
})();
