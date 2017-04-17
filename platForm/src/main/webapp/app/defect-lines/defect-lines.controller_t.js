/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('DefectLineController_t', DefectLineController_t);

DefectLineController_t.$inject = ['$scope', '$http', '$stateParams', 'DateUtils', '$filter'];

function DefectLineController_t($scope, $http, $stateParams, DateUtils, $filter) {

    var localLocale = moment();
    localLocale.locale('zh-cn');
    $scope.searchDefectLineDateStart = DateUtils.convertDateTimeFromServer(moment());
    $scope.searchDefectLineDateStart = $filter('date')($scope.searchDefectLineDateStart, 'yyyy-MM-dd HH:MM');
    $scope.searchDefectLineDateEnd = DateUtils.convertDateTimeFromServer(moment());
    $scope.searchDefectLineDateEnd = $filter('date')($scope.searchDefectLineDateEnd, 'yyyy-MM-dd HH:MM');

    console.log($scope.searchDefectLineDateStart);
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.searchDefectLine = $stateParams.searchDefectLine;
    $scope.searchType = $stateParams.searchType;
    /* $scope.$watch('searchDefectLine', function() {
         console.log($scope.searchDefectLine);
         $scope.getDefectLines($scope.searchDefectLine);
     });*/
    $scope.DefectLinesPaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function() {
            if ($scope.defectLineData != null) {
                var start = ($scope.DefectLinesPaginationConf.currentPage - 1) * $scope.DefectLinesPaginationConf.itemsPerPage;
                var end = $scope.DefectLinesPaginationConf.currentPage * $scope.DefectLinesPaginationConf.itemsPerPage;
                $scope.defectLineDataShow = $scope.defectLineData.slice(start, end);
            }
        }
    };
    $scope.getDefectLines = function(searchDefectLine) {
        $http.jsonp(host + 'defect-lines?callback=JSON_CALLBACK')
            .success(function(data) {
                $scope.defectLineData = data;
                $scope.DefectLinesPaginationConf.totalItems = $scope.defectLineData.length;
                $scope.DefectLinesPaginationConf.onChange();
            }).error(function(msg) {
                console.log(msg);
            });
    };
    $scope.search = function(searchDefectLine, searchType) {
        var url = host + 'defect-lines?callback=JSON_CALLBACK';
        if ($scope.searchDefectLineDateEnd == $scope.searchDefectLineDateStart) {
            if ($scope.searchType == "设备ID") {
                url += '&searchDefectLine=' + $scope.searchDefectLine;
            }
            if ($scope.searchType == "缺陷性质") {
                url += '&searchDefectLineLevel=' + $scope.searchDefectLine;
            }
            $http.jsonp(url)
                .success(function(data) {
                    $scope.defectLineData = data;
                    $scope.DefectLinesPaginationConf.totalItems = $scope.defectLineData.length;
                    $scope.DefectLinesPaginationConf.onChange();
                }).error(function(msg) {
                console.log(msg);
            });
        } else {
            url += '&searchDefectLineDateStart=' + $scope.searchDefectLineDateStart +
                    '&searchDefectLineDateEnd=' + $scope.searchDefectLineDateEnd;
            console.log(url);
            $http.jsonp(url)
                .success(function(data) {
                    $scope.defectLineData = data;
                    $scope.DefectLinesPaginationConf.totalItems = $scope.defectLineData.length;
                    $scope.DefectLinesPaginationConf.onChange();
                }).error(function(msg) {
                console.log(msg);
            });
        }

    };
     
    $scope.onTimeSetStart = function (newDate, oldDate) {
        $scope.searchDefectLineDateStart = newDate;
        $scope.searchDefectLineDateStart = $filter('date')($scope.searchDefectLineDateStart, 'yyyy-MM-dd HH:MM');
        console.log($scope.searchDefectLineDateStart);

    }
    $scope.onTimeSetEnd = function (newDate, oldDate) {
        $scope.searchDefectLineDateEnd = newDate;
        $scope.searchDefectLineDateEnd = $filter('date')($scope.searchDefectLineDateEnd, 'yyyy-MM-dd HH:MM');
        console.log($scope.searchDefectLineDateEnd);

    }
}
