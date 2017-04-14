
angular
    .module('bsbmsoneApp')
    .directive('dashboardManagePlayerGroup', dashboardManagePlayerGroup);

function dashboardManagePlayerGroup() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-player-group/dashboard-manage-player-group.html',
        restrict: 'EA',
        scope: {
            refresh:'='
        },
        controller: 'DashboardManagePlayerGroupController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
