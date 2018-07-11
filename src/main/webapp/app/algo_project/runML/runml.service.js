
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .service('RunMLService', RunMLService);

    RunMLService.$inject = ['$resource'];

    function RunMLService($resource){    
    	this.train = $resource('api/train', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
    	
    	this.test = $resource('api/test', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
 
    }
})();
