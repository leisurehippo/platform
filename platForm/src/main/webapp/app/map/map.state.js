/**
 * Created by gsy on 2017/4/17.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('map', {
            parent: 'admin',
            url: '/map',
            views: {
                'content@': {
                    templateUrl: 'app/map/map.html',
                    controller: 'MapController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('logs');
                    return $translate.refresh();
                }]
            }
        });
    }
})();

