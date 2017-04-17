(function () {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('ow-register', {
            parent: 'app',
            url: '/participant',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/participant/participant.html',
                    controller: 'ParticipantController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
