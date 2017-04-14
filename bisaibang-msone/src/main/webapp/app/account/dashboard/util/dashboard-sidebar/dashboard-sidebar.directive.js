/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardSideBar', dashboardSideBar);

function dashboardSideBar() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-sidebar/dashboard-sidebar.html',
        restrict: 'EA',
        scope: {
            selectedTab: '=',
            selectedVideoTag:'=',
            video: '=',
            article: '='
        },
        controller: 'DashboardSideBarController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
