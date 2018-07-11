angular
    .module('jhipsterSampleApplicationApp')
    .controller('RunController',RunController);

RunController.$inject = ['$scope', '$http',"projectName", "GetAlgoTasks"];
function RunController($scope, $http, projectName, GetAlgoTasks) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn
	
	//var
	vm.tasks = GetAlgoTasks.get_tasks()
	vm.cur_task = null
	vm.files = []
	vm.algo_files = []
	vm.para_files = []
	vm.data_files = []
	
	vm.cur_algo_file = null
	vm.cur_para_file = null
	vm.para_submit = {}
	vm.para = {}
	vm.log = ""
		
	vm.run = run
	
	
	
	$scope.$watch('vm.cur_task', function (oldValue,newValue) {
		console.log(vm.cur_task)
		if (vm.cur_task == null){
			return
		}
		init()
		get_task_files("algo")
		get_task_files("para")
		get_task_files("data")	
    });
	
	
	
	$scope.$watch('vm.cur_para_file', function (oldValue,newValue) {
		if (vm.cur_task == null){
			return
		}
		console.log(vm.cur_para_file)
	 	url = "algo_task/getPara"
	 	url = url + "?project=" + vm.projectName +"&task="+vm.cur_task+"&para="+vm.cur_para_file
	    $http.get(url).success(function(result){  
	    	vm.para = JSON.parse(result['para'])
	    	console.log(vm.para)
	    });
		
    });
	
	$scope.$watch('vm.cur_algo_file', function (oldValue,newValue) {
		if (vm.cur_task == null){
			return
		}
		console.log(vm.cur_algo_file)
		
	 	url = "algo_task/getPara"
	 	url = url + "?project=" + vm.projectName +"&task="+vm.cur_task+"&para="+vm.cur_para_file
	    $http.get(url).success(function(result){  
	    	vm.para = JSON.parse(result['para'])
	    });	    
		
    });

	
	function init(){
		vm.algo_files = []
		vm.para_files = []
		vm.data_files = []
		
		vm.cur_algo_file = null
		vm.cur_para_file = null
		vm.para_submit = {}
		vm.para = {}
		vm.log = ""
	}
	
    function get_task_files(dataType){
 	   vm.dataType = dataType
	   if(dataType=="algo"){
		   vm.algo_files = GetAlgoTasks.get_task_files(vm.cur_task, dataType, false)
		   
	   }
	   else if(dataType=="para"){
		   vm.para_files = GetAlgoTasks.get_task_files(vm.cur_task, dataType, false)
	   }
	   else if(dataType=="data"){
		   vm.data_files = GetAlgoTasks.get_task_files(vm.cur_task, dataType, false)
	   }
   }
        
    function run(){
    	var sub_para = {}
    	console.log("run")
    	if( vm.cur_algo_file == null | vm.cur_para_file == null){
    		alert("参数或者文件不能为空")
    		return
    	}
    	col = vm.para.col
    	
    	for(i=0;i<col.length;i++){
    		if (col[i].value == null){
    			alert("参数不能为空")
    			return
    		}
    	}

    	  data = {
      		"project":vm.projectName,
  			"task":vm.cur_task,
  			"para":vm.para,
  			"algo_file":vm.cur_algo_file
  	      }
    	  
    	  console.log(data)
    	  url = "algo_task/runLocal";
          $http.post(url, data).success(function(result) {  
        	  alert("运行完成， 运行过程请查看log")
        	  vm.log = result['log']
        });
    }
    

    
}