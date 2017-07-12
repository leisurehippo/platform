(function() {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        }).state('map', {
            parent: 'app',
            url: '/map',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/map/map.html',
                    controller: 'MapController',
                    controllerAs: 'vm'
                }
            }
        }).state("defect-lines-chart", {
            url: "/defect-lines-chart",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/defect-lines/defect-lines-chart.html",
                    controller: "DefectLineController",
                    controllerAs: 'vm'
                }
            },
            params: {
                searchDefectLine: null
            }
        }).state("defect-lines-table", {
            url: "/defect-lines-table",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/defect-lines/defect-lines-table.html",
                    controller: "DefectLineController_t",
                    controllerAs: 'vm'
                }
            },
            params: {
                searchDefectLine: null
            }
        }).state("defect-transformers-chart", {
            url: "/defect-transformers-chart",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/defect-transformers/defect-transformers-chart.html",
                    controller: "DefectTransformerController",
                    controllerAs: 'vm'
                }
            },
            params: {
                searchDefectTransformer: null
            }
        }).state("defect-transformers-table", {
            url: "/defect-transformers-table",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/defect-transformers/defect-transformers-table.html",
                    controller: "DefectTransformerController_t",
                    controllerAs: 'vm'
                }
            },
            params: {
                searchDefectTransformer: null
            }
        }).state("gas-chart", {
            url: "/gas-chart",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/gas/gas-chart.html",
                    controller: "GasController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("gas-table", {
            url: "/gas-table",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/gas/gas-table.html",
                    controller: "GasController_t",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("weather-chart", {
            url: "/weather-chart",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/weather/weather-chart.html",
                    controller: "WeatherController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("weather-table", {
            url: "/weather-table",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/weather/weather-table.html",
                    controller: "WeatherController_t",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("line-status-chart", {
            url: "/line-status-chart",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/line-status/line-status-chart.html",
                    controller: "LineStatusController",
                    controllerAs: 'vm'
                }
            },
            params: {
                searchDefectTransformer: null
            }
        }).state("line-status-table", {
            url: "/line-status-table",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/line-status/line-status-table.html",
                    controller: "LineStatusController_t",
                    controllerAs: 'vm'
                }
            },
            params: {
                searchDefectTransformer: null
            }
        }).state("lines", {
            url: "/lines",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/lines/lines.html",
                    controller: "LineStatusController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("transformers", {
            url: "/transformers",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/transformers/transformers.html",
                    controller: "TransformerController",
                    controllerAs: 'vm'
                }
            },
            params: {

            }
        }).state("psoLocal", {
            url: "/pso/local",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/project/pso-local/psoLocal.html",
                    controller: "PsoLocalController",
                    controllerAs: 'vm'
                }
            },
            params: {

            }
        }).state("fileUpload", {
            url: "/fileUpload",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/fileUpload/fileUpload.html",
                    controller: "FileUploadController",
                    controllerAs: 'vm'
                }
            },
            params: {
                "fileType":null,
            }
        }).state("allFileData", {
            url: "/allFileData",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/allFileData/allFileData.html",
                    controller: "AllFileDataController",
                    controllerAs: 'vm'
                }
            },
            params: {

            }
        });
    }
})();
