(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .filter('registerSex', registerSex);

    registerSex.$inject = [];

    function registerSex () {
        return function (input, type) {

            if (input === "female") {
                return "女";
            }
            if (input === "male") {
                return "男";
            }
            if (input === null) {
                return 0;
            }
        }
    }
})();
