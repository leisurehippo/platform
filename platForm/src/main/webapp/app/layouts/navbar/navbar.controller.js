(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService',
        '$rootScope', '$timeout'];

    function NavbarController ($state, Auth, Principal, ProfileService,
                               $rootScope, $timeout) {
        var vm = this;
        vm.$state = $state;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.isHomePage = vm.$state.$current.name == 'home';

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;

        $rootScope.globalVariable = {};
        $rootScope.globalVariable.nickName = null;
        $rootScope.globalVariable.id = -1;
        $rootScope.globalVariable.refreshBracket = false;
        $rootScope.globalVariable.avatarUrl = null;
        $rootScope.globalVariable.tsportName = null;


        //重刷页面显示昵称
        $timeout(function () {
            //获取用户个人信息
            vm.dataIsReady = true;
        });


        $rootScope.$watch('globalVariable.avatarUrl', function () {
            vm.accountAvatar = $rootScope.globalVariable.avatarUrl;
        });



        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
