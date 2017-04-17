/**
 * Created by arslan on 8/25/16.
 */
(function () {
    'use strict';

    angular
        .module('app')
        .factory('QiniuUploadAvatar', QiniuUploadAvatar);

    QiniuUploadAvatar.$inject = ['$resource'];

    function QiniuUploadAvatar($resource) {
        var service = $resource('api/ms-task/update-avatar-url', {}, {
            'save': {method: 'POST'}
        });
        return service;
    }
})();
