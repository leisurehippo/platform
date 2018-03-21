/**
 * Created by Gsy on 2017/7/26.
 */
(function() {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .controller('RunAlController', RunAlController);

    RunAlController.$inject = ['$state', '$scope', '$stateParams', 'GetServerProject', 'GetAlgorithmData', 'GetAlgorithmParams', 'GetServerData', 'runLocal', 'GetParameter', 'RunMLlib'];

    function RunAlController ($state, $scope, $stateParams, GetServerProject, GetAlgorithmData, GetAlgorithmParams, GetServerData, runLocal, GetParameter,RunMLlib) {
        var vm = this;
        vm.projects = [];
        vm.projectName = null;
        vm.checkList = [];
        vm.test = test;
        vm.serverData = [];
        vm.paramDes = [];
        vm.param = [];
        vm.results = [];
        vm.check = null;
        vm.alMLLib = null;
        vm.paramMllib = [];
        vm.runMllibAl = runMllibAl;
        vm.tradinData = null;
        vm.testData = null;
        vm.showResults = false;

        function test() {
            console.log(vm.checkList);
        }

        GetServerProject.get({}, function (res) {
            vm.projects = res;
            console.log(vm.projects);
            if ($stateParams.projectName == null && vm.projects.length > 0) {
                vm.projectName = vm.projects[0];
                console.log(vm.projectName);
            } else
                vm.projectName = $stateParams.projectName;

        }, function (res) {

        });

        var type="Algorithm";
        $scope.$watch('vm.projectName', function () {
            console.log("run-al-server");
            vm.serverData = [];
            vm.paramDes = [];
            vm.param = [];
            vm.results = [];
            vm.check = null;
            vm.alMLLib = null;
            vm.paramMllib = [];
            vm.tradinData = null;
            vm.testData = null;
            vm.showResults = false;
            if (vm.projectName != null) {
                GetAlgorithmData.get({ProjectName:vm.projectName},function (res) {
                    console.log(res);
                    vm.algrithmData = res;
                }, function (result) {
                });
            }

        });

        $scope.$watch('vm.check', function (newvalue, oldvalue) {
            console.log("11111");
            console.log(newvalue);
            vm.showResults = false;
            if (newvalue!="Mllib") {
                GetAlgorithmParams.get({ProjectName:vm.projectName, AlgorithmName:vm.algrithmData[newvalue]},
                    function (res) {
                        vm.paramDes = res;
                        console.log(vm.paramDes);
                        GetServerData.get({ProjectName:vm.projectName}, function (result) {
                            for (var i = 0; i< result.length; i++) {
                                vm.serverData[i] = result[i].split("+");
                                vm.checkList[i] = false;
                                if (vm.serverData[i][1] == '0') {
                                    vm.serverData[i][1] = false;
                                }else
                                    vm.serverData[i][1] = true;

                            }
                            console.log(vm.serverData);
                        }, function (res) {
                            console.log(res);
                        });


                    }, function (res) {
                        console.log(res);
                    });
            } else {
                if (vm.alMLLib!=null)
                    console.log("dddd")
                    GetParameter.get({Algorithm: vm.alMLLib},  function (res) {
                        for (var i = 0; i< res.length; i++) {
                            vm.param[i] = res[i].split(":");
                        }
                        angular.copy(vm.param, vm.paramMllib);
                        GetServerData.get({ProjectName:vm.projectName}, function (result) {
                            for (var i = 0; i< result.length; i++) {
                                vm.serverData[i] = result[i].split("+");
                                vm.checkList[i] = false;
                                if (vm.serverData[i][1] == '0') {
                                    vm.serverData[i][1] = false;
                                }else
                                    vm.serverData[i][1] = true;

                            }
                            console.log(vm.serverData);
                        }, function (res) {
                            console.log(res);
                        });
                    }, function (res) {
                        console.log(res);
                    });

            }

        });

        $scope.$watch('vm.alMLLib', function (newvalue, oldvalue) {
            vm.showResults = false;
            GetParameter.get({Algorithm: vm.alMLLib},  function (res) {
                for (var i = 0; i< res.length; i++) {
                    vm.param[i] = res[i].split(":");
                }

                angular.copy(vm.param, vm.paramMllib);
                GetServerData.get({ProjectName:vm.projectName}, function (result) {
                    console.log(result);
                    for (var i = 0; i< result.length; i++) {
                        vm.serverData[i] = result[i].split("+");
                        vm.checkList[i] = false;
                        if (vm.serverData[i][1] == '0') {
                            vm.serverData[i][1] = false;
                        }else
                            vm.serverData[i][1] = true;

                    }
                    console.log(vm.serverData);
                }, function (res) {
                    console.log(res);
                });
                console.log( vm.paramMllib);
            }, function (res) {
                console.log(res);
            });
        });

        vm.saveResult = saveResult;
        vm.fileNameSave = null;
        function saveResult() {
            var results  = "";
            for(var k in vm.results) {
                results += vm.results[k] + "\n";
            }
            var blob = new Blob([results], {type: "text/plain;charset=utf-8"});
            saveAs(blob, vm.fileNameSave+".txt");
        }

        vm.runAl = runAl;
        function runAl() {
            vm.showResults = true;
            console.log(vm.param);
            var str = '{';
            for(var k = 0; k < vm.paramDes.length; k++) {
                str += "\""+vm.paramDes[k][0].toString()+"\"" + ":"; //加参
                if (vm.paramDes[k][2] == 'true')
                    str += "\"src/main/webappfiles/Project/"+vm.projectName+"/Data/" +vm.param[k].toString()+"\"" + ",";
                else
                    str += "\"" +vm.param[k].toString()+"\"" + ",";
            }
            str = str.substring(0, str.lastIndexOf(','));
            str += '}';
            console.log(str);
            console.log( JSON.parse(str));
            runLocal.get({ProjectName:vm.projectName, AlgorithmName:vm.algrithmData[vm.check], Params:str, isPython:true}, function (res) {
                console.log(res);
                vm.results = res;

            }, function (res) {

            });

        }

        function runMllibAl() {
            vm.showResults = true;

            var params = "{";
            var count = 0;
            for( var i = 0; i< vm.param.length; i++) {
                if (vm.param[i][1] != vm.paramMllib[i][1]) {
                    count ++;
                    if (count == 1)
                        params += vm.paramMllib[i][0] + ":" + vm.paramMllib[i][1];
                    else
                        params += "," + vm.paramMllib[i][0] + ":" + vm.paramMllib[i][1];

                }
            }
            params += "}";
            console.log(params);
            RunMLlib.get({ProjectName:vm.projectName, trainDataName:vm.trainData, testDataName:vm.testData,
                Parameters:params, Algorithm:vm.alMLLib}, function (res) {
                vm.results = res;
                console.log(res);
            },function (res) {
                console.log(res);
            });
        }

    }
})();
