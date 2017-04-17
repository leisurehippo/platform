/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('LineStatusController',LineStatusController);

LineStatusController.$inject = ['$scope', '$http'];

function LineStatusController($scope, $http) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.getLineStatus = function(){
        $http.jsonp(host + 'line-statuses?callback=JSON_CALLBACK')
            .success(function(data)
            {
                statisticsLineStatus(data);
            }).error(function(msg){
            console.log(msg);
        });
    };
    function statisticsLineStatus(data){
        var ia_start = Array();
        var ia_end = Array();
        var ib_start = Array();
        var ib_end = Array();
        var ic_start = Array();
        var ic_end = Array();
        var time = Array();
        $.each(data, function(i, val){
            ia_start.push(val.ia_start);
            ia_end.push(val.ia_end);
            ib_start.push(val.ib_start);
            ib_end.push(val.ib_end);
            ic_start.push(val.ic_start);
            ic_end.push(val.ic_end);
            time.push(val.time);
        });
        showLineStatusGraph(ia_start, ia_end,ib_start, ib_end,ic_start, ic_end,time);
    }
    function showLineStatusGraph(ia_start, ia_end,ib_start, ib_end,ic_start, ic_end,time){
        var myChart = echarts.init(document.getElementById('graph'));
        var option = {
            title: {
                text: '线路状态变化图'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['起点电站Ia值	','终点电站Ia值','起点电站Ib值','终点电站Ib值','起点电站Ic值','终点电站Ic值']
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
                    data : time
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [

                {
                    name:'起点电站Ia值',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data : ia_start
                },
                {
                    name:'终点电站Ia值',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:ia_end
                },
                {
                    name:'起点电站Ib值',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:ib_start
                },
                {
                    name:'终点电站Ib值',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:ib_end
                },
                {
                    name:'起点电站Ic值',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:ic_start
                },
                {
                    name:'终点电站Ic值',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:ic_end
                },
            ]
        };

        myChart.setOption(option);
    }
}
