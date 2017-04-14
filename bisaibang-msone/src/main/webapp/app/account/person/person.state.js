(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('personInfo', {
            parent: 'account',
            url: '/personInfo',
            data: {
                // authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.person'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/person/person.html',
                    controller: 'PersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    // $translatePartialLoader.addPart('person');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
