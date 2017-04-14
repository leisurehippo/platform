/**
 * Created by arslan on 9/22/16.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('RegisterCountDown', RegisterCountDown);

    RegisterCountDown.$inject = [];
    var count = 0;

    function RegisterCountDown(){
        var service = {
            get: get,
            set: set
        };
        return service;

        function get(){
            return count;
        }
        function set(value){
            count = value;
        }
    }
})();
