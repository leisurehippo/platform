(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .filter('gblen', gblen);

    gblen.$inject = [];

    function gblen () {
        return function (input) {
            if(!input) return;
            var len = 0;
            for (var i=0; i<input.length; i++) {
                if (input.charCodeAt(i)>127 || input.charCodeAt(i)==94) {
                    len += 2;
                } else {
                    len++;
                }
            }
            return len;
        }
    }
})();
