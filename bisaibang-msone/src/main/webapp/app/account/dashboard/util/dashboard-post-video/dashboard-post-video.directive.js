/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardPostVideo', dashboardPostVideo);

function dashboardPostVideo() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-post-video/dashboard-post-video.html',
        restrict: 'EA',
        scope: {
            video:'='
        },
        controller: 'DashboardPostVideoController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
