angular
    .module('jhipsterSampleApplicationApp')
    .controller('TaskFileController',TaskFileController);

TaskFileController.$inject = ['$scope', '$http', "projectName","GetAlgoTasks"];
function TaskFileController($scope, $http, projectName,  GetAlgoTasks) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn
	
	vm.tasks = GetAlgoTasks.get_tasks()
	
	//Variable
	vm.files = []
	vm.dataType = "data"
	vm.cur_task = null
	vm.dataType_CN = "数据文件"
	vm.para = {}
	vm.para['name'] = ''
	vm.para['col'] = []
	vm.hdfs = false
	vm.fs = "本地"
	//Functions
	vm.get_task_files = get_task_files
	vm.delete_task_file = delete_task_file
	vm.add_item = add_item
	vm.init_para = init_para
	vm.submit_para = submit_para
	

	
	$scope.$watch('vm.dataType', function (oldValue,newValue) {
		console.log(vm.dataType)
		if(vm.dataType == "data"){
			vm.dataType_CN = "数据文件"
		}
		else if(vm.dataType == "algo"){
			vm.dataType_CN = "算法文件"
		}
		else{
			vm.dataType_CN = "参数配置文件"
		}
    });
	
	$scope.$watch('vm.fs', function (oldValue,newValue) {
		if(vm.fs == "HDFS"){
			vm.hdfs = true
		}
		else if(vm.fs == "本地"){
			vm.hdfs = false
		}
		console.log(vm.fs, vm.hdfs)
    });
	
	
 
    vm.file_upload = dataFileUpload;
  	function dataFileUpload() {
  			if(vm.cur_task == null | vm.dataType==null) return
  			console.log(vm.cur_task)
  			if(vm.dataType == "para"){
  				submit_para()
  				return
  			}
  	        var oFileInput = FileInput(); 	
  	        oFileInput.Init("txt_file", "algo_task/uploadAlgoTaskFile", vm.projectName, vm.cur_task, vm.dataType, vm.hdfs);

  	    }
      //初始化fileinput
      var FileInput = function () {
          var oFile = new Object();
          console.log("init")
          //初始化fileinput控件（第一次初始化）
          oFile.Init = function(ctrlName, uploadUrl, projectName, taskName, dataType, hdfs) {
              var control = $('#' + ctrlName);
              //初始化上传控件的样式
              $('#txt_file').fileinput(
            		  'refresh',{
                  language: 'zh', //设置语言
                  uploadUrl: uploadUrl, //上传的地址
                  allowedFileExtensions: ['txt', 'json', 'csv', 'pdf', 'word', 'py'],//接收的文件后缀
                  showUpload: true, //是否显示上传按钮
                  showCaption: false,//是否显示标题
                  browseClass: "btn btn-primary", //按钮样式
                  maxFileCount: 10, //表示允许同时上传的最大文件个数
                  //maxFilePreviewSize: 10240, // 最大文件大小
                  enctype: 'multipart/form-data',
                  validateInitialCount:true,
                  previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                  msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
                  uploadExtraData:{project: projectName, task: taskName, dataType:dataType, hdfs:hdfs}

              }).on("fileuploaded", function (event, data, previewId, index) {
                  $("#file_modal").modal("hide");
				   get_task_files(vm.dataType, vm.hdfs)

              });


          }
          return oFile;
      };
      
      function get_task_files(){
		   if(vm.cur_task == null | vm.dataType==null) return
    	   vm.files = GetAlgoTasks.get_task_files(vm.cur_task, vm.dataType, vm.hdfs)
      }
      
      function delete_task_file(file){
    	  console.log(file)
		   url = "algo_task/delTaskFile"
		   url = url + "?project=" + vm.projectName +"&task="+vm.cur_task+"&dataType="+vm.dataType+"&fileName="+file
		   url += "&hdfs=" + vm.hdfs
		   $http.get(url).success(function(result){    		   
			   if(result['flag']){
				   alert("删除成功")
				   get_task_files(vm.dataType)
			   }
			   else{
				   alert("删除失败")
			   }
		   });
      }
      
      function init_para(){
    	  	vm.para = {}
    		vm.para['name'] = ''
    		vm.para['col'] = []
      }
      
      function add_item(){
    	  item = {}
    	  item['name'] = ''
    	  item['type'] = false
    	  vm.para['col'].push(item)
      }
      
      function submit_para(){
    	  items = vm.para.col
    	  s = new Set()
    	  if(items.length == 0){
    		  alert("至少需要一个参数")
    	  }
    	  for (i=0;i<items.length;i++){
    		  name = items[i].name
    		  if (name.length == 0){
    			  alert("参数名不能为空")
    			  return
    		  }
    		  s.add(name)
    	  }
    	  if(s.size != items.length){
    		  alert("参数名不能重复")
    		  return
    	  }
    	  console.log(vm.para)
    	  data = {
      		"project":vm.projectName,
  			"task":vm.cur_task,
  			"para":vm.para,
  			"hdfs":vm.hdfs
	      }
    	  
    	  console.log(data)
	      url = "algo_task/addPara";
	      $http.post(url, data).success(function(result) {  
			    console.log(result);

			});
	      }
    
	
}