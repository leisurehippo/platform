angular
    .module('jhipsterSampleApplicationApp')
    .controller('TextETLController',TextETLController);

TextETLController.$inject = ['$scope', '$http','$timeout','$state','$window'];

function TextETLController($scope, $http, $timeout, $state, $window){
	var vm = this;
	vm.sep = ",";
	vm.columns = [];
	vm.download_url = "";
    vm.skipHead = false;
    
    vm.selected = new Array();
	vm.updateSkipHeader = function($event){
		var checkbox = $event.target;
		vm.skipHead = checkbox.checked;
		console.log(vm.skipHead);
	}
	
	vm.updateType= function(index, type){
		console.log(index,type);
		vm.selected[index][1] = type;
	}
	vm.updateSelection = function($event, index){
		var checkbox = $event.target;
        console.log(index,checkbox.checked);
        vm.selected[index][0] = checkbox.checked;
        console.log(vm.selected);
	}
	
	vm.run = function run(){
		var columns = new Array();
		for(var i=0;i<vm.selected.length;i++){
			if(vm.selected[i][0]){
				var s = {}
				s["index"] = i;
				s["type"] = vm.selected[i][1];
				columns.push(s);
			}
		}
		console.log(columns);
		data = {
			"column":columns,
			"skipHeader":vm.skipHead,
			"transform":vm.transformItems		
		};
		url = "etl/runTextJob";

		$http.post(url, data).success(function(result) {  
		   console.log(result);
		   if (result.message == "success"){
			   alert("转换成功");
		   }
		   else{
			   alert("转换失败");
		   }
		   vm.download_url = result.result_file_path;
		});
	}
	
	
	
    vm.dataFileUpload = dataFileUpload;
	function dataFileUpload() {
	        var oFileInput = FileInput();
	        oFileInput.Init("txt_file", "etl/uploadETLFile", vm.skipHead);
	        vm.upServerModal = true;

	    }
    //初始化fileinput
    var FileInput = function () {
        var oFile = new Object();

        //初始化fileinput控件（第一次初始化）
        oFile.Init = function(ctrlName, uploadUrl, skipHead) {
            var control = $('#' + ctrlName);
            console.log(skipHead);
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
                uploadExtraData:{skipHead:skipHead}

            }).on("fileuploaded", function (event, data, previewId, index) {
                $("#myModal").modal("hide");
                // $state.go('allFileData', null, { reload: true });
                var a = $timeout(function () {
                    //$state.go('allFileData', {projectName:projectName}, { reload: true });
                    console.log(data.response);
                    console.log(data.response["cols"]);
                    vm.columns = data.response["cols"];
                    for(var i=0; i<vm.columns.length;i++){
                    	vm.selected.push([false,"string"]);
                    }
                    console.log(vm.selected);
                    
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
    
    //后续代码和etl.controler.js重复,后续可以将两者提取到一个server.js中
    vm.transformItems = [];
	
	vm.rmTransItem = function(){
		vm.transformItems = [];
	};
	
	//感觉有点冗余，待优化...。如果有后面的同学看到这段代码，只能说抱歉
	function validIndex(index){
		if (index>=vm.selected.length){
			return false;
		}
		return true;
	}
	function addTransItem(name, columnIndex, paras){
		if (!validIndex(columnIndex)){
			console.log(columnIndex);
			alert("超出输入表的列数，请重新输入");
			return;
		}
		item = {
				"name":name,
				"columnIndex":columnIndex,
				"paras":paras
		};
		vm.transformItems.push(item);
		console.log(vm.transformItems);
	};

	
	vm.createT1 = function(){
		name = 'dx_substr'
		columnIndex = vm.t1_columnIndex;
		paras = [vm.t1_begin.toString(),vm.t1_end.toString()];
		addTransItem(name, columnIndex,paras);
		
	};
	vm.createT2 = function(){
		name = "dx_pad";
		columnIndex = vm.t2_columnIndex;
		paras = [vm.t2_lr,vm.t2_len.toString(),vm.t2_char];
		addTransItem(name, columnIndex,paras);
	};
	vm.createT3 = function(){
		name = "dx_replace";
		columnIndex = vm.t3_columnIndex;
		paras = [vm.t3_begin.toString(),vm.t3_len.toString(),vm.t3_char];
		addTransItem(name, columnIndex,paras);	
	};
	vm.createT4 = function(){
		name = "dx_filter";
		columnIndex = vm.t4_columnIndex;
		paras = [vm.t4_operator,vm.t4_value];
		addTransItem(name, columnIndex,paras);
	};
	
}
