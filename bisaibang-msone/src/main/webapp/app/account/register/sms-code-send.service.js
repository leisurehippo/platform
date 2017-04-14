/**
 * Created by szzz on 2016/7/6.
 */

(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('SmsCodeSend', SmsCodeSend);

    SmsCodeSend.$inject = ['$resource'];

    function SmsCodeSend($resource) {
        var service = $resource('api/register/send_sms_code', {}, {
            'get': { method: 'GET'},
            'save': { method: 'POST' }
        });
        return service;
    }
})();
