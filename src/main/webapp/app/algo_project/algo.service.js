
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .service('GetAlgoTasks', GetAlgoTasks);

    GetAlgoTasks.$inject = ['$http', 'projectName'];

    function GetAlgoTasks($http, projectName){    
    	this.get_tasks = get_tasks
    	this.get_task_files = get_task_files
    	
    	function get_tasks(){
    	    var url = "algo_task/getTasks"
    	    url = url + "?project=" + projectName.name
	    	var tasks = []

    	    $http.get(url).success(function(result){
    	        for(var i=0;i<result["tasks"].length;i++){
    	            var task = result["tasks"][i]
    	            task = JSON.parse(task)
    	            tasks.push(task)
    	        }
    	    });
    	    return tasks
    	 }
    	
       function get_task_files(cur_task, dataType, hdfs){
     	   var url = "algo_task/getTaskFiles"
 		   url = url + "?project=" + projectName.name +"&task="+cur_task+"&dataType="+dataType
 		   url += "&hdfs=" + hdfs
 		   var files = []
 		   $http.get(url).success(function(result){    		   
 			    for(var i=0;i<result["files"].length;i++){
    	            var file = result["files"][i]
    	            files.push(file)
    	        }
 		   });
     	   return files
       }
    }
})();
