/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('app')
    .controller('TransformerController',TransformerController);

TransformerController.$inject = ['$scope', '$http', '$state'];
function TransformerController($scope,$http,$stateParams) {
    var host = 'http://10.109.247.121/electric_backend/public/electric/';

    $scope.seeTransformerDefect = function ($event) {
        $state.go('defect-transformers',{searchDefectTransformer: $event.target.innerHTML})
    };
    $scope.$watch('searchTransformer',function(){
        console.log($scope.searchTransformer);
        $scope.getTransformers($scope.searchTransformer);
    });
    $scope.TransformerPaginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 15,
        pagesLength: 0,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            if($scope.transformers != null) {
                var start = ($scope.TransformerPaginationConf.currentPage - 1 ) * $scope.TransformerPaginationConf.itemsPerPage;
                var end = $scope.TransformerPaginationConf.currentPage * $scope.TransformerPaginationConf.itemsPerPage;
                $scope.transformersShow = $scope.transformers.slice(start, end);
            }
        }
    };
    $scope.getTransformers = function(searchTransformer){
        var url = host + 'transformers?callback=JSON_CALLBACK';
        if (searchTransformer != null) {
            url += '&transformerId=' + searchTransformer;
        }
        $http.jsonp(url)
            .success(function(data)
            {
                $scope.transformers = data;
                $scope.TransformerPaginationConf.totalItems = $scope.transformers.length;
                $scope.TransformerPaginationConf.onChange();
            }).error(function(msg){
            console.log(msg);
        });
    };
}