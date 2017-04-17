(function() {
    'use strict';
    angular
        .module('app')
        .factory('Comment', Comment);

    Comment.$inject = ['$resource', 'DateUtils'];

    function Comment ($resource, DateUtils) {
        var resourceUrl =  'api/comments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.editDate = DateUtils.convertDateTimeFromServer(data.editDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
