/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('WeatherController',WeatherController);

WeatherController.$inject = ['$scope', '$http'];

function WeatherController($scope, $http) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.getWeather = function(){
        $http.jsonp(host + 'weathers?callback=JSON_CALLBACK')
            .success(function(data)
            {
                statisticsWeather(data);
            }).error(function(msg){
            console.log(msg);
        });
    };
    function statisticsWeather(data){
        $scope.devices = Array();
        var res = {};
        $.each(data, function(i, obj){
            if($scope.devices.indexOf(obj.device) == -1) {
                $scope.devices.push(obj.device);
                res[obj.device] = {};
                res[obj.device].time = Array();
                res[obj.device].direction = Array();
                res[obj.device].speed = Array();
                res[obj.device].direction_gust = Array();
                res[obj.device].speed_gust = Array();
                res[obj.device].precipitation = Array();
                res[obj.device].humidity = Array();
                res[obj.device].temperature = Array();
                res[obj.device].pressure = Array();
                res[obj.device].visibility = Array();
                res[obj.device].direction_max = Array();
                res[obj.device].speed_max = Array();
            }
            res[obj.device].time.push(obj.time);
            res[obj.device].direction.push(obj.direction  == 9999.9 ? null : obj.direction);
            res[obj.device].speed.push(obj.speed);
            res[obj.device].direction_gust.push(obj.direction_gust);
            res[obj.device].speed_gust.push(obj.speed_gust);
            res[obj.device].precipitation.push(obj.precipitation == 9999.9 ? null : obj.precipitation);
            res[obj.device].humidity.push(obj.humidity);
            res[obj.device].temperature.push(obj.temperature);
            res[obj.device].pressure.push(obj.pressure);
            res[obj.device].visibility.push(obj.visibility  == 9999.9 ? null : obj.visibility);
            res[obj.device].direction_max.push(obj.direction_max);
            res[obj.device].speed_max.push(obj.speed_max);
        });
        showWeatherGraph(res,"");
    }
    function showWeatherGraph(data, selection){
        selection = selection || $scope.devices[0];
        var myChart = echarts.init(document.getElementById('graph'));
        var option = {
            title: {
                text: '气象状态变化图'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['风向','风速','阵风风向','阵风风速','降水量','相对湿度','温度','气压','能见度','级大风风向','级大风风速']
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data[selection].time
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'风向',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data : data[selection].direction
                },
                {
                    name:'风速',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:data[selection].speed
                },
                {
                    name:'阵风风向',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data : data[selection].direction_gust
                },
                {
                    name:'阵风风速',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:data[selection].speed_gust
                },
                {
                    name:'降水量',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data : data[selection].precipitation
                },
                {
                    name:'相对湿度',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:data[selection].humidity
                },
                {
                    name:'温度',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data : data[selection].temperature
                },
                {
                    name:'气压',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:data[selection].pressure
                },
                {
                    name:'能见度',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data : data[selection].visibility
                },
                {
                    name:'级大风风向',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:data[selection].direction_max
                },
                {
                    name:'级大风风速',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:data[selection].speed_max
                }
            ]
        };

        myChart.setOption(option);
    }
}
