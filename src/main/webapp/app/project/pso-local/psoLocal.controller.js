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
    vm.hasParams = true;
    vm.alName = null;
    vm.fileName = null;
    vm.params = null;
    vm.result = [];
    function run() {
        var hasParams;
        if(vm.hasParams) {
            hasParams = 1;
        }
        runLocal.get({AlgorithmName:vm.alName, hasParams:hasParams,Params:vm.fileName+"+"+vm.params}, function success(result) {
            console.log(result);
            vm.result = result;

        },function () {

        });
    }


}
