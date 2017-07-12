/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelController',DataLabelController);

DataLabelController.$inject = ['$scope', '$http', '$state', 'dataLabelservice'];
function DataLabelController($scope, $http, $state, dataLabelservice) {
    var vm = this;
    vm.run = run;
    $scope.keywords=" ";
    $scope.selectdb=false;
    $scope.dbname=" ";
    $scope.selectsina=false;
    $scope.selecttime=false;
    $scope.timestart=" ";
    $scope.timeend=" ";
    $scope.writedb=" ";
    $scope.oldlabel=" ";
    $scope.newlabel=" ";
    $scope.selectoldlabel=false;

    function run() {

        dataLabelservice.get({keywords:$scope.keywords,selectdb:$scope.selectdb,dbname:$scope.dbname,selectsina:$scope.selectsina,selecttime:$scope.selecttime,timestart
        :$scope.timestart,timeend:$scope.timeend,selectoldlabel:$scope.selectoldlabel,oldlabel:$scope.oldlabel,newlabel:$scope.newlabel,writedb:$scope.writedb}, function success(result) {
            console.log(result);

            for(var item in datares.keyset)
            {
                    ;
            }



        },function () {

        });
    }




}
