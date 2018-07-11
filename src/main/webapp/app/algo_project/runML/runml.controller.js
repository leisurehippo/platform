angular
    .module('jhipsterSampleApplicationApp')
    .controller('RunMLController',RunMLController);

RunMLController.$inject = ['$scope', '$http',"projectName", "GetAlgoTasks", "RunMLService"];
function RunMLController($scope, $http, projectName, GetAlgoTasks, RunMLService) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn	
	vm.tasks = GetAlgoTasks.get_tasks()
	vm.cur_task = null		
	vm.type_map = {
		"分类": "classification",
		"回归":"regression",
		"聚类": "cluster"
	}
	
	vm.type_cn = "分类"
	vm.type = "classification" // regression, cluster
	vm.algos = []	
	vm.cur_algo = null
	vm.para = []
	vm.train_file = null
	vm.test_file = null
	vm.data_files = []	
	vm.mode = "train"
	vm.cur_model = null


	getHDFSAlgo()
	
	vm.run = run
		
	$scope.$watch('vm.cur_task', function (oldValue,newValue) {	
		if(vm.cur_task == null) return
		init()
		vm.data_files = GetAlgoTasks.get_task_files(vm.cur_task, "data",true)
		vm.model_files = GetAlgoTasks.get_task_files(vm.cur_task, "model",true)

    });		
	
	$scope.$watch('vm.type_cn', function (oldValue,newValue) {	
		if(vm.cur_task == null) return

		console.log(vm.type_cn)
		vm.type = vm.type_map[vm.type_cn]
		getHDFSAlgo()
    });	
	
	$scope.$watch('vm.cur_algo', function (oldValue,newValue) {	
		if(vm.cur_algo == null) return
		console.log(vm.cur_algo)
		getAlgoPara()
    });	
	
	$scope.$watch('vm.mode', function (oldValue,newValue) {	
		if(vm.cur_task == null) return
		if(vm.mode == "test"){
			vm.para = []
			vm.train_file = null
			vm.test_file = null
		}else{
			getAlgoPara()
			vm.data_files = GetAlgoTasks.get_task_files(vm.cur_task, "data",true)
		}
		vm.model_files = GetAlgoTasks.get_task_files(vm.cur_task, "model",true)

    });	
	
	function init(){
		vm.type_cn = "分类"
		vm.type = "classification" // regression, cluster
		vm.algos = []	
		vm.cur_algo = null
		vm.para = []
		vm.train_file = null
		vm.test_file = null
		vm.data_files = []	
		vm.mode = "train"
		getHDFSAlgo()
	}
	
	function getAlgoPara(){
		url = "api/getLibraryParameter"
		url = url + "?Algorithm=" + vm.cur_algo +"&type=" +vm.type
	    $http.get(url).success(function(result){
	    	vm.para = []
	    	for(i=0;i<result.length;i++){
		    	var item = {}
	    		tmp = result[i].split(":")
	    		item['name'] = tmp[0]
	    		item['value'] = tmp[1]
		    	vm.para.push(item)
	    	}
	    	console.log(vm.para)
	    });
	}
	
	function  getHDFSAlgo(){
		url = "api/getSparkAlgorithms"
		url = url + '?type=' + vm.type
	    $http.get(url).success(function(result){  
	    	vm.algos = result.slice()
	    });	
	}
	
	function run(){
		//train mode
		if(vm.cur_task == null){
			alert("请选择一个任务")
			return
		}
		if(vm.cur_algo == null){
			alert("请选择一个算法")
			return
		}
		if(vm.mode == "train"){
			console.log("train")
			if(vm.train_file == null | vm.test_file == null){
				alert("请选择数据文件")
				return
			}
			
			var para = {}
		   	for(i=0;i<vm.para.length;i++){
	    		if (vm.para[i].value.length == 0){
	    			alert("参数不能为空")
	    			return
	    		}
	    		para[vm.para[i].name] = vm.para[i].value
	    	}
			
			RunMLService.train.get({project:vm.projectName, task:vm.cur_task, algoType:vm.type,
				algo:vm.cur_algo, para:JSON.stringify(para), trainData:vm.train_file,
				testData: vm.test_file
			}, function (res) {
                console.log(res)
                alert("Done")
                vm.log = res.join('\n')
            });
		}
		else{
			console.log("test")
			if(vm.test_file == null){
				alert("请选择数据文件")
				return
			}
			if(vm.cur_model == null){
				alert("请选择模型")
				return
			}
			
			RunMLService.test.get({project:vm.projectName, task:vm.cur_task, algoType:vm.type,
				algo:vm.cur_algo, testData: vm.test_file, model:vm.cur_model
				
			},function(res){
                console.log(res)
                alert("Done")
                vm.log = res.join('\n')
			});
		}
	}
	
}	