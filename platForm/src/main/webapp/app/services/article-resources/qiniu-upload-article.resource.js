/**
 * Created by arslan on 3/8/17.
 */
(function () {
    'use strict';

    angular
        .module('app')
        .factory('QiniuUploadArticleImage', QiniuUploadArticleImage);

    QiniuUploadArticleImage.$inject = ['$resource'];

    function QiniuUploadArticleImage($resource) {
        var service = $resource('api/ms-article/update-pic-url/article/:id', {}, {
            'update': {method: 'PUT'}
        });
        return service;
    }
})();
