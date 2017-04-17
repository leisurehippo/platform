(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('thread', {
            parent: 'entity',
            url: '/thread?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.thread.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/thread/threads.html',
                    controller: 'ThreadController',
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
                    $translatePartialLoader.addPart('thread');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('thread-detail', {
            parent: 'thread',
            url: '/thread/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.thread.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/thread/thread-detail.html',
                    controller: 'ThreadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('thread');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Thread', function($stateParams, Thread) {
                    return Thread.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'thread',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('thread-detail.edit', {
            parent: 'thread-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thread/thread-dialog.html',
                    controller: 'ThreadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Thread', function(Thread) {
                            return Thread.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('thread.new', {
            parent: 'thread',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thread/thread-dialog.html',
                    controller: 'ThreadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                authorName: null,
                                title: null,
                                content: null,
                                viewNum: null,
                                postNum: null,
                                createDate: null,
                                editDate: null,
                                state: null,
                                isAbandon: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('thread', null, { reload: 'thread' });
                }, function() {
                    $state.go('thread');
                });
            }]
        })
        .state('thread.edit', {
            parent: 'thread',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thread/thread-dialog.html',
                    controller: 'ThreadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Thread', function(Thread) {
                            return Thread.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('thread', null, { reload: 'thread' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('thread.delete', {
            parent: 'thread',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/thread/thread-delete-dialog.html',
                    controller: 'ThreadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Thread', function(Thread) {
                            return Thread.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('thread', null, { reload: 'thread' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
