
angular
    .module('bsbmsoneApp')
    .directive('dashboardManagePlayerInfo', dashboardManagePlayerInfo);

function dashboardManagePlayerInfo() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-player-info/dashboard-manage-player-info.html',
        restrict: 'EA',
        scope: {
            refresh:'='
        },
        controller: 'DashboardManagePlayerInfoController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
