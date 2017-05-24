/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .directive('footer', footer);

function footer() {
    return {
        restrict: 'E',
        templateUrl: 'components/footer.html',
        replace: true
    };
}
