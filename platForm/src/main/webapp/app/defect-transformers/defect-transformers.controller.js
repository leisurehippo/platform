/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('DefectTransformerController', DefectTransformerController);

DefectTransformerController.$inject = ['$scope', '$http', '$stateParams'];

function DefectTransformerController($scope, $http, $stateParams) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.getDefectTransformers = function(){
        var url = host + 'defect-transformers?callback=JSON_CALLBACK';
        $http.jsonp(url)
            .success(function(data)
            {
                $scope.defectTransformers = data;
                statisticsDefectTransformers($scope.defectTransformers);
            }).error(function(msg){
            console.log(msg);
        });
    };
    function statisticsDefectTransformers(data) {
        var levels = {};
        var device_types = {};
        $.each(data, function(i, obj){
            if (obj.level in levels){
                levels[obj.level] += 1
            }else {
                levels[obj.level] = 1
            }
            obj.device_type = obj.device_type || '不确定';
            if (obj.device_type in device_types){
                device_types[obj.device_type] += 1
            }else {
                device_types[obj.device_type] = 1
            }
        });
        device_types['其他'] = 0;
        $.each(device_types, function(i, val) {
            if (device_types[i] < 2) {
                delete device_types[i];
                device_types['其他'] += val;
            }
        });
        //console.log(device_types, levels);
        showDefectTransformersGraph(device_types, levels);
    }
    function showDefectTransformersGraph(device_types,levels){
        var myChart1 = echarts.init(document.getElementById('graph1'));
        var myChart2 = echarts.init(document.getElementById('graph2'));
        var showDevicetTypes = [];
        var showLevels = [];
        $.each(device_types, function(i,val) {
            showDevicetTypes.push({value:val, name: i});
        });
        $.each(levels, function(i,val) {
            showLevels.push({value:val, name: i});
        });
        option1 = {
            title : {
                text: '电网变电站故障部位统计',
                //subtext: '纯属虚构',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                x : 'center',
                y : 'bottom',
                data:Object.keys(device_types)
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            series : [
                {
                    name:'面积模式',
                    type:'pie',
                    radius : [30, 110],
                    center : ['60%', '50%'],
                    roseType : 'area',
                    data: showDevicetTypes
                }
            ]
        };
        option2 = {
            title : {
                text: '电网变电站故障部位统计',
                //subtext: '纯属虚构',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                x : 'center',
                y : 'bottom',
                data:Object.keys(levels)
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            series : [
                {
                    name:'面积模式',
                    type:'pie',
                    radius : [30, 110],
                    center : ['65%', '50%'],
                    roseType : 'area',
                    data: showLevels
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart1.setOption(option1);
        myChart2.setOption(option2);
    }
}