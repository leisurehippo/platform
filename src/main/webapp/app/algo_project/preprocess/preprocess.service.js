
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .service('PreprocessService', PreprocessService);

    PreprocessService.$inject = ['$resource'];

    function PreprocessService($resource){    
    	this.preview = $resource('preprocess/preview', {}, {
            'get': {method: 'GET', param:{},isArray: false
            }
        });
    	
    	this.transform = $resource('preprocess/transform', {}, {
            'get': {method: 'GET', param:{},isArray: false
            }
        });
 
    }
})();
