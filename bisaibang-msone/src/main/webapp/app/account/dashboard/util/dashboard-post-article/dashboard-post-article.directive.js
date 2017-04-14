/**
 * Created by arslan on 1/31/17.
 */
angular
    .module('bsbmsoneApp')
    .directive('dashboardPostArticle', dashboardPostArticle);

function dashboardPostArticle() {
    var directive = {
        templateUrl: 'app/account/dashboard/util/dashboard-post-article/dashboard-post-article.html',
        restrict: 'EA',
        scope: {
            article:'='
        },
        controller: 'DashboardPostArticleController',
        controllerAs: 'vm',
        bindToController: true
    };
    return directive;
}
