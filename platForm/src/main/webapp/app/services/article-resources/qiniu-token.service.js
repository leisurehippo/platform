/**
 * Created by arslan on 8/22/16.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('QiniuToken', QiniuToken);

    QiniuToken.$inject = ['$resource'];

    function QiniuToken($resource) {
        var service = $resource('api/ms-article/:key/get-pic-upload-token', {}, {
            'get' : {method: 'GET'}
        });
        return service;
    }
})();
