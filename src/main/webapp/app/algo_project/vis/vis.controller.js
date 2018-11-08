angular
    .module('jhipsterSampleApplicationApp')
    .controller('VisualizeController',VisualizeController);

VisualizeController.$inject = ['$scope', '$http', '$window','projectName','GetAlgoTasks','FileDownload'];
function VisualizeController($scope, $http, $window, projectName,GetAlgoTasks,FileDownload) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn
	
	vm.tasks = GetAlgoTasks.get_tasks()
	vm.files = []
	vm.cur_task = null
	vm.cur_file = null
	vm.data_type = null
	vm.fs = "本地"
	vm.hdfs = false	
	vm.data = []
	vm.data_preview = ''
	vm.download = download
	vm.loadData = getData
	vm.columns = []
	vm.render_line = render_line
	vm.line_chart = new G2.Chart({
		container: 'line-chart'
	})
	vm.render_point = render_point
	vm.point_chart = new G2.Chart({
		container: 'point-chart'
	})
	vm.render_bar = render_bar
	vm.bar_chart = new G2.Chart({
		container: 'bar-chart'
	})	
	
		
	$scope.$watch('vm.fs', function (oldValue,newValue) {
		if(vm.fs == "HDFS"){
			vm.hdfs = true
		}
		else if(vm.fs == "本地"){
			vm.hdfs = false
		}
		init()
    });		
	
	$scope.$watch('vm.cur_task', function (oldValue,newValue) {	
		init()
    });
	
	$scope.$watch('vm.data_type', function (oldValue,newValue) {	
		if(vm.data_type == null) return
		vm.cur_file = null		
		vm.files = GetAlgoTasks.get_task_files(vm.cur_task, vm.data_type, vm.hdfs);
    });
		
		
	function init(){
		vm.cur_file = null
		vm.files = []
		vm.data_type = null
		
	}	
	
	function getData(){
  	  	if(vm.cur_task == null | vm.cur_file == null){
  	  		return
  	  	}
  	  	console.log('getData')
  	  	var params = {
  	  		    ProjectName:vm.projectName, task:vm.cur_task, dataType:vm.data_type,
  	  			DataName:vm.cur_file, hdfs:vm.hdfs
  				}		
		$http.get('api/getJsonData', {"params" : params}).success(function(result) {
			vm.data = []
			vm.columns = []
	    	for(i=0;i<result.length;i++){
	    		item = result[i]
		    	vm.data.push(JSON.parse(item))
	    	}
			if(result.length > 0){
				vm.columns = Object.keys(vm.data[0])
			}
			data = result.slice(0,5)	
			s = ''
	    	for(i=0;i<data.length;i++){
	    		item = data[i]
		    	s = s + item + '\n'
	    	}
			vm.data_preview = s
			console.log(vm.data_preview)
			console.log(vm.columns)
		})
	}
		
    function download(){     
    	console.log("download");
  	  	if(vm.cur_task == null | vm.cur_file == null){
  	  		return
  	  	}
  	  	var params = {
  	  			project:vm.projectName, task:vm.cur_task, dataType:vm.data_type,
  				fileName:vm.cur_file, hdfs:vm.hdfs
  				}
    	$http.get('algo_task/download', {"params" : params}).success(function(result) { 
    		/*
              blob = new Blob([result]),
              url = $window.URL || $window.webkitURL;
    	      $scope.fileUrl = URL.createObjectURL(blob);
    	     */ 
    		  var eleLink = document.createElement('a');  
    		  eleLink.style.display = 'none';    		  
    		  var blob = new Blob([result]);
    		  eleLink.download = vm.cur_file;
    		  eleLink.href = URL.createObjectURL(blob);
    		  document.body.appendChild(eleLink);
    		  eleLink.click();
    		  // 然后移除
    		  document.body.removeChild(eleLink);    		  
    		  
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
    
    function render_line(){
    	if(vm.line_x == null | vm.line_y == null){
    		return
    	}
    	if(vm.cur_file==null){
    		return 
    	}
    	vm.line_chart.clear()
    	vm.line_chart.forceFit()
    	vm.line_chart.source(vm.data)
    	axis = vm.line_x + '*' + vm.line_y
    	if(vm.line_color!=null){
    		vm.line_chart.line().position(axis).color(vm.line_color)
    	}else{
    		vm.line_chart.line().position(axis)
    	}
    	vm.line_chart.render()
    }
    
    function render_point(){
    	if(vm.point_x == null | vm.point_y == null){
    		return
    	}
    	if(vm.cur_file==null){
    		return 
    	}
    	vm.point_chart.clear()
    	vm.point_chart.forceFit()
    	vm.point_chart.source(vm.data)
    	axis = vm.point_x + '*' + vm.point_y
    	if(vm.point_color!=null){
    		vm.point_chart.point().position(axis).color(vm.point_color)
    	}else{
    		vm.point_chart.point().position(axis)
    	}
    	vm.point_chart.render()
    }    
    
    function render_bar(){
    	if(vm.bar_x == null | vm.bar_y == null){
    		return
    	}
    	if(vm.cur_file==null){
    		return 
    	}
    	vm.bar_chart.clear()
    	vm.bar_chart.forceFit()
    	vm.bar_chart.source(vm.data)
    	axis = vm.bar_x + '*' + vm.bar_y
    	if(vm.bar_color!=null){
    		vm.bar_chart.interval().position(axis).color(vm.bar_color)
    	}else{
    		vm.bar_chart.interval().position(axis)
    	}
    	vm.bar_chart.render()
    }       
}