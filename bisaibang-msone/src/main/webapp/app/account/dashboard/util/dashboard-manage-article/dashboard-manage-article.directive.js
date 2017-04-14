/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardManageArticle', dashboardManageArticle);

function dashboardManageArticle() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-manage-article/dashboard-manage-article.html',
        restrict: 'EA',
        scope: {
            article:'=',
            refresh:'='
        },
        controller: 'DashboardManageArticleController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
