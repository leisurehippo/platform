/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('DefectLineController', DefectLineController);

DefectLineController.$inject = ['$scope', '$http'];

function DefectLineController($scope, $http, $stateParams) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.getDefectLines = function(){
        var url = host + 'defect-lines?callback=JSON_CALLBACK' 
        $http.jsonp(url)
            .success(function(data)
            {
                /*$scope.defectLineData = data;*/
                statisticsDefectLines(data);
            }).error(function(msg){
            console.log(msg);
        });
    };

    function statisticsDefectLines(data) {
        var levels = {};
        var places = {};
        $.each(data, function(i, obj){
            if (obj.level in levels){
                levels[obj.level] += 1
            }else {
                levels[obj.level] = 1
            }
            if (obj.place in places){
                places[obj.place] += 1
            }else {
                places[obj.place] = 1
            }
        });
        places['其他'] = 0;

        $.each(places, function(i, val) {
            if (places[i] < 5) {
                delete places[i];
                places['其他'] += val;
            }
        });
        //console.log(places);
        //console.log(Object.keys(places));
        //console.log(levels);
        //console.log(Object.keys(levels));
        showDefectLinesGraph(places, levels);
    }
    function showDefectLinesGraph(places,levels){
        var myChart1 = echarts.init(document.getElementById('graph1'));
        var myChart2 = echarts.init(document.getElementById('graph2'));
        var showPlaces = [];
        var showLevels = [];
        $.each(places, function(i,val) {
            showPlaces.push({value:val, name: i});
        });
        $.each(levels, function(i,val) {
            showLevels.push({value:val, name: i});
        });
        //console.log(showPlaces);
        option1 = {
            title : {
                text: '电网线路故障部位统计',
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
                data:Object.keys(places)
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
                    center : ['55%', '50%'],
                    roseType : 'area',
                    data: showPlaces
                }
            ]
        };
        option2 = {
            title : {
                text: '电网线路故障部位统计',
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
                    center : ['55%', '50%'],
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