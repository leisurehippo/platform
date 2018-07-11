/**
 * Created by Gsy on 2017/7/31.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('UploadParamsDes', UploadParamsDes);

    UploadParamsDes.$inject = ['$resource'];

    function UploadParamsDes ($resource) {
        var service = $resource('api/uploadParameterDescribe', {}, {
            'post': {method: 'POST', params:{}, isArray: false
            }

        });

        return service;
    }
})();
