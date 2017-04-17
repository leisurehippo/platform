/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('DefectTransformerController_t', DefectTransformerController_t)
    .filter('replaceCode', replaceCode)

DefectTransformerController_t.$inject = ['$scope', '$http', '$stateParams', 'replaceCodeFilter','DateUtils', '$filter'];

function DefectTransformerController_t($scope, $http, $stateParams, replaceCodeFilter, DateUtils, $filter) {
    var localLocale = moment();
    localLocale.locale('zh-cn');
    $scope.searchDefectTransDateStart = DateUtils.convertDateTimeFromServer(moment());
    $scope.searchDefectTransDateStart = $filter('date')($scope.searchDefectTransDateStart, 'yyyy-MM-dd HH:MM');
    $scope.searchDefectTransDateEnd = DateUtils.convertDateTimeFromServer(moment());
    $scope.searchDefectTransDateEnd = $filter('date')($scope.searchDefectTransDateEnd, 'yyyy-MM-dd HH:MM');


    console.log($scope.searchDefectTransDateStart);
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.searchDefectTransformer = $stateParams.searchDefectTransformer;
    $scope.searchType = $stateParams.searchType;

    /*$scope.$watch('searchDefectTransformer', function() {
        $scope.getDefectTransformers($scope.searchDefectTransformer);
    });*/
    $scope.DefectTransformersPaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function() {
            if ($scope.defectTransformers != null) {
                var start = ($scope.DefectTransformersPaginationConf.currentPage - 1) * $scope.DefectTransformersPaginationConf.itemsPerPage;
                var end = $scope.DefectTransformersPaginationConf.currentPage * $scope.DefectTransformersPaginationConf.itemsPerPage;
                $scope.defectTransformersShow = $scope.defectTransformers.slice(start, end);
            }
        }
    };
    $scope.getDefectTransformers = function(searchDefectTransformer) {
        $http.jsonp(host + 'defect-transformers?callback=JSON_CALLBACK')
            .success(function(data) {
                $scope.defectTransformers = data;
                $scope.DefectTransformersPaginationConf.totalItems = $scope.defectTransformers.length;
                $scope.DefectTransformersPaginationConf.onChange();
            }).error(function(msg) {
                console.log(msg);
            });
    };
    $scope.search = function(searchDefectTransformer, searchType) {
        var url = host + 'defect-transformers?callback=JSON_CALLBACK'
        if ($scope.searchDefectTransDateEnd == $scope.searchDefectTransDateStart) {
            if ($scope.searchType == "设备ID") {
                url += '&searchDefectTransformer=' + $scope.searchDefectTransformer;
            }
            if ($scope.searchType == "缺陷性质") {
                url += '&searchDefectTransformerLevel=' + $scope.searchDefectTransformer;
            }
            $http.jsonp(url)
                .success(function(data) {
                    $scope.defectTransformers = data;
                    $scope.DefectTransformersPaginationConf.totalItems = $scope.defectTransformers.length;
                    $scope.DefectTransformersPaginationConf.onChange();
                }).error(function(msg) {
                console.log(msg);
            });
        } else {
            url += '&searchDefectTransDateStart=' + $scope.searchDefectTransDateStart +
                '&searchDefectTransDateEnd=' + $scope.searchDefectTransDateEnd;
            console.log(url);
            $http.jsonp(url)
                .success(function(data) {
                    $scope.defectTransformers = data;
                    $scope.DefectTransformersPaginationConf.totalItems = $scope.defectTransformers.length;
                    $scope.DefectTransformersPaginationConf.onChange();
                }).error(function(msg) {
                console.log(msg);
            });
        }

    };
    $scope.onTimeSetStart = function(newDate, oldDate) {
        $scope.searchDefectTransDateStart = newDate;
        $scope.searchDefectTransDateStart = $filter('date')($scope.searchDefectTransDateStart, 'yyyy-MM-dd HH:MM');
        console.log($scope.searchDefectTransDateStart);

    };
    $scope.onTimeSetEnd = function(newDate, oldDate) {
        $scope.searchDefectTransDateEnd = newDate;
        $scope.searchDefectTransDateEnd = $filter('date')($scope.searchDefectTransDateEnd, 'yyyy-MM-dd HH:MM');
        console.log($scope.searchDefectTransDateEnd);
    };
}

function replaceCode() {
    return function(val) {
        if (!val) val = '';
        return val.replace(/[-*]/g, '');
    }
};
