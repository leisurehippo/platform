/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('LineController',LineController);

LineController.$inject = ['$scope', '$http', '$state'];

function LineController($scope,$http, $state) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';
    $scope.$watch('searchLine',function(){
        $scope.getLines($scope.searchLine);
    });
    $scope.LinePaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            if($scope.lines != null) {
                var start = ($scope.LinePaginationConf.currentPage - 1 ) * $scope.LinePaginationConf.itemsPerPage;
                var end = $scope.LinePaginationConf.currentPage * $scope.LinePaginationConf.itemsPerPage;
                $scope.linesShow = $scope.lines.slice(start, end);
            }
        }
    };
    $scope.getLines = function(searchLine){
        var url = host + 'lines?callback=JSON_CALLBACK';
        if (searchLine != null) {
            url += '&LineId=' + searchLine;
        }
        $http.jsonp(url)
            .success(function(data)
            {
                $scope.lines = data;
                $scope.LinePaginationConf.totalItems = $scope.lines.length;
                $scope.LinePaginationConf.onChange();
            }).error(function(msg){
            console.log(msg);
        });
    };
    $scope.seeLineDefect = function ($event) {
        $state.go('defect-lines',{searchDefectLine: $event.target.innerHTML})
    };
    $scope.seeLineStatus = function (devide_id) {
        console.log($(this).val());
        $state.go('line-status',{searchDefectLine: $event.target.innerHTML})
    }
}