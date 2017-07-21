/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('AllFileAlController',AllFileAlController);

AllFileAlController.$inject = ['$scope', '$http', '$state', 'GetAlgorithmData', 'HdfsUpload', 'GetServerProject', '$stateParams', '$timeout'];
function AllFileAlController($scope, $http, $state, GetAlgorithmData, HdfsUpload, GetServerProject, $stateParams, $timeout) {
    var vm = this;
    vm.fileData = [];
    vm.algrithmData = [];
    vm.projectName = $stateParams.projectName;
    vm.paramtersDes = null;
    vm.projects = [];

    GetServerProject.get({}, function (res) {
        vm.projects = res;
    }, function (res) {

    });

    var type="Algorithm";
    $scope.$watch('vm.projectName', function () {
        GetAlgorithmData.get({ProjectName:vm.projectName},function (res) {
            console.log(res);
            vm.algrithmData = res;
        }, function (result) {
        });
    });


    vm.alFileUpload = alFileUpload;
    function alFileUpload() {
        // $state.go('fileUpload', {fileType:1});
    }

    vm.fileUpHdfs = fileUpHdfs;
    function fileUpHdfs(index) {
        console.log(index);
        HdfsUpload.get({DataName:vm.fileData[index][0]}, function (res) {
            console.log(res);
            $state.go('allFileAl', {projectName:vm.projectName}, { reload: true });
        }, function (res) {
            console.log(res);
        });
    }

    //初始化fileinput
    var FileInput = function () {
        var oFile = new Object();

        //初始化fileinput控件（第一次初始化）
        oFile.Init = function(ctrlName, uploadUrl) {
            var control = $('#' + ctrlName);

            //初始化上传控件的样式
            control.fileinput({
                language: 'zh', //设置语言
                uploadUrl: uploadUrl, //上传的地址
                allowedFileExtensions: ['txt', 'json', 'csv', 'pdf', 'word'],//接收的文件后缀
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
                uploadExtraData:{ProjectName:vm.projectName, ParameterDescribe:vm.paramtersDes},

            });

            //导入文件上传完成之后的事件
            $("#txt_file").on("fileuploaded", function (event, data, previewId, index) {
                $("#myModal").modal("hide");
                var a = $timeout(function () {
                    $state.go('allFileAl', {projectName:vm.projectName}, { reload: true });
                    console.log(data.response);
                },1000);

                // var data = data.response.lstOrderImport;
                // if (data == undefined) {
                //     toastr.error('文件格式类型不正确');
                //     return;
                // }
                // //1.初始化表格
                // var oTable = new TableInit();
                // oTable.Init(data);
                // $("#div_startimport").show();
            });
        }
        return oFile;
    };

    var oFileInput = FileInput();
    oFileInput.Init("txt_file", "api/uploadAlgorithm");

}
