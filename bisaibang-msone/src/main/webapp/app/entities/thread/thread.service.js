(function() {
    'use strict';
    angular
        .module('bsbmsoneApp')
        .factory('Thread', Thread);

    Thread.$inject = ['$resource', 'DateUtils'];

    function Thread ($resource, DateUtils) {
        var resourceUrl =  'api/threads/:id';

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
