/**
 * Created by Zhaoxuan on 2017/7/13.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('TrainModel', TrainModel);

    TrainModel.$inject = ['$resource'];

    function TrainModel ($resource) {
        var service = $resource('api/SparkTrain', {}, {
            'get': {method: 'GET', params:{}
            }

        });

        return service;
    }
})();