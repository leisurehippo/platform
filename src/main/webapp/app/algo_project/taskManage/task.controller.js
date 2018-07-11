angular
    .module('jhipsterSampleApplicationApp')
    .controller('TaskController',TaskController);

TaskController.$inject = ['$scope', '$http', "projectName"];
function TaskController($scope, $http, projectName) {
	var vm = this
	vm.projectName = projectName.name
	vm.projectName_CN = projectName.name_cn

	//Variables
	vm.tasks = []
	vm.cur_task = null
	
	
	//functions
    vm.get_all = get_all
    vm.create_task = create_task
	vm.delete_task = delete_task
	vm.searchByName = searchByName
	vm.searchByTime = searchByTime
	vm.edit_task = edit_task
	vm.edit_task_submit = edit_task_submit
	
	
	
	get_all()
	
    function get_all(){
	    console.log("get all task")
	    url = "algo_task/getTasks"
	    url = url + "?project=" + vm.projectName
	    $http.get(url).success(function(result){
	        tasks = []
	        for(i=0;i<result["tasks"].length;i++){
	            task = result["tasks"][i]
	            task = JSON.parse(task)
	            tasks.push(task)
	        }
	        vm.tasks = tasks
	
	    });
	}


    function create_task(){   
        console.log("create task")   
        url = "algo_task/addTask";
        url = url + "?project="+vm.projectName+"&task="+vm.c_taskname+"&desc="+vm.c_taskdesc

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
        flag = confirm('是否要删除任务'+task.task_name)
        if (!flag){
            return
        }
        url = "algo_task/delTask";
        url = url +"?project="+vm.projectName+"&task="+task.task_name

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
    

    function searchByTime(){
        url = "algo_task/searchByTime";
        url = url +"?project="+vm.projectName+"&beginTime="+vm.beginTime+"&endTime="+vm.endTime

		$http.get(url).success(function(result) {  
		   tasks = []
	        for(i=0;i<result["tasks"].length;i++){
	            task = result["tasks"][i]
	            task = JSON.parse(task)
	            tasks.push(task)
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
        
        url = "algo_task/searchByName";
        url = url +"?project="+vm.projectName+"&task_name="+vm.s_taskname

		$http.get(url).success(function(result) {  
		   tasks = []
	        for(i=0;i<result["tasks"].length;i++){
	            task = result["tasks"][i]
	            task = JSON.parse(task)
	            tasks.push(task)
	        }
           if(tasks.length>0){
               vm.tasks = tasks
           }
           else{
               alert("没有找到符合条件的任务")
           }
		});
    }
    
    function edit_task(index){
        task = vm.tasks[index]
        vm.cur_task = task
    }
    
    function edit_task_submit(){
    	 console.log("create task")   
         url = "algo_task/editTask";
         url = url + "?project="+vm.projectName+"&task_name="+vm.cur_task.task_name+"&desc="+vm.e_taskdesc

 		$http.get(url).success(function(result) {  
             console.log(result);
             if(result.flag){
                 alert("修改成功")
                 get_all()
             }
             else{
                 alert("修改失败")
             }
 		});
    }
}