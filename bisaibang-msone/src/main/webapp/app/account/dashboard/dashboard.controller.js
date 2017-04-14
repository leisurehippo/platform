(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = [];

    function DashboardController () {
        var vm = this;
        vm.selectedTab = 'index';
        vm.selectedVideoTag = null;
    }
})();
