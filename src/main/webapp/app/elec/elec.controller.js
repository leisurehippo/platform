angular
    .module('jhipsterSampleApplicationApp')
    .controller('ElecController',ElecController);

ElecController.$inject = ['$scope', '$state', 'projectName'];

function ElecController($scope, $state, projectName){
	var vm = this
	projectName.name = "elec"

	
	
}