angular
    .module('jhipsterSampleApplicationApp')
    .controller('TemplateFillController',TemplateFillController);

TemplateFillController.$inject = ['$scope', '$http','$timeout','$state','$window'];

function TemplateFillController($scope, $http, $timeout, $state, $window){
	var vm = this;
	vm.download_url = "";
    vm.tasks = [];
    vm.xianlu = false
    task = {
        'name':'test_task_1'
    }
    vm.tasks.push(task)
    vm.filelist = ['a1.docx']

    vm.cur_task = 'test_task'
    vm.create_task = create_task
    vm.edit_task = edit_task
    vm.delete_task = delete_task
    vm.run_task = run_task
    vm.delete_all = delete_all
    vm.deleteFileList = delete_filelist
    vm.delete_task_file = delete_task_file
    vm.update_selection = update_selection
    function create_task(){
        taskname = vm.c_taskname
        console.log(taskname)
    }

    function delete_task(taskname){
        
        console.log(taskname)
    }

    function edit_task(taskname){
        vm.cur_task = taskname
        console.log(taskname)
    }

    function run_task(taskname){
        vm.cur_task = taskname
        console.log(vm.cur_task)
    }

    function delete_all(){
        flag = confirm('是否要删除所有任务？')
        if (flag){
            console.log('删除所有任务')
        }
    }

    function delete_filelist(cur_task){
        flag = confirm('是否要删除任务'+cur_task+'下的所有文件？')
        if (flag){
            console.log('删除任务'+cur_task+'所有文件')
        }
    }

    function delete_task_file(cur_task, filename){
        console.log(cur_task)
        console.log(filename)
    }

    function update_selection($event){
		var checkbox = $event.target;
        vm.xianlu = checkbox.checked;
        console.log(vm.xianlu)
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
                // upLoadExtraData:function (previewId, index) {
                //     var data = {ProjectName:vm.projectName};
                //     return data;
                // }
                uploadExtraData:{taskname:taskname}

            }).on("fileuploaded", function (event, data, previewId, index) {
                $("#myModal").modal("hide");
                // $state.go('allFileData', null, { reload: true });
                var a = $timeout(function () {
                    //$state.go('allFileData', {projectName:projectName}, { reload: true });
                    console.log(data.response);
                    vm.filelist = data.response["filelist"];
                    
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

            //导入文件上传完成之后的事件
            // $("#txt_file").on("fileuploaded", function (event, data, previewId, index) {
            //     $("#myModal").modal("hide");
            //     console.log(data.response);
            //     // var data = data.response.lstOrderImport;
            //     // if (data == undefined) {
            //     //     toastr.error('文件格式类型不正确');
            //     //     return;
            //     // }
            //     // //1.初始化表格
            //     // var oTable = new TableInit();
            //     // oTable.Init(data);
            //     // $("#div_startimport").show();
            // });
        }
        return oFile;
    };
    
    vm.download = function(){
    	  console.log("download");
    	  var url = "etl/download";
    	  var params = {
    			  "result_file_path": vm.download_url
    	  }; 
    	  $http.get(url, {"params" : params}).success(function(result) { 
              blob = new Blob([result], { type: 'text/plain' }),
              url = $window.URL || $window.webkitURL;
    	      $scope.fileUrl = url.createObjectURL(blob);
   		});

    };
	
}
