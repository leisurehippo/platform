/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('CreateArticle', CreateArticle);

    CreateArticle.$inject = ['$resource'];

    function CreateArticle($resource) {
        var service = $resource('api/ms-article/create', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
