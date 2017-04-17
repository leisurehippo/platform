(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('forum', {
            parent: 'entity',
            url: '/forum?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.forum.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forum/forums.html',
                    controller: 'ForumController',
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
                    $translatePartialLoader.addPart('forum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('forum-detail', {
            parent: 'forum',
            url: '/forum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'app.forum.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forum/forum-detail.html',
                    controller: 'ForumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('forum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Forum', function($stateParams, Forum) {
                    return Forum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'forum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('forum-detail.edit', {
            parent: 'forum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum/forum-dialog.html',
                    controller: 'ForumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Forum', function(Forum) {
                            return Forum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forum.new', {
            parent: 'forum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum/forum-dialog.html',
                    controller: 'ForumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                manager: null,
                                state: null,
                                isAbandon: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('forum', null, { reload: 'forum' });
                }, function() {
                    $state.go('forum');
                });
            }]
        })
        .state('forum.edit', {
            parent: 'forum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum/forum-dialog.html',
                    controller: 'ForumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Forum', function(Forum) {
                            return Forum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forum', null, { reload: 'forum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forum.delete', {
            parent: 'forum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum/forum-delete-dialog.html',
                    controller: 'ForumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Forum', function(Forum) {
                            return Forum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forum', null, { reload: 'forum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
