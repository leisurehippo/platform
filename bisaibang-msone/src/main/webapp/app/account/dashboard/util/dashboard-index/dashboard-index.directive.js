/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardIndex', dashboardIndex);

function dashboardIndex() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-index/dashboard-index.html',
        restrict: 'EA',
        scope: {
        },
        controller: 'DashboardIndexController',
        controllerAs: 'vm'
    };
    return directive;
}
