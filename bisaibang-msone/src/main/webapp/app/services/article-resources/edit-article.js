/**
 * Created by arslan on 3/23/2017.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('EditArticle', EditArticle);

    EditArticle.$inject = ['$resource'];

    function EditArticle($resource) {
        var service = $resource('api/ms-article/edit/article/:articleid', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
