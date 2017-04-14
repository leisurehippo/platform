(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('dashboard', {
            parent: 'account',
            url: '/dashboard',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.dashboard'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/dashboard/dashboard.html',
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
