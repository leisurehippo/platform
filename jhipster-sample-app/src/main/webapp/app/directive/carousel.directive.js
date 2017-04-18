/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .directive('carousel', carousel);

function carousel() {
    return {
        restrict: 'E',
        templateUrl:    'components/carousel.html',
        replace: true
    };
}
