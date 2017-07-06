/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('PsoLocalController',PsoLocalController);

PsoLocalController.$inject = ['$scope', '$http', '$state', 'runLocal'];
function PsoLocalController($scope, $http, $state, runLocal) {
    var vm = this;
    vm.run = run;

    function run() {
        runLocal.get({AlgorithmName:"Apriori", hasParams:1,Params:"defect.csv+0.1+0.1"}, function success(result) {
            console.log(result);

        },function () {

        });
    }


}
