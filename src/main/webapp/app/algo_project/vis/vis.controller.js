angular
    .module('jhipsterSampleApplicationApp')
    .controller('VisualizeController',VisualizeController);

VisualizeController.$inject = ['$scope', '$http', '$window','projectName','GetAlgoTasks','FileDownload'];
function VisualizeController($scope, $http, $window, projectName,GetAlgoTasks,FileDownload) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn
	
	vm.tasks = GetAlgoTasks.get_tasks()
	vm.result_files = []
	vm.cur_task = null
	vm.cur_res_file = null
	
	
	$scope.$watch('vm.cur_task', function (oldValue,newValue) {	
		console.log(vm.cur_task)
		vm.cur_res_file = null		
		vm.result_files = GetAlgoTasks.get_task_files(vm.cur_task, "result", false);
    });
	
	$scope.$watch('vm.cur_res_file', function (oldValue,newValue) {	
		if(vm.cur_res_file == null) return
		download()
    });
	
		
    function download(){     
    	console.log("download");
  	  	if(vm.cur_task == null | vm.cur_res_file == null){
  	  		return
  	  	}
  	  	var params = {
  	  			project:vm.projectName, task:vm.cur_task, dataType:"result",
  				fileName:vm.cur_res_file, hdfs:false
  				}
    	$http.get('algo_task/download', {"params" : params}).success(function(result) { 
              blob = new Blob([result], { type: "application/octet-stream" }),
              url = $window.URL || $window.webkitURL;
    	      $scope.fileUrl = url.createObjectURL(blob);
    	});
  	  	/**
		FileDownload.download.get({project:vm.projectName, task:vm.cur_task, dataType:"result",
			fileName:vm.cur_res_file, hdfs:false
			}, function (result) {
			    blob = new Blob([result], { type: 'text/plain' }),
			    url = $window.URL || $window.webkitURL;
			    $scope.fileUrl = url.createObjectURL(blob);
		});
		*/

    };
}