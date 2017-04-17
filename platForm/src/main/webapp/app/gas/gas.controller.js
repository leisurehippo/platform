/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('GasController',GasController);

GasController.$inject = ['$scope', '$http'];

function GasController($scope, $http) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.getGas = function(){
        $http.jsonp(host + 'gas?callback=JSON_CALLBACK')
            .success(function(data)
            {
                statisticsGas(data);
            }).error(function(msg){
            console.log(msg);
        });
    };
    function statisticsGas(data){
        $scope.devices = Array();
        var res = {};
        $.each(data, function(i, obj){
            if($scope.devices.indexOf(obj.device_id) == -1) {
                $scope.devices.push(obj.device_id);
                res[obj.device_id] = {};
                //res[obj.device_id].phase = Array();
                res[obj.device_id].h2 = Array();
                res[obj.device_id].ch4 = Array();
                res[obj.device_id].c2h4 = Array();
                res[obj.device_id].c2h2 = Array();
                res[obj.device_id].co = Array();
                res[obj.device_id].co2 = Array();
                res[obj.device_id].o2 = Array();
                res[obj.device_id].n2 = Array();
                res[obj.device_id].c2h6 = Array();
                res[obj.device_id].total = Array();
                res[obj.device_id].time = Array();
            }
            res[obj.device_id].time.push(obj.time);
            //res[obj.device_id].phase.push(obj.phase);
            res[obj.device_id].h2.push(obj.h2);
            res[obj.device_id].ch4.push(obj.ch4);
            res[obj.device_id].c2h4.push(obj.c2h4);
            res[obj.device_id].c2h2.push(obj.c2h2);
            res[obj.device_id].co.push(obj.co);
            res[obj.device_id].co2.push(obj.co2);
            res[obj.device_id].o2.push(obj.o2);
            res[obj.device_id].n2.push(obj.n2);
            res[obj.device_id].c2h6.push(obj.c2h6);
            res[obj.device_id].total.push(obj.total);

        });
        showGasGraph(res,"");
    }
    function showGasGraph(data, selection){
        selection = selection || $scope.devices[0];
        var myChart = echarts.init(document.getElementById('graph'));
        var option = {
            title: {
                text: '在线监测变化图'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['氢气','甲烷','乙烯','乙炔','一氧化碳','二氧化碳','氧气','氮气','乙烷','总烃']
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
                    name:'氢气',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data : data[selection].h2
                },
                {
                    name:'甲烷',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data:data[selection].ch4
                },
                {
                    name:'乙烯',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data : data[selection].c2h4
                },
                {
                    name:'乙炔',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data:data[selection].c2h2
                },
                {
                    name:'一氧化碳',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data : data[selection].co
                },
                {
                    name:'二氧化碳',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data:data[selection].co2
                },
                {
                    name:'氧气',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data : data[selection].o2
                },
                {
                    name:'氮气',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data:data[selection].n2
                },
                {
                    name:'乙烷',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data : data[selection].c2h6
                },
                {
                    name:'总烃',
                    type:'line',
                    stack: '总量',
                    areaStyle: {normal: {}},
                    data:data[selection].total
                },
            ]
        };

        myChart.setOption(option);
    }
}