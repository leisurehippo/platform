/**
 * Created by arslan on 1/31/17.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardIndexController', DashboardIndexController);

    DashboardIndexController.$inject = ['$scope'];

    function DashboardIndexController ($scope) {
        var vm = this;
        vm.title = '欢迎使用仪表盘';
    }
})();
