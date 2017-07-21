/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('AllFileDataController',AllFileDataController);

AllFileDataController.$inject = ['$scope', '$http', '$state', 'GetHdfsData', 'GetAlgorithmData', 'HdfsUpload', 'GetServerData', 'GetServerProject', '$timeout'];
function AllFileDataController($scope, $http, $state, GetHdfsData, GetAlgorithmData, HdfsUpload, GetServerData, GetServerProject, $timeout) {
    var vm = this;
    vm.algrithmData = [];
    vm.projects = [];
    vm.projectName = "pso";
    vm.upServerModal = false;
    vm.checkList = [];
    GetServerProject.get({}, function (res) {
        vm.projects = res;
    }, function (res) {

    });
    $scope.$watch('vm.projectName', function (oldValue,newValue) {
        console.log(vm.projectName);
        vm.serverData = [];
        GetServerData.get({ProjectName:vm.projectName}, function (result) {
            for (var i = 0; i< result.length; i++) {
                vm.serverData[i] = result[i].split("+");
                vm.checkList[i] = false;
                if (vm.serverData[i][1] == '0') {
                    vm.serverData[i][1] = false;
                }else
                    vm.serverData[i][1] = true;

            }
            console.log(vm.serverData);
        }, function (result) {
        });
    });

    vm.changeCheck = changeCheck;
    function changeCheck(index) {
        // console.log(vm.checkList[0]);
    }

    $scope.$watch('vm.projectName', function (oldValue,newValue) {
        vm.hdfsData = [];
        GetHdfsData.get({ProjectName:vm.projectName}, function (res) {
            vm.hdfsData = res;
        });
    });


    vm.alFileUpload = alFileUpload;
    function alFileUpload() {
        // $state.go('fileUpload', {fileType:1});
    }

    vm.dataFileUpload = dataFileUpload;
    function dataFileUpload() {
        var oFileInput = FileInput();
        oFileInput.Init("txt_file", "api/uploadData", vm.projectName);
        vm.upServerModal = true;

    }

    vm.fileUpHdfs = fileUpHdfs;
    vm.nameList = [];
    function fileUpHdfs() {
        console.log(vm.checkList);
        for(i in vm.checkList) {
            if (vm.checkList[i]) {
                vm.nameList.push(vm.serverData[i][0]);
            }
        }
        console.log(vm.nameList);
        HdfsUpload.get({ProjectName:vm.projectName, DataName:vm.nameList }, function (res) {
            console.log(res);
            $state.go('allFileData', null, { reload: true });
        }, function (res) {
            console.log(res);
        });
        vm.nameList = [];
    }

    //初始化fileinput
    var FileInput = function () {
        var oFile = new Object();

        //初始化fileinput控件（第一次初始化）
        oFile.Init = function(ctrlName, uploadUrl, projectName) {
            console.log(vm.projectName);
            var control = $('#' + ctrlName);

            //初始化上传控件的样式
            control.fileinput({
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
                uploadExtraData:{ProjectName:projectName}

            }).on("fileuploaded", function (event, data, previewId, index) {
                // $("#myModal").modal("hide");
                console.log(data.response);
                var a = $timeout(function () {
                    $state.go('allFileData', null, { reload: true });

                },1000);

                $timeout.cancel(a);

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


}
