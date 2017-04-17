/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('WeatherController_t', WeatherController_t);

WeatherController_t.$inject = ['$scope', '$http', '$stateParams'];

function WeatherController_t($scope, $http, $stateParams) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';
    $scope.searchWeather = $stateParams.searchWeather;
    $scope.searchType = $stateParams.searchType;

    $scope.WeatherPaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function() {
            if ($scope.weathers != null) {
                var start = ($scope.WeatherPaginationConf.currentPage - 1) * $scope.WeatherPaginationConf.itemsPerPage;
                var end = $scope.WeatherPaginationConf.currentPage * $scope.WeatherPaginationConf.itemsPerPage;
                $scope.weathersShow = $scope.weathers.slice(start, end);
            }
        }
    };
    $scope.getWeather = function() {
        $http.jsonp(host + 'weathers?callback=JSON_CALLBACK')
            .success(function(data) {
                $scope.weathers = data;
                $scope.WeatherPaginationConf.totalItems = $scope.weathers.length;
                $scope.WeatherPaginationConf.onChange();
            }).error(function(msg) {
                console.log(msg);
            });
    };

    $scope.search = function() {
     var url = host + 'weathers?callback=JSON_CALLBACK'
     if ($scope.searchType == "变电站")
         url += '&devStation=' + $scope.searchWeather;
     $http.jsonp(url)
         .success(function(data) {
             $scope.weathers = data;
             $scope.WeatherPaginationConf.totalItems = $scope.weathers.length;
             $scope.WeatherPaginationConf.onChange();
         }).error(function(msg) {
             console.log(msg);
         });
 };

}
