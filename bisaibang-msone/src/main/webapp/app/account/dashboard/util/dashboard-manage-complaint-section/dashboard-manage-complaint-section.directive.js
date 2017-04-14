/**
 * Created by gsy on 2017/3/25.
 */

angular
    .module('bsbmsoneApp')
    .directive('dashboardManageComplaintSection', dashboardManageComplaintSection);

function dashboardManageComplaintSection() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-complaint-section/dashboard-manage-complaint-section.html',
        restrict: 'EA',
        scope: {
            refresh:'='
        },
        controller: 'DashboardManageComplaintSectionController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
