(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
