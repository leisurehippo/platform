/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('GasController_t', GasController_t);

GasController_t.$inject = ['$scope', '$http', '$stateParams'];

function GasController_t($scope, $http, $stateParams) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.searchGas = $stateParams.searchGas;
    $scope.searchType = $stateParams.searchType;

    $scope.GasPaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function() {
            if ($scope.gases != null) {
                var start = ($scope.GasPaginationConf.currentPage - 1) * $scope.GasPaginationConf.itemsPerPage;
                var end = $scope.GasPaginationConf.currentPage * $scope.GasPaginationConf.itemsPerPage;
                $scope.gasesShow = $scope.gases.slice(start, end);
            }
        }
    };
    $scope.getGas = function() {
        $http.jsonp(host + 'gas?callback=JSON_CALLBACK')
            .success(function(data) {
                $scope.gases = data;
                $scope.GasPaginationConf.totalItems = $scope.gases.length;
                $scope.GasPaginationConf.onChange();
            }).error(function(msg) {
                console.log(msg);
            });
    };

    $scope.search = function() {
        var url = host + 'gas?callback=JSON_CALLBACK'
        if ($scope.searchType == "设备ID")
            url += '&gasDevId=' + $scope.searchGas;
        $http.jsonp(url)
            .success(function(data) {
                $scope.gases = data;
                $scope.GasPaginationConf.totalItems = $scope.gases.length;
                $scope.GasPaginationConf.onChange();
            }).error(function(msg) {
                console.log(msg);
            });
    };
}
