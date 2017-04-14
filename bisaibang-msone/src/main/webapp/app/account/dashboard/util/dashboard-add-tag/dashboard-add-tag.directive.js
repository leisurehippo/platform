/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardAddTag', dashboardAddTag);

function dashboardAddTag() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-add-tag/dashboard-add-tag.html',
        restrict: 'EA',
        scope: {
        },
        controller: 'DashboardAddTagController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
