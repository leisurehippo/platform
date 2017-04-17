(function() {
    'use strict';

    angular
        .module('app')
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
        }).state("defect-lines-chart", {
            url: "/defect-lines-chart",
            templateUrl: "app/defect-lines/defect-lines-chart.html",
            controller: "DefectLineController",
            params: {
                searchDefectLine: null
            }
        }).state("defect-lines-table", {
            url: "/defect-lines-table",
            templateUrl: "app/defect-lines/defect-lines-table.html",
            controller: "DefectLineController_t",
            params: {
                searchDefectLine: null
            }
        }).state("defect-transformers-chart", {
            url: "/defect-transformers-chart",
            templateUrl: "app/defect-transformers/defect-transformers-chart.html",
            controller: "DefectTransformerController",
            params: {
                searchDefectTransformer: null
            }
        }).state("defect-transformers-table", {
            url: "/defect-transformers-table",
            templateUrl: "app/defect-transformers/defect-transformers-table.html",
            controller: "DefectTransformerController_t",
            params: {
                searchDefectTransformer: null
            }
        }).state("gas-chart", {
            url: "/gas-chart",
            templateUrl: "app/gas/gas-chart.html",
            controller: "GasController",
            params: {}
        }).state("gas-table", {
            url: "/gas-table",
            templateUrl: "app/gas/gas-table.html",
            controller: "GasController_t",
            params: {}
        }).state("weather-chart", {
            url: "/weather-chart",
            templateUrl: "app/weather/weather-chart.html",
            controller: "WeatherController",
            params: {}
        }).state("weather-table", {
            url: "/weather-table",
            templateUrl: "app/weather/weather-table.html",
            controller: "WeatherController_t",
            params: {}
        }).state("line-status-chart", {
            url: "/line-status-chart",
            templateUrl: "app/line-status/line-status-chart.html",
            controller: "LineStatusController",
            params: {
                searchDefectTransformer: null
            }
        })
            .state("line-status-table", {
                url: "/line-status-table",
                templateUrl: "app/line-status/line-status-table.html",
                controller: "LineStatusController_t",
                params: {
                    searchDefectTransformer: null
                }
            }).state("lines", {
            url: "/lines",
            templateUrl: "app/lines/lines.html",
            controller: "LineStatusController",
            params: {}
        }).state("transformers", {
            url: "/transformers",
            templateUrl: "app/transformers/transformers.html",
            controller: "TransformerController",
            params: {

            }
        }).state("contact", {
            url: "/contact",
            templateUrl: "contact.html",
            params: {}
        })

    }
})();
