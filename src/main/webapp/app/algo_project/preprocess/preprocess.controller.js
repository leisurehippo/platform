angular
    .module('jhipsterSampleApplicationApp')
    .controller('PreprocessController',PreprocessController);

PreprocessController.$inject = ['$scope', '$http', "projectName","GetAlgoTasks","PreprocessService"];
function PreprocessController($scope, $http, projectName,  GetAlgoTasks, PreprocessService) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn
	
	vm.tasks = GetAlgoTasks.get_tasks()
	
	//Variable
	vm.files = []
	vm.cur_file = null
	vm.dataType = "data"
	vm.cur_task = null
	vm.hdfs = false
	vm.fs = "本地"
	vm.outDataType = "txt"
	vm.outFileName = ""
	vm.header = "false"		
	vm.columns = [{"name":"label"}]
	vm.seps = ["空格",  ","]	
	vm.cur_sep = "空格"
	//vm.operations = ["drop","dropDuplicates", "noop", "MinMaxScaler","StandardScaler", "MaxAbsScaler","Bucketizer"
	//	,"Binarizer", "StringIndexer", "OneHot"]		
	vm.operations = []
	vm.log = "test log"
	vm.file_content = "test file content"
	vm.out_info = "out file data preview"
	//Functions
	vm.get_task_files = get_task_files
	vm.show = preview
	vm.transform = transform
	get_transforms()
	
	function init(){
		vm.log = ""
		vm.file_content = ""
		vm.columns = []
	}
	
	$scope.$watch('vm.cur_task', function (oldValue,newValue) {
		get_task_files()
		init()
    });	
		
	$scope.$watch('vm.fs', function (oldValue,newValue) {
		if(vm.fs == "HDFS"){
			vm.hdfs = true
		}
		else if(vm.fs == "本地"){
			vm.hdfs = false
		}
		console.log(vm.fs, vm.hdfs)
		get_task_files()
		init()

    });
	
		
	$scope.$watch('vm.cur_file', function (oldValue,newValue) {
		if(vm.cur_file == null){
			return
		}		
    });	
	
	function get_transforms(){
	   url = "preprocess/supported"
	   $http.get(url).success(function(result){    		   
		   vm.operations = result['supported']
	   });
	}
	
	
    function get_task_files(){
		   if(vm.cur_task == null | vm.dataType==null) return
  	   vm.files = GetAlgoTasks.get_task_files(vm.cur_task, vm.dataType, vm.hdfs)
    }
    
    function get_file_content(){
    	console.log()
    }
    
    function preview(){
    	if(vm.cur_sep == "空格"){
    		delimiter = " "
    	}
    	else {
    		delimiter = vm.cur_sep
    	}
		PreprocessService.preview.get({project:vm.projectName, task:vm.cur_task, dataType:vm.dataType,
			fileName:vm.cur_file, hdfs: vm.hdfs, delimiter:delimiter, header: vm.header			
		},function(res){
			vm.columns = []
	        for(i=0;i<res["columns"].length;i++){
	        	colInfo = {}
	            colName = res["columns"][i]
	        	colInfo["name"] = colName
	        	colInfo["operation"] = "noop";	        	
				vm.columns.push(colInfo)	        	
	        }
            
            vm.file_content = res["info"]
		});
    }
    
    function transform(){
    	if(vm.cur_sep == "空格"){
    		delimiter = " "
    	}
    	else if(vm.cur_sep == "无"){
    		delimiter = ""
    	}
    	else {
    		delimiter = vm.cur_sep
    	}

    	colInfo = {}
    	colInfo["colMaps"] = vm.columns
    	PreprocessService.transform.get({
    		project:vm.projectName, task:vm.cur_task, dataType:vm.dataType,
			fileName:vm.cur_file, hdfs: vm.hdfs, delimiter:delimiter,
			outFileName: vm.outFileName, colMaps: colInfo, outType: vm.outDataType,
			header: vm.header
    	},function(res){
    		console.log(res)
    		vm.log = res["log"].join('\n')
    		vm.out_info = res["info"]
    	});
    }
    
	
}