/**
 * Created by Zhaoxuan on 2017/7/13.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('HdfsUpload', HdfsUpload);

    HdfsUpload.$inject = ['$resource'];

    function HdfsUpload ($resource) {
        var service = $resource('api/HdfsUpload', {}, {
            'get': {method: 'GET', params:{}, isArray: true
            }

        });

        return service;
    }
})();
