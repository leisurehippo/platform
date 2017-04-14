
angular
    .module('bsbmsoneApp')
    .directive('dashboardManageCommunicateSection', dashboardManageCommunicateSection);

function dashboardManageCommunicateSection() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-communicate-section/dashboard-manage-communicate-section.html',
        restrict: 'EA',
        scope: {
            refresh:'='
        },
        controller: 'DashboardManageCommunicateSectionController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
