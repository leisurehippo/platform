/**
 * Created by bisaibang-player on 2017/4/5.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('NewsGetArticle', NewsGetArticle);

    NewsGetArticle.$inject = ['$resource'];

    function NewsGetArticle($resource) {
        var service = $resource('api/ms-article/find/article-all', {}, {
            'query': { method: 'GET', isArray: false}
        });
        return service;
    }
})();
