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
                    templateUrl: 'app/elec/map/map.html',
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
                    templateUrl: "app/elec/defect-lines/defect-lines-chart.html",
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
                    templateUrl: "app/elec/defect-lines/defect-lines-table.html",
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
                    templateUrl: "app/elec/defect-transformers/defect-transformers-chart.html",
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
                    templateUrl: "app/elec/defect-transformers/defect-transformers-table.html",
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
                    templateUrl: "app/elec/gas/gas-chart.html",
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
                    templateUrl: "app/elec/gas/gas-table.html",
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
                    templateUrl: "app/elec/weather/weather-chart.html",
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
                    templateUrl: "app/elec/weather/weather-table.html",
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
                    templateUrl: "app/elec/line-status/line-status-chart.html",
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
                    templateUrl: "app/elec/line-status/line-status-table.html",
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
                    templateUrl: "app/elec/lines/lines.html",
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
                    templateUrl: "app/elec/transformers/transformers.html",
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
        }).state("dataLabel", {
              url: "/dataLabel",
              parent: 'app',
              data: {
                  authorities: []
              },
              views: {
                  'content@': {
                      templateUrl: "app/project/datalabel/dataLabel.html",
                      controller: "DataLabelController",
                      controllerAs: 'vm'
                  }
              },
              params: {

              }
        }).state("dataLabel_db", {
                url: "/dataLabel/db",
                parent: 'app',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: "app/project/datalabel/dataLabeldb.html",
                        controller: "DataLabelController",
                        controllerAs: 'vm'
                    }
                },
                params: {

                }
        }).state("dataLabel_admin", {
	          url: "/dataLabel/admin",
	          parent: 'app',
	          data: {
	              authorities: []
	          },
	          views: {
	              'content@': {
	                  templateUrl: "app/project/datalabel_admin/dataLabeladmin.html",
	                  controller: "DataLabelAdminController",
	                  controllerAs: 'vm'
	              }
	          },
	          params: {
	
	          }
        }).state("dataLabel_train", {
            url: "/dataLabel/train",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/project/datalabel_train/dataLabeltrain.html",
                    controller: "DataLabelTrainController",
                    controllerAs: 'vm'
                }
            },
            params: {

            }
         }).state("dataLabel_manage", {
            url: "/dataLabel/manage",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/project/datalabel_manage/dataLabelmanage.html",
                    controller: "DataLabelManageController",
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
                    // controller: "FileUploadController",
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
                    templateUrl: "app/algorithm_project/allFileData/allFileData.html",
                    controller: "AllFileDataController",
                    controllerAs: 'vm'
                }
            },
            params: {
                "projectName":null

            }
        }).state("allFileAl", {
            url: "/allFileAl",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algorithm_project/allFileAl/allFileAl.html",
                    controller: "AllFileAlController",
                    controllerAs: 'vm'
                }
            },
            params: {
                "projectName":null
            }
        }).state("LRTrain", {
            url: "/LR/Train",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/MLLib/predict/LR/Train/LRTrain.html",
                    controller: "LRTrainController",
                    controllerAs: 'vm'
                }
            },
            params: {

            }
        }).state("LRTest", {
            url: "/LR/Test",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/MLLib/predict/LR/Test/LRTest.html",
                    controller: "LRTestController",
                    controllerAs: 'vm'
                }
            },
            params: {

            }
        }).state("projectManage", {
            url: "/projectManage",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algorithm_project/projectManage/projectManage.html",
                    controller: "ProjectManageController",
                    controllerAs: 'vm'
                }
            },
            params: {
                "projectName":null
            }
        }).state("RunAl", {
            url: "/run-algorithm",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algorithm_project/runAl/runAl.html",
                    controller: "RunAlController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("visualization", {
            url: "/data-visualization",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/elec/data-visualization/data-visualization.html",
                    controller: "VisualizationController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("etl", {
            url: "/etl",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/etl/etl.html",
                    controller: "ETLController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("textetl", {
            url: "/textetl",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/etl/textETL.html",
                    controller: "TextETLController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("templatefill", {
            url: "/templatefill",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/template-fill/templateFill.html",
                    controller: "TemplateFillController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("elec", {
            url: "/elec",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/elec/elec.html",
                    controller: "ElecController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("task", {
            url: "/task",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algo_project/taskManage/task.html",
                    controller: "TaskController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("taskFile", {
            url: "/taskFile",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algo_project/fileManage/file.html",
                    controller: "TaskFileController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("visualize", {
            url: "/visualize",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algo_project/vis/vis.html",
                    controller: "VisualizeController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("taskRun", {
            url: "/taskRun",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algo_project/run/run.html",
                    controller: "RunController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("taskRunML", {
            url: "/taskRunML",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algo_project/runML/runML.html",
                    controller: "RunMLController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        }).state("preprocess", {
            url: "/preprocess",
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: "app/algo_project/preprocess/preprocess.html",
                    controller: "PreprocessController",
                    controllerAs: 'vm'
                }
            },
            params: {}
        })
        
        
        ;
    }
})();
