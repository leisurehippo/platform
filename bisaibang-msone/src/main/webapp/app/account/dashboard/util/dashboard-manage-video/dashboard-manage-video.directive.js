/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardManageVideo', dashboardManageVideo);

function dashboardManageVideo() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-video/dashboard-manage-video.html',
        restrict: 'EA',
        scope: {
            refresh:'=',
            selectedVideoTag:'=',
            video:'='
        },
        controller: 'DashboardManageVideoController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
