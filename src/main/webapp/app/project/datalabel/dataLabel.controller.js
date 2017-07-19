/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelController',DataLabelController);

DataLabelController.$inject = ['$scope', '$http', '$state', 'dataLabelservice','Submitservice'];
function DataLabelController($scope, $http, $state, dataLabelservice,Submitservice) {
    var vm = this;
    vm.run = run;
    vm.submit=submit;
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
            $scope.items=result.dataset;

        },function () {

        });
    }

    function submit(){
        var postdata=new Array();
        for(var i in $scope.items)
        {

            var item=$scope.items[i];
            if(document.getElementById(item.since_id).checked)
            {
                   postdata.push(item.since_id) ;
            }
        }

        Submitservice.post({labelresult:postdata}, function success(result) {
         console.log(result);
         },function () {});

    }


}
