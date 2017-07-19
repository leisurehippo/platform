(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Predict', Predict);

    Predict.$inject = ['$resource'];

    function Predict ($resource) {
        var service = $resource('api/SparkPredict', {}, {
            'get': {method: 'GET', params:{}, isArray: true
            }
        });

        return service;
    }
})();
