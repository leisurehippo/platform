(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('term', {
            parent: 'entity',
            url: '/term?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.term.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/term/terms.html',
                    controller: 'TermController',
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
                    $translatePartialLoader.addPart('term');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('term-detail', {
            parent: 'term',
            url: '/term/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.term.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/term/term-detail.html',
                    controller: 'TermDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('term');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Term', function($stateParams, Term) {
                    return Term.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'term',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('term-detail.edit', {
            parent: 'term-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/term/term-dialog.html',
                    controller: 'TermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Term', function(Term) {
                            return Term.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('term.new', {
            parent: 'term',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/term/term-dialog.html',
                    controller: 'TermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                slug: null,
                                groupTerm: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('term', null, { reload: 'term' });
                }, function() {
                    $state.go('term');
                });
            }]
        })
        .state('term.edit', {
            parent: 'term',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/term/term-dialog.html',
                    controller: 'TermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Term', function(Term) {
                            return Term.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('term', null, { reload: 'term' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('term.delete', {
            parent: 'term',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/term/term-delete-dialog.html',
                    controller: 'TermDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Term', function(Term) {
                            return Term.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('term', null, { reload: 'term' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
