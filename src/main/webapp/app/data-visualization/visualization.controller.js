/**
 * Created by Muki on 2017/10/7.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('VisualizationController', VisualizationController);

VisualizationController.$inject = ['$scope', '$http', '$state'];

function VisualizationController($scope, $http, $state) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.lineInfo = {};
    var t;
    var t2;

    var pieChart1 = echarts.init(document.getElementById('dPie1'));
    var pieChart2 = echarts.init(document.getElementById('dPie2'));
    var defectLineData;
    var url = host + 'lines?callback=JSON_CALLBACK';
    $http.jsonp(url).success(function (data) {
        var lines = data;
        console.log(data);
        //地图
        // var geoCoordMap = {
        //     '站点1': [118.3118,35.2936],  //临沂  00118000000100
        //     '站点2': [119.2786,35.5023],   //日照 00119006965550
        //     '站点3': [117.0264,36.0516],   //泰安 00118000000090
        //     '站点4': [117.1582,36.8701],   //济南  1500000044
        //     '站点5': [116.8286,35.3375],   //济宁  1500229815
        //     '站点6': [118.0371,36.6064],   //淄博  1500000007
        //     '站点7': [115.6201,35.2057],    //菏泽  1900782119
        //     '站点8': [119.0918,36.524],    //潍坊  00118000000170
        //     '站点9': [120.7397,37.5128],   //烟台   00119006942520
        //     // '站点10': [115.6201,35.2057],   //菏泽
        //     '站点11': [120.4651,36.3373]    //青岛  00119007043000
        //
        // };
        var mapChart = echarts.init(document.getElementById('main'));
        var geoCoordMap = {
            '00118000000100': [118.3118, 35.2936],  //临沂  00118000000100
            '00119006965550': [119.2786, 35.5023],   //日照 00119006965550
            '00118000000090': [117.0264, 36.0516],   //泰安 00118000000090
            '1500000044': [117.1582, 36.8701],   //济南  1500000044
            '1500229815': [116.8286, 35.3375],   //济宁  1500229815
            '1500000007': [118.0371, 36.6064],   //淄博  1500000007
            '1900782119': [115.6201, 35.2057],    //菏泽  1900782119
            '00118000000170': [119.0918, 36.524],    //潍坊  00118000000170
            '00119006942520': [120.7397, 37.5128],   //烟台   00119006942520
            // '站点10': [115.6201,35.2057],   //菏泽
            '00119007043000': [120.4651, 36.3373]    //青岛  00119007043000

        };
        var yellowDataLine = [];
        var blueDataLine = [];
        var greenDataLine = [];
        var redDataLine = [];
        var purpleDataLine = [];
        var crimsonDataLine = [];

        angular.forEach(lines, function (value, key) {
            if (value.color == "黄色") {
                var lineName = [];
                lineName.push({
                    name: value.station_start
                });
                lineName.push({
                    name: value.station_end
                });
                lineName.push({
                    key: key
                });
                yellowDataLine.push(lineName);
            }
            else if (value.color == "蓝色") {
                var lineName = [];
                lineName.push({
                    name: value.station_start
                });
                lineName.push({
                    name: value.station_end
                });
                lineName.push({
                    key: key
                });
                blueDataLine.push(lineName);
            }
            else if (value.color == "绿色") {
                var lineName = [];
                lineName.push({
                    name: value.station_start
                });
                lineName.push({
                    name: value.station_end
                });
                lineName.push({
                    key: key
                });
                greenDataLine.push(lineName);
                console.log(greenDataLine);
            }
            else if (value.color == "红色") {
                var lineName = [];
                lineName.push({
                    name: value.station_start
                });
                lineName.push({
                    name: value.station_end
                });
                lineName.push({
                    key: key
                });
                redDataLine.push(lineName);
            }
            else if (value.color == "深红色") {
                var lineName = [];
                lineName.push({
                    name: value.station_start
                });
                lineName.push({
                    name: value.station_end
                });
                lineName.push({
                    key: key
                });
                crimsonDataLine.push(lineName);
            }
            else if (value.color == "紫色") {
                var lineName = [];
                lineName.push({
                    name: value.station_start
                });
                lineName.push({
                    name: value.station_end
                });
                lineName.push({
                    key: key
                });
                purpleDataLine.push(lineName);
            }
        });

        var convertData = function (data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var dataItem = data[i];
                var fromCoord = geoCoordMap[dataItem[0].name];
                var toCoord = geoCoordMap[dataItem[1].name];
                if (fromCoord && toCoord) {
                    res.push({
                        fromName: dataItem[0].name,
                        toName: dataItem[1].name,
                        coords: [fromCoord, toCoord],
                        keyLine: dataItem[2].key,
                        vmax: lines[dataItem[2].key].v_level_design,
                        imax: lines[dataItem[2].key].i_max_allow,
                        importance: lines[dataItem[2].key].importance,
                        vlevel: lines[dataItem[2].key].v_level

                    });
                }
            }
            return res;
        };
        var convertGeo = function (data) {
            var array = [];
            for (var i = 0; i < data.length; i++) {
                var dataItem = data[i];
                array.push({
                    name: dataItem.station_start,
                    value: geoCoordMap[dataItem.station_start]

                });
                if (dataItem.station_start != dataItem.station_end) {
                    array.push({
                        name: dataItem.station_end,
                        value: geoCoordMap[dataItem.station_end]
                    });
                }
            }
            return array;
        };


        var color = ['yellow', 'green', 'blue', 'purple', 'red', 'crimson'];
        var mapseries = [];
        [['黄色的线路色标', yellowDataLine], ['绿色的线路色标', greenDataLine], ['蓝色的线路色标', blueDataLine], ['紫色的线路色标', purpleDataLine], ['红色的线路色标', redDataLine],
            ['深红色的线路色标', crimsonDataLine]].forEach(
            function (item, i) {
                mapseries.push({
                        name: item[0],
                        type: 'lines',
                        zlevel: 1,
                        effect: {
                            show: true,
                            period: 6,
                            trailLength: 0.7,
                            color: '#fff',
                            symbolSize: 3
                        },
                        lineStyle: {
                            normal: {
                                color: color[i],
                                width: 0,
                                curveness: 0.1
                            }
                        },
                        markLine: {
                            symbol: 'circle'
                        },
                        animationDuration: 2000,

                        data: convertData(item[1])
                    },
                    {
                        name: item[0],
                        type: 'lines',
                        zlevel: 2,
                        effect: {
                            show: true,
                            period: 6,
                            trailLength: 0,
                            symbol: 'arrow',
                            symbolSize: 7
                        },
                        lineStyle: {
                            normal: {
                                color: color[i],
                                width: 2,
                                opacity: 0.4,
                                curveness: 0.1
                            }
                        },
                        animationDuration: 2000,
                        data: convertData(item[1])

                    });
            });
        mapseries.push({
            type: 'scatter',
            coordinateSystem: 'geo',
            symbol: 'image://img/Power-2.png',
            symbolSize: 20,
            data: convertGeo(lines)
        });
        var schema = [
            {name: '最大电流'},
            {name: '最大电压'},
            {name: 'vlevel'},
            {name: 'importance'}
        ];
        var mapOption = {
            tooltip: {
                padding: 10,
                backgroundColor: '#222',
                borderColor: '#777',
                borderWidth: 1,
                formatter: function (params) {

                    if (params.componentSubType == "lines") {
                        return '<div style="border-bottom: 1px solid rgba(255,255,255,0.3); font-size: 15px;padding-bottom: 7px;margin-bottom: 7px">'
                            + params.seriesName + '<div>' + params.data.fromName + '-' + params.data.toName + '</div>' + '</div>'
                            + '<div style="font-size: 15px;">' + schema[0].name + ': ' + params.data.imax + '</div>'
                            + '<div style="font-size: 15px;">' + schema[1].name + ': ' + params.data.vmax + '</div>'
                            + '<div style="font-size: 15px;">' + schema[2].name + ': ' + params.data.vlevel + '</div>'
                            + '<div style="font-size: 15px;">' + schema[3].name + ': ' + params.data.importance + '</div>'
                            ;
                    } else if (params.componentSubType == "scatter") {
                        return '<div style="border-bottom: 1px solid rgba(255,255,255,0.3); font-size: 15px;padding-bottom: 7px;margin-bottom: 7px">'
                            + '站点名称: ' + params.name + '</div>'
                            + '<div style="font-size: 15px;">' + '站点位置: ' + '(' + params.data.value[0]
                            + ',' + params.data.value[1] + ')' + '</div>';
                    }

                }
            },
            legend: {
                orient: 'vertical',
                data: ['黄色的线路色标', '绿色的线路色标', '蓝色的线路色标', '紫色的线路色标', '红色的线路色标', '深红色的线路色标'],
                left: '75%',
                top: 'bottom',
                textStyle: {
                    color: '#2e2e2f'
                },
                selectedMode: 'multiple'
            },
            toolbox: {
                show: true,
                orient: 'vertical',
                left: 'right',
                top: 'center',
                feature: {
                    dataView: {readOnly: false},
                    restore: {},
                    saveAsImage: {}
                }
            },
            geo: {
                map: '山东',
                label: {
                    emphasis: {
                        show: false
                    }
                },
                itemStyle: {
                    normal: {
                        areaColor: '#f4f4d0',
                        borderColor: '#404a59'
                    },
                    emphasis: {
                        areaColor: '#f4f3ab'
                    }
                }

            },
            series: mapseries
        };

        function changeTableInfo(params) {
            $scope.lineInfo = lines[params.data.keyLine];
            $("#info").html(
                "<h5>线路名称: " + $scope.lineInfo.line_name + "</h5>" +
                "<h5>线路ID: " + $scope.lineInfo.line_id + "</h5>" +
                "<h5>设备ID: " + $scope.lineInfo.device_id + "</h5>" +
                "<h5>城市: " + $scope.lineInfo.city + "</h5>" +
                "<h5>设备编码: " + $scope.lineInfo.device_code + "</h5>" +
                "<h5>设备铭牌运行库ID: " + $scope.lineInfo.device_library_id + "</h5>" +
                "<h5>起点电站: " + $scope.lineInfo.station_start + "</h5>" +
                "<h5>终点电站: " + $scope.lineInfo.station_end + "</h5>" +
                "<h5>起点位置: " + $scope.lineInfo.location_start + "</h5>" +
                "<h5>终点位置: " + $scope.lineInfo.location_end + "</h5>" +
                "<h5>投运日期: " + $scope.lineInfo.date_commissioning + "</h5>" +
                "<h5>线路总长度: " + $scope.lineInfo.length_total + "</h5>" +
                "<h5>架空线路长度: " + $scope.lineInfo.length_overhead + "</h5>" +
                "<h5>电缆线路长度: " + $scope.lineInfo.length_cable + "</h5>" +
                "<h5>线路色标: " + $scope.lineInfo.color + "</h5>" +
                "<h5>重要程度: " + $scope.lineInfo.importance + "</h5>" +
                "<h5>设计电压等级: " + $scope.lineInfo.v_level + "</h5>" +
                "<h5>允许的最大电流:" + $scope.lineInfo.i_max_allow + "</h5>"
            );
            $("#navLine").css("border-top", "1px solid");
            $("#navLine").css("padding", "5px");
        }

        function changeDefectInfo(params) {
            console.log("get the device_id of clicked line:" + lines[params.data.keyLine].device_id);
            statisticsDefectLines(defectLineData, lines[params.data.keyLine].device_id);
            console.log('get the information of defect-line pie1 chart of clicked line');
            console.log(pieChart1.getOption());
        }

        function changeInfo(params) {
            changeTableInfo(params);
            changeDefectInfo(params);
        }

        mapChart.setOption(mapOption);

        //饼图
        var voltagePieChart = echarts.init(document.getElementById('vPie'));
        var firstLevel = [];
        var secondLevel = [];
        var thirdLevel = [];
        angular.forEach(lines, function (value, i) {
            if (value.v_level == "交流110kV") {
                firstLevel.push({
                    value: value.line_id
                });
            }
            if (value.v_level == "交流220kV") {
                secondLevel.push({
                    value: value.line_id
                });
            }
            if (value.v_level == "交流500kV") {
                thirdLevel.push({
                    value: value.line_id
                });
            }
        });
        var voltagePieOption = {
            title: {
                text: '电压等级分布',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                top: '10%',
                data: ['交流110kV', '交流220kV', '交流500kV']
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                        {value: firstLevel.length, name: '交流110kV'},
                        {value: secondLevel.length, name: '交流220kV'},
                        {value: thirdLevel.length, name: '交流500kV'}
                    ],
                    label: {
                        normal: {
                            show: false,
                            position: 'inner'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '10',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        voltagePieChart.setOption(voltagePieOption);
        mapChart.on('click', function (params) {
            clearTimeout(t);
            console.log(params);
            if (params.componentType == "series") {
                if (params.seriesType == "lines") {
                    function set() {
                        voltagePieChart.setOption({
                            series: [{
                                data: [
                                    {value: firstLevel.length, name: '交流110kV', itemStyle: {normal: {opacity: 1}}},
                                    {value: secondLevel.length, name: '交流220kV', itemStyle: {normal: {opacity: 1}}},
                                    {value: thirdLevel.length, name: '交流500kV', itemStyle: {normal: {opacity: 1}}}
                                ]
                            }]
                        });
                        console.log(voltagePieChart.getOption());
                    }

                    changeInfo(params);
                    if (params.data.vlevel == "交流110kV") {
                        voltagePieChart.setOption({
                            series: [{
                                data: [
                                    {value: firstLevel.length, name: '交流110kV', itemStyle: {normal: {opacity: 0.5}}},
                                    {value: secondLevel.length, name: '交流220kV'},
                                    {value: thirdLevel.length, name: '交流500kV'}
                                ]
                            }]
                        });
                        t = setTimeout(function () {
                            set();
                        }, 2000);
                    }
                    else if (params.data.vlevel == "交流220kV") {
                        voltagePieChart.setOption({
                            series: [{
                                data: [
                                    {value: firstLevel.length, name: '交流110kV'},
                                    {value: secondLevel.length, name: '交流220kV', itemStyle: {normal: {opacity: 0.5}}},
                                    {value: thirdLevel.length, name: '交流500kV'}
                                ]
                            }]
                        });
                        t = setTimeout(function () {
                            set();
                        }, 2000);
                    }
                    else if (params.data.vlevel == "交流500kV") {
                        voltagePieChart.setOption({
                            series: [{
                                data: [
                                    {value: firstLevel.length, name: '交流110kV'},
                                    {value: secondLevel.length, name: '交流220kV'},
                                    {value: thirdLevel.length, name: '交流500kV', itemStyle: {normal: {opacity: 0.5}}}
                                ]
                            }]
                        });
                        t = setTimeout(function () {
                            set();
                        }, 2000);
                    }
                }
            }
        });
        voltagePieChart.on('click', function (params) {
            clearTimeout(t2);

            function secSet(series) {
                mapChart.setOption({
                    series: series
                });
            }

            var secOption = mapChart.getOption();
            mapOption = angular.copy(secOption);
            var secSeries = secOption.series;
            if (params.componentType == "series") {
                if (params.seriesType == "pie") {
                    if (params.name == "交流110kV") {
                        for (var i = 0; i < secSeries.length - 1; i++) {
                            var secData = secSeries[i].data;
                            for (var j = 0; j < secData.length; j++) {
                                if (secData[j].vlevel == "交流110kV") {
                                    mapOption.series[i].data[j].lineStyle = {};
                                    mapOption.series[i].data[j].lineStyle.normal = {};
                                    mapOption.series[i].data[j].lineStyle.normal.width = 3;
                                }
                            }
                        }
                        mapChart.setOption(mapOption);
                        console.log(mapChart.getOption());
                        t2 = setTimeout(function () {
                            secSet(secSeries);
                        }, 2000);

                    } else if (params.name == "交流220kV") {
                        for (var i = 0; i < secSeries.length - 1; i++) {
                            var secData = secSeries[i].data;
                            for (var j = 0; j < secData.length; j++) {
                                if (secData[j].vlevel == "交流220kV") {
                                    mapOption.series[i].data[j].lineStyle = {};
                                    mapOption.series[i].data[j].lineStyle.normal = {};
                                    mapOption.series[i].data[j].lineStyle.normal.width = 3;
                                }
                            }
                        }
                        mapChart.setOption(mapOption);
                        console.log(mapChart.getOption());
                        t2 = setTimeout(function () {
                            secSet(secSeries);
                        }, 2000);

                    } else if (params.name == "交流500kV") {
                        for (var i = 0; i < secSeries.length - 1; i++) {
                            var secData = secSeries[i].data;
                            for (var j = 0; j < secData.length; j++) {
                                if (secData[j].vlevel == "交流500kV") {
                                    mapOption.series[i].data[j].lineStyle = {};
                                    mapOption.series[i].data[j].lineStyle.normal = {};
                                    mapOption.series[i].data[j].lineStyle.normal.width = 3;
                                }
                            }
                        }
                        mapChart.setOption(mapOption);
                        console.log(mapChart.getOption());
                        t2 = setTimeout(function () {
                            secSet(secSeries);
                        }, 2000);

                    }
                }
            }
        });

    }).error(function (msg) {
        console.log(msg);

    });

    var urlGas = host + 'gas?callback=JSON_CALLBACK';
    $http.jsonp(urlGas).success(function (data) {
        var gas = data;
        console.log(gas);
        var deviceId1 = "1800002460";
        var deviceId2 = "1800002459";
        var deviceId3 = "1800002078";


        var device1 = [];
        var device2 = [];
        var device3 = [];
        angular.forEach(gas, function (value, key) {
            if (value.device_id == deviceId1) {
                device1.push({
                    time: value.time,
                    co2Value: value.co2,
                    coValue: value.co,
                    h2Value: value.h2,
                    ch4Value: value.ch4
                });
            } else if (value.device_id == deviceId2) {
                device2.push({
                    time: value.time,
                    co2Value: value.co2,
                    coValue: value.co,
                    h2Value: value.h2,
                    ch4Value: value.ch4
                });
            } else if (value.device_id == deviceId3) {
                device3.push({
                    time: value.time,
                    co2Value: value.co2,
                    coValue: value.co,
                    h2Value: value.h2,
                    ch4Value: value.ch4
                });
            }
        });


        device1.sort(function (a, b) {
            return Number(a.time.substring(0, 2)) - Number(b.time.substring(0, 2));
        });

        device2.sort(function (a, b) {
            return Number(a.time.substring(0, 2)) - Number(b.time.substring(0, 2));
        });

        device3.sort(function (a, b) {
            return Number(a.time.substring(0, 2)) - Number(b.time.substring(0, 2));
        });

        var date = [];

        var device1Value = [];
        device1Value[0] = [];
        device1Value[1] = [];
        device1Value[2] = [];
        device1Value[3] = [];

        angular.forEach(device1, function (item, key) {

            device1Value[0].push(item.co2Value);
            device1Value[1].push(item.ch4Value);
            device1Value[2].push(item.h2Value);
            device1Value[3].push(item.coValue);
            date.push(item.time.substring(0, 2));
        });

        var device2Value = [];
        device2Value[0] = [];
        device2Value[1] = [];
        device2Value[2] = [];
        device2Value[3] = [];
        angular.forEach(device2, function (item, key) {
            device2Value[0].push(item.co2Value);
            device2Value[1].push(item.ch4Value);
            device2Value[2].push(item.h2Value);
            device2Value[3].push(item.coValue);
        });

        var device3Value = [];
        device3Value[0] = [];
        device3Value[1] = [];
        device3Value[2] = [];
        device3Value[3] = [];
        angular.forEach(device3, function (item, key) {
            if (key < 29) {
                device3Value[0].push(item.co2Value);
                device3Value[1].push(item.ch4Value);
                device3Value[2].push(item.h2Value);
                device3Value[3].push(item.coValue);
            }

        });


        function setGasSeries() {
            var gasSeries = [];
            console.log(myChart1.getOption().legend.selected);
        }

        var myChart1 = echarts.init(document.getElementById('gas'));
        var option1 = {
            title: {
                text: '气体变化',
                textStyle: {
                    color: '#2e2e2f'
                },
                x: 'center',
                padding: [5, 50, 5, 50]

            },
            tooltip: {
                trigger: 'axis'
            },
            legend: [{
                data: ['设备一', '设备二', '设备三'],
                selected: {
                    '设备一': true,
                    '设备二': false,
                    '设备三': false
                },
                selectedMode: 'single',
                top: 'bottom',
                textStyle: {
                    color: '#2e2e2f'
                }
            }, {
                data: ['co2', 'co', 'h2', 'ch4'],
                top: '10%',
                textStyle: {
                    color: '#2e2e2f'
                }
            }],
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: date,
                axisLine: {
                    lineStyle: {
                        color: '#2e2e2f'
                    }
                }

            },
            yAxis: {
                type: 'value',
                axisLine: {
                    lineStyle: {
                        color: '#2e2e2f'
                    }
                },

                axisLabel: {
                    margin: -4
                }
            },
            series: [
                {
                    name: '设备一',
                    type: 'line',
                    data: []
                },
                {
                    name: '设备二',
                    type: 'line',
                    data: []
                },
                {
                    name: '设备三',
                    type: 'line',
                    data: []
                },
                {
                    name: 'co2',
                    type: 'line',
                    data: device1Value[0]
                },
                {
                    name: 'ch4',
                    type: 'line',
                    data: device1Value[1]
                },
                {
                    name: 'h2',
                    type: 'line',
                    data: device1Value[2]
                },
                {
                    name: 'co',
                    type: 'line',
                    data: device1Value[3]
                }
            ]

        };
        myChart1.on('legendselectchanged', function (params) {
            console.log(params);
            if (params.name == '设备二') {
                myChart1.setOption({
                    series: [
                        {
                            name: '设备一',
                            type: 'line',
                            data: []
                        },
                        {
                            name: '设备二',
                            type: 'line',
                            data: []
                        },
                        {
                            name: '设备三',
                            type: 'line',
                            data: []
                        },
                        {
                            name: 'co2',
                            type: 'line',
                            data: device2Value[0]
                        },
                        {
                            name: 'ch4',
                            type: 'line',
                            data: device2Value[1]
                        },
                        {
                            name: 'h2',
                            type: 'line',
                            data: device2Value[2]
                        },
                        {
                            name: 'co',
                            type: 'line',
                            data: device2Value[3]
                        }
                    ]
                });
            }
            else if (params.name == '设备三') {
                myChart1.setOption({
                    series: [
                        {
                            name: '设备一',
                            type: 'line',
                            data: []
                        },
                        {
                            name: '设备二',
                            type: 'line',
                            data: []
                        },
                        {
                            name: '设备三',
                            type: 'line',
                            data: []
                        },
                        {
                            name: 'co2',
                            type: 'line',
                            data: device3Value[0]
                        },
                        {
                            name: 'ch4',
                            type: 'line',
                            data: device3Value[1]
                        },
                        {
                            name: 'h2',
                            type: 'line',
                            data: device3Value[2]
                        },
                        {
                            name: 'co',
                            type: 'line',
                            data: device3Value[3]
                        }
                    ]
                });
            }
            else if (params.name == '设备一') {
                myChart1.setOption({
                    series: [
                        {
                            name: '设备一',
                            type: 'line',
                            data: []
                        },
                        {
                            name: '设备二',
                            type: 'line',
                            data: []
                        },
                        {
                            name: '设备三',
                            type: 'line',
                            data: []
                        },
                        {
                            name: 'co2',
                            type: 'line',
                            data: device1Value[0]
                        },
                        {
                            name: 'ch4',
                            type: 'line',
                            data: device1Value[1]
                        },
                        {
                            name: 'h2',
                            type: 'line',
                            data: device1Value[2]
                        },
                        {
                            name: 'co',
                            type: 'line',
                            data: device1Value[3]
                        }
                    ]
                });
            }

        });
        myChart1.setOption(option1);
    }).error(function (msg) {
        console.log(msg);
    });


    var urlDefectLine = host + 'defect-lines?callback=JSON_CALLBACK';
    console.log(urlDefectLine);
    $http.jsonp(urlDefectLine).success(function (data) {
        console.log('get date from defect-lines');
        /*$scope.defectLineData = data;*/
        defectLineData = data;
        statisticsDefectLines(data, 0);
    }).error(function (msg) {
        console.log(msg);
    });

    function statisticsDefectLines(data, id) {
        console.log('get the line id in statisticsDefectLines:' + id);
        var levels = {};
        var places = {};
        //刚打开页面时显示是所有缺陷线路信息的饼图统计
        if (id === 0) {
            $.each(data, function (i, obj) {
                if (obj.level in levels) {
                    levels[obj.level] += 1
                } else {
                    levels[obj.level] = 1
                }
                if (obj.place in places) {
                    places[obj.place] += 1
                } else {
                    places[obj.place] = 1
                }
            });
        }
        else {
            $.each(data, function (i, obj) {
                //console.log('obj:'+obj.device_id);
                if ((obj.level in levels) && (obj.device_id === id)) {
                    levels[obj.level] += 1
                } else if (obj.device_id === id) {
                    levels[obj.level] = 1
                }
                if ((obj.place in places) && (obj.device_id === id)) {
                    places[obj.place] += 1
                } else if (obj.device_id === id) {
                    places[obj.place] = 1
                }
            });
        }
        places['其他'] = 0;

        $.each(places, function (i, val) {
            if (places[i] < 5) {
                delete places[i];
                places['其他'] += val;
            }
        });
        //console.log('places:'+places);
        console.log('places:' + Object.keys(places));
        //console.log('levels'+levels);
        console.log('levels' + Object.keys(levels));
        showDefectLinesGraph(places, levels);
    }

    function showDefectLinesGraph(places, levels) {
        var showPlaces = [];
        var showLevels = [];
        $.each(places, function (i, val) {
            showPlaces.push({value: val, name: i});
        });
        $.each(levels, function (i, val) {
            showLevels.push({value: val, name: i});
        });
        console.log('begin to draw pie graph of defect-lines');
        console.log(showPlaces);
        var pieOption1 = {
            title: {
                text: '电网线路故障部位统计',
                //subtext: '纯属虚构',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            /*legend: {
                x : 'center',
                y : 'bottom',
                data:Object.keys(places)
            },*/
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: true,
            series: [
                {
                    name: '面积模式',
                    type: 'pie',
                    radius: [30, 110],
                    center: ['55%', '50%'],
                    roseType: 'area',
                    data: showPlaces
                }
            ]
        };
        var pieOption2 = {
            title: {
                text: '电网线路故障等级统计',
                //subtext: '纯属虚构',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                x: 'center',
                y: 'bottom',
                data: Object.keys(levels)
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: true,
            series: [
                {
                    name: '面积模式',
                    type: 'pie',
                    radius: [30, 110],
                    center: ['55%', '50%'],
                    roseType: 'area',
                    data: showLevels
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        pieChart1.setOption(pieOption1);
        pieChart2.setOption(pieOption2);
    }

    var urlLineStatus = host + 'line-statuses?callback=JSON_CALLBACK';
    $http.jsonp(urlLineStatus).success(function (data) {
        statisticsLineStatus(data);
    }).error(function (msg) {
        console.log(msg);
    });

    function statisticsLineStatus(data) {
        var ia_start = Array();
        var ia_end = Array();
        var ib_start = Array();
        var ib_end = Array();
        var ic_start = Array();
        var ic_end = Array();
        var time = Array();
        $.each(data, function (i, val) {
            ia_start.push(val.ia_start);
            ia_end.push(val.ia_end);
            ib_start.push(val.ib_start);
            ib_end.push(val.ib_end);
            ic_start.push(val.ic_start);
            ic_end.push(val.ic_end);
            time.push(val.time);
        });
        showLineStatusGraph(ia_start, ia_end, ib_start, ib_end, ic_start, ic_end, time);
    }

    function showLineStatusGraph(ia_start, ia_end, ib_start, ib_end, ic_start, ic_end, time) {
        var lineStatusChart = echarts.init(document.getElementById('line-status'));
        var lineStatusOption = {
            title: {
                text: '线路状态变化图'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['起点电站Ia值	', '终点电站Ia值', '起点电站Ib值', '终点电站Ib值', '起点电站Ic值', '终点电站Ic值']
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
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: time
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [

                {
                    name: '起点电站Ia值',
                    type: 'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: ia_start
                },
                {
                    name: '终点电站Ia值',
                    type: 'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: ia_end
                },
                {
                    name: '起点电站Ib值',
                    type: 'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: ib_start
                },
                {
                    name: '终点电站Ib值',
                    type: 'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: ib_end
                },
                {
                    name: '起点电站Ic值',
                    type: 'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: ic_start
                },
                {
                    name: '终点电站Ic值',
                    type: 'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: ic_end
                }
            ]
        };
        lineStatusChart.setOption(lineStatusOption);
    }
}
