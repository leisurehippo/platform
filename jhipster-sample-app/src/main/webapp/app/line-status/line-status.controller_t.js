/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('LineStatusController_t',LineStatusController_t);

LineStatusController_t.$inject = ['$scope', '$http'];

function LineStatusController_t($scope, $http) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.LineStatusPaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            if($scope.lineStatuses != null) {
                var start = ($scope.LineStatusPaginationConf.currentPage - 1 ) * $scope.LineStatusPaginationConf.itemsPerPage;
                var end = $scope.LineStatusPaginationConf.currentPage * $scope.LineStatusPaginationConf.itemsPerPage;
                $scope.lineStatusShow = $scope.lineStatuses.slice(start, end);
            }
        }
    };
    $scope.getLineStatus = function(){
        $http.jsonp(host + 'line-statuses?callback=JSON_CALLBACK')
            .success(function(data)
            {
                $scope.lineStatuses = data;
                $scope.LineStatusPaginationConf.totalItems = $scope.lineStatuses.length;
                $scope.LineStatusPaginationConf.onChange();
            }).error(function(msg){
            console.log(msg);
        });
    };
}
