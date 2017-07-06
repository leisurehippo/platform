(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('upLoad', upLoad);

    upLoad.$inject = ['$resource'];

    function upLoad ($resource) {
        var service = $resource('api/upload/:file', {}, {
            'post': {method: 'POST'},
        });

        return service;
    }
})();
