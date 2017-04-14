
angular
    .module('bsbmsoneApp')
    .directive('dashboardManageGameMatchSection', dashboardManageGameMatchSection);

function dashboardManageGameMatchSection() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-game-match-section/dashboard-manage-game-match-section.html',
        restrict: 'EA',
        scope: {
            refresh:'='
        },
        controller: 'ManageGameMatchSectionController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
