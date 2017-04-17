/**
 * Created by arslan on 9/19/16.
 */
(function () {
    'use strict';

    angular
        .module('app')
        .directive('fileChange', fileChange);

    fileChange.$inject = [];
    function fileChange() {
        var directive = {
            restrict: 'A',
            require: 'ngModel',
            scope: {
                fileChange: '&'
            },
            link: link
        };
        return directive;
        function link(scope, element, attrs, ctrl) {
            element.on('change', onChange);

            scope.$on('destroy', function () {
                element.off('change', onChange);
            });

            function onChange() {

                attrs.multiple ? ctrl.$setViewValue(element[0].files) : ctrl.$setViewValue(element[0].files[0]);
                scope.$apply(function () {
                    //console.log(element[0].files[0]);
                    scope.fileChange(attrs.multiple ? element[0].files : element[0].files[0]);
                });
            }
        }
    }
})();
