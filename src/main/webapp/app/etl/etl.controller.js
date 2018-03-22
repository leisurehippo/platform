angular
    .module('jhipsterSampleApplicationApp')
    .controller('ETLController',ETLController);

ETLController.$inject = ['$scope', '$http', 'ETLService'];

function ETLController($scope, $http, ETLService){
	var vm = this;
	
	vm.i_createDBConn = i_createDBConn;
	function i_createDBConn(){
		console.log("create conn");
		console.log(vm.i_dbType);
		data = {
			"type":vm.i_dbType,
			"ip":vm.i_ip,
			"port":vm.i_port,
			"database":vm.i_database,
			"username":vm.i_username,
			"password":vm.i_password
		};
		url = "etl/tables";

		$http.post(url, data).success(function(result) {  
			if (result.length <= 0){
				console.log('fail')
				alert('the connection infos are not correct, please check it carefully')
			}
			else{
				alert('success!')
			}
		    console.log(result);
		    vm.i_tables = result;
		    console.log(vm.i_tables);
		
		});
	}
	
	$scope.$watch('vm.i_table', function (oldValue,newValue) {
		console.log(vm.i_table);
		if (vm.i_table!=null){
	        data = {
	        		"type":vm.i_dbType,
	    			"ip":vm.i_ip,
	    			"port":vm.i_port,
	    			"database":vm.i_database,
	    			"username":vm.i_username,
	    			"password":vm.i_password,
	    			"tableName":vm.i_table
	        };
	        url = "etl/columns";
	        $http.post(url, data).success(function(result) {  
			    console.log(result);
			    vm.i_columns = result;	
			    vm.i_selected = new Set();
			});
		}
    });
	
	vm.i_selected = new Set();
	vm.o_selected = new Set();
	vm.i_selected_list = [];
	vm.o_selected_list = [];
	var updateSelected = function(type,action,name){
		if(action =='add'){
			if(type=="i"){
				vm.i_selected.add(name);
			}
			else{
				vm.o_selected.add(name);
			}
		}
		else{
			if(type=="i"){
				vm.i_selected.delete(name);
			}
			else{
				vm.o_selected.delete(name);
			}
		}
		vm.i_selected_list = Array.from(vm.i_selected);
		vm.o_selected_list = Array.from(vm.o_selected);
	}
	vm.updateSelection = function($event, type, name){
		var checkbox = $event.target;
		var action = (checkbox.checked?'add':'remove');
		updateSelected(type, action, name);
	}
	vm.isSelected = function(type, name){
		if(type =='i'){
			return vm.i_selected.has(name);
		}
		else{
			return vm.o_selected.has(name);
		}
	}
	
	
	
	vm.o_createDBConn = o_createDBConn;
	function o_createDBConn(){
		console.log("create conn");
		console.log(vm.o_dbType);
		data = {
			"type":vm.o_dbType,
			"ip":vm.o_ip,
			"port":vm.o_port,
			"database":vm.o_database,
			"username":vm.o_username,
			"password":vm.o_password
		};
		url = "etl/tables";

		$http.post(url, data).success(function(result) { 
			if (result.length <= 0){
				console.log('fail')
				alert('the connection infos are not correct, please check it carefully')
			}
			else{
				alert('success!')
			}
		    console.log(result);
		    vm.o_tables = result;
		    console.log(vm.o_tables);
		
		});
	}
	
	$scope.$watch('vm.o_table', function (oldValue,newValue) {
		console.log(vm.o_table);
		if(vm.o_table!=null){
        data = {
        		"type":vm.o_dbType,
    			"ip":vm.o_ip,
    			"port":vm.o_port,
    			"database":vm.o_database,
    			"username":vm.o_username,
    			"password":vm.o_password,
    			"tableName":vm.o_table
        };
        url = "etl/columns";
        $http.post(url, data).success(function(result) {  
		    console.log(result);
		    vm.o_columns = result;	
		    vm.o_selected = new Set();
		});
		}
    });
	vm.runable = true;
	vm.runJob = function(){
		alert("the job may take some time to be executed, please wait a minute");
		vm.runable = false;
		reader = {
				"type":vm.i_dbType,
    			"ip":vm.i_ip,
    			"port":vm.i_port,
    			"database":vm.i_database,
    			"username":vm.i_username,
    			"password":vm.i_password,
    			"table":vm.i_table,
    			"column":Array.from(vm.i_selected)
				
		};
		writer = {
				"type":vm.o_dbType,
    			"ip":vm.o_ip,
    			"port":vm.o_port,
    			"database":vm.o_database,
    			"username":vm.o_username,
    			"password":vm.o_password,
    			"table":vm.o_table,
    			"column":Array.from(vm.o_selected)	
		};
		transform = vm.transformItems;
		console.log(vm.transformItems)
		data = {
				"writer":writer,
				"reader":reader,
				"transform":transform
		};
		url = "etl/runJob";
		 $http.post(url, data).success(function(result) {  
			    console.log(result);
			    vm.runable = true;
			    var flag = result['message']
			    if(flag=='fail'){
			    	alert("failed! please check the input value");
			    }else{
			    	alert("the job has been executed success!congratulation!");
			    }
			    
			    
			});
	}

	vm.transformItems = [];
	
	vm.rmTransItem = function(){
		vm.transformItems = [];
	};
	
	//感觉有点冗余，待优化...。如果有后面的同学看到这段代码，只能说抱歉
	function validIndex(index){
		if (index>=vm.i_selected_list.length){
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