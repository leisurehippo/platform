angular
    .module('jhipsterSampleApplicationApp')
    .controller('TemplateFillController',TemplateFillController);

TemplateFillController.$inject = ['$scope', '$http','$timeout','$state','$window'];

function TemplateFillController($scope, $http, $timeout, $state, $window){
	var vm = this;
    vm.download_url = "";
    
    //variables
    vm.tasks = [];
    vm.filelist = []
    vm.cur_task = []
    vm.cur_index = 0
    vm.result_infos = []
    vm.task_result_urls = []

    //functions
    vm.create_task = create_task
    vm.edit_task = edit_task
    vm.delete_task = delete_task
    vm.run_task = run_task
    vm.deleteFileList = delete_filelist
    vm.delete_task_file = delete_task_file
    vm.get_all = get_all
    vm.changeTaskType = changeTaskType
    vm.searchByTime = searchByTime
    vm.searchByName = searchByName
    vm.getResult = getResult


    function get_all(){
        console.log("get all task")
        url = "templatefill/getAllTasks"
        $http.get(url).success(function(result){
            tasks = []
            for(i=0;i<result["tasks"].length;i++){
                task = result["tasks"][i]
                tasks.push(to_task(task))
            }
            vm.tasks = tasks
            vm.task_result_urls = new Array(tasks.length)

        });
    }

    function to_task(s){
        var items = s.split(" ")
        task = {
            "taskname":items[0],
            "type":items[1],
            "username":items[2],
            "createTime":items[3]
        }
        return task
    }

    function create_task(){   
        console.log("create task")   
        url = "templatefill/addTask";
        url = url + "?taskname="+vm.c_taskname+"&type="+vm.c_tasktype

		$http.get(url).success(function(result) {  
            console.log(result);
            if(result.flag){
                alert("创建成功")
                get_all()
            }
            else{
                alert("创建失败")
            }
		});
    }

    function delete_task(index){
        task = vm.tasks[index]
        flag = confirm('是否要删除任务'+task.taskname)
        if (!flag){
            return
        }
        url = "templatefill/delTask";
        url = url + "?taskname="+task.taskname

		$http.get(url).success(function(result) {  
            console.log(result);
            if(result.flag){
                alert("删除成功")
                get_all()
            }
            else{
                alert("删除失败")
            }
		});
    }

    function getTaskFiles(taskname){
        url = "templatefill/getTaskFiles";
        url = url + "?taskname="+taskname

		$http.get(url).success(function(result){  
            console.log(result);
            vm.filelist = result.files;
		});
    }

    function edit_task(index){
        task = vm.tasks[index]
        vm.cur_task = task
        vm.cur_index = index
        getTaskFiles(vm.cur_task.taskname)
    }

    function changeTaskType(){
        vm.tasks[vm.cur_index] = vm.cur_task 
        console.log(vm.cur_task)
        url = "templatefill/updateTask";
        url = url + "?taskname="+vm.cur_task.taskname+"&type="+vm.cur_task.type

		$http.get(url).success(function(result) {  
            console.log(result);
            if(result["flag"]){
                alert("类型改变成功")
            }
            else{
                alert("类型改变失败失败")
            }
		});
        
    }

    function searchByTime(){
        url = "templatefill/searchByTime";
        url = url + "?beginTime="+vm.beginTime+"&endTime="+vm.endTime

		$http.get(url).success(function(result) {  
            tasks = []
            for(i=0;i<result["tasks"].length;i++){
                task = result["tasks"][i]
                tasks.push(to_task(task))
            }
            if(tasks.length>0){
                vm.tasks = tasks
            }
            else{
                alert("没有找到符合条件的任务")
            }
		});
    }
    function searchByName(){
        
        url = "templatefill/getTask";
        url = url + "?taskname="+vm.s_taskname

		$http.get(url).success(function(result) {  
            task = result["task"]
            console.log(task)

            if(task != "null"){
                vm.tasks = [to_task(task)]
            }
            else{
                alert("没有找到符合条件的任务")
            }
		});
    }

    function run_task(index){
        task = vm.tasks[index]
        vm.cur_task = task
        vm.cur_index = index
        url = "templatefill/runTask";
        url = url + "?taskname="+vm.cur_task.taskname
        $http.get(url).success(function(result) {  
            console.log(result);
            if(result["flag"]){
                alert("运行成功")
            }
            else{
                alert("运行失败")
            }
		});
    }

 

    function delete_filelist(cur_task){
        flag = confirm('是否要删除任务'+cur_task+'下的所有文件？')
        if (!flag){
            return 
        }
        url = "templatefill/delTaskFiles";
        url = url + "?taskname="+cur_task

		$http.get(url).success(function(result) {  
            console.log(result);
            if(result["flag"]){
                alert("删除成功")
                getTaskFiles(cur_task)
            }
            else{
                alert("删除失败")
            }
		});
    }

    function delete_task_file(cur_task, filename){
        console.log(cur_task)
        console.log(filename)
        flag = confirm('是否要删除任务'+cur_task+'下的文件？'+filename)
        if (!flag){
            return 
        }
        url = "templatefill/delTaskFile";
        url = url + "?taskname="+cur_task+"&filename="+filename

		$http.get(url).success(function(result) {  
            console.log(result);
            if(result["flag"]){
                alert("删除成功")
                getTaskFiles(cur_task)
            }
            else{
                alert("删除失败")
            }
		});
    }
/*
    vm.column_names = ["file_name","fault_name","unit_name","substation","tower_id","voltaget","device_type",
            "device_id","manufacturer","run_date","find_date","weather","temperature","humidity",
          "location","bad_level","protective_action","protective_device","pre_situation","in_situation","other_time"]
]*/
    vm.column_names = ['报告名称','故障名称', '单位名称', '变电站名称', '塔号','电压等级', '设备类型','型号','生产厂家','投运时间', '故障发现时间','测试时天气'
    ,'测试环境温度', '测试环境湿度', '地理位置','缺陷等级', '保护动作','保护装置','故障前情况','故障时情况','其他时间']
    function getResult(taskname){
        console.log(taskname)
        url = "templatefill/getResult" + "?taskname="+taskname
        $http.get(url).success(function(result) {  
            console.log(result)
            vm.result_infos = result["result_infos"]
            var data = []
            for(i=0;i<vm.result_infos.length;i++){
                row = vm.result_infos[i]
                var d = {

                }
                for(j=0;j<vm.column_names.length;j++){
                    d[vm.column_names[j]] = row[j]
                }
                data.push(d)
            }
            $('#table').bootstrapTable('destroy'); 
            $('#table').bootstrapTable({
                    data: data
              });
		});
    }





    vm.file_upload = dataFileUpload;
	function dataFileUpload(taskname) {
            console.log(taskname)
	        var oFileInput = FileInput();
	        oFileInput.Init("txt_file", "templatefill/uploadFile", taskname);
	        vm.upServerModal = true;

	    }
    //初始化fileinput
    var FileInput = function () {
        var oFile = new Object();

        //初始化fileinput控件（第一次初始化）
        oFile.Init = function(ctrlName, uploadUrl, taskname) {
            var control = $('#' + ctrlName);
            //初始化上传控件的样式
            $('#txt_file').fileinput({
                language: 'zh', //设置语言
                uploadUrl: uploadUrl, //上传的地址
                // allowedFileExtensions: ['txt', 'json', 'csv', 'pdf', 'word'],//接收的文件后缀
                showUpload: true, //是否显示上传按钮
                showCaption: false,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式
                //dropZoneEnabled: false,//是否显示拖拽区域
                //minImageWidth: 50, //图片的最小宽度
                //minImageHeight: 50,//图片的最小高度
                //maxImageWidth: 1000,//图片的最大宽度
                //maxImageHeight: 1000,//图片的最大高度
                //maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
                //minFileCount: 0,
                maxFileCount: 10, //表示允许同时上传的最大文件个数
                enctype: 'multipart/form-data',
                validateInitialCount:true,
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
                uploadExtraData:{taskname:taskname}

            }).on("fileuploaded", function (event, data, previewId, index) {
                $("#myModal").modal("hide");
                // $state.go('allFileData', null, { reload: true });
                var a = $timeout(function () {
                    //$state.go('allFileData', {projectName:projectName}, { reload: true });
                    console.log(data.response);
                    vm.filelist = data.response["filelist"];
                    //alert("上传成功")
                },1000);

                // $timeout.cancel(a);

            });


            $('#txt_file').on('fileerror', function(event, data, msg) {
                console.log(data.id);
                console.log(data.index);
                console.log(data.file);
                console.log(data.reader);
                console.log(data.files);
                // get message
                alert(msg);
            });

        }
        return oFile;
    };
    
    vm.download = function(index){
          taskname = vm.tasks[index].taskname
    	  console.log("download");
    	  var url = "templatefill/download";
    	  var params = {
    			  "taskname": taskname
          }; 
          console.log(taskname)
    	  $http.get(url, {"params" : params}).success(function(result) { 
              blob = new Blob([result], { type: 'text/plain' }),
              url = $window.URL || $window.webkitURL;
              vm.task_result_urls[index] = url.createObjectURL(blob);
    	      //$scope.fileUrl = url.createObjectURL(blob);
   		});

    };

    
	
}
