
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .service('FileDownload', FileDownload);

    FileDownload.$inject = ['$resource'];

    function FileDownload($resource){    
    	this.download = $resource('algo_task/download', {}, {
            'get': {method: 'GET', param:{}
            }
        });   	
 
    }
})();
