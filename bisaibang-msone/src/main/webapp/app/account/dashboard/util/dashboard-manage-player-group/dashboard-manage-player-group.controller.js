
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardManagePlayerGroupController', DashboardManagePlayerGroupController);

    DashboardManagePlayerGroupController.$inject = ['$scope', 'adminGetAllPlayersInfo', 'ParseLinks', 'AlertService', 'PaginationUtil', 'paginationConstants', 'DeleteTeam', 'RegisterBatchR', 'UpdateGroup', 'GetRegistrationNoPage'];

    function DashboardManagePlayerGroupController($scope, adminGetAllPlayersInfo, ParseLinks, AlertService, PaginationUtil, paginationConstants, DeleteTeam, RegisterBatchR, UpdateGroup, GetRegistrationNoPage) {
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

        vm.selectGroupNums =['128', '64', '32'];
        vm.selectedGroupNum = 128;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isChecked = isChecked;
        vm.updateSelection = updateSelection;
        vm.registerRs = registerRs;
        vm.groupRs = groupRs;



        function registerRs() {
            RegisterBatchR.update({k:500}, {}, function (response) {
                //console.log(response);
                loadAll();
            }, function (response) {
                //console.log(response);
            });
        }

        function groupRs() {
            var maxGroupLength = parseInt(vm.selectedGroupNum);

            var totalLength = vm.playersList.length;

            // 总的组数
            var groupsNum = parseInt(totalLength / maxGroupLength + 1);
            // 未满组的人数
            var lastGroupPersons = parseInt(totalLength % maxGroupLength);
            console.log(lastGroupPersons);
            // var groupsNum = 12;
            // 表示每组已有人数
            var groupsLength = new Int16Array(parseInt(groupsNum - 1));
            // 0表示未被分组,1表示已经分组
            var groupTag = new Int8Array(parseInt(totalLength));

            if (groupsNum > 1) {
                var test = [];
                // 随机选出未排满组的所有人
                for (var i = 0; i < lastGroupPersons; i++) {
                    var rand = 0;
                    for (var j = 0, flag = 0; flag != 1; j++) {
                        rand = Math.floor(Math.random() * parseInt(totalLength) + 1);
                        if (groupTag[rand - 1] == 0) {
                            groupTag[rand - 1] = 1;
                            test.push(rand);
                            flag = 1;
                            UpdateGroup.update({registrationid: vm.playersList[rand - 1].id, groupid: groupsNum}, {},
                                function success(result) {
                                    // console.log(result);
                                }, function error(result) {
                                    console.log(result);
                                });
                        }
                    }

                }
                console.log(test);
                console.log(totalLength);
                var rand = 0;
                for (var i = 0; i < totalLength; i++) {

                    if (groupTag[i] == 1)
                        continue;
                    for (var j = 0, flag = 0; flag != 1; j++) {
                        rand = Math.floor(Math.random() * parseInt(groupsNum - 1) + 1);
                         console.log(groupsLength);
                        if (groupsLength[rand - 1] < maxGroupLength) {
                            // groupTag[i] = 1;
                            groupsLength[rand - 1]++;
                            flag = 1;
                        }
                    }

                    UpdateGroup.update({registrationid: vm.playersList[i].id, groupid: rand}, {},
                        function success(result) {
                            // console.log(result);
                        }, function error(result) {
                            console.log(result);
                        });

                }
                console.log(groupsLength);
            }
            else {
                for (var i = 0; i < totalLength; i++) {
                    UpdateGroup.update({registrationid: vm.playersList[i].id, groupid: 1}, {},
                        function success(result) {
                            // console.log(result);
                        }, function error(result) {
                            console.log(result);
                        });
                }
            }

        }

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


        $scope.$watch('vm.refresh',function () {
           if (vm.refresh == 'managePlayerGroup') {
               loadAll();
               //console.log(vm.refresh);
           }

        });



        function loadAll() {
            GetRegistrationNoPage.get({}, onSuccess, onError);

            function onSuccess(result, headers) {
                // vm.links = ParseLinks.parse(headers('link'));
                var data = result.data;
                console.log(data);
                 vm.playersList = data;

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
            loadAll();
        }
    }
})();
