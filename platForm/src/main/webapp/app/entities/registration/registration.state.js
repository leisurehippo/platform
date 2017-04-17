(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('registration', {
            parent: 'entity',
            url: '/registration?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bsbmsoneApp.registration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registration/registrations.html',
                    controller: 'RegistrationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('registration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('registration-detail', {
            parent: 'registration',
            url: '/registration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bsbmsoneApp.registration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registration/registration-detail.html',
                    controller: 'RegistrationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('registration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Registration', function($stateParams, Registration) {
                    return Registration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'registration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('registration-detail.edit', {
            parent: 'registration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration/registration-dialog.html',
                    controller: 'RegistrationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Registration', function(Registration) {
                            return Registration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('registration.new', {
            parent: 'registration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration/registration-dialog.html',
                    controller: 'RegistrationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                name: null,
                                sex: null,
                                phone: null,
                                email: null,
                                idCard: null,
                                idGame: null,
                                teamName: null,
                                teamMate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('registration', null, { reload: 'registration' });
                }, function() {
                    $state.go('registration');
                });
            }]
        })
        .state('registration.edit', {
            parent: 'registration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration/registration-dialog.html',
                    controller: 'RegistrationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Registration', function(Registration) {
                            return Registration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registration', null, { reload: 'registration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('registration.delete', {
            parent: 'registration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registration/registration-delete-dialog.html',
                    controller: 'RegistrationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Registration', function(Registration) {
                            return Registration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registration', null, { reload: 'registration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
