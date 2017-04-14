(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'RegisterService', 'LoginService',
        '$rootScope', '$timeout', 'AccountCurrent'];

    function NavbarController ($state, Auth, Principal, ProfileService, RegisterService, LoginService,
                               $rootScope, $timeout, AccountCurrent) {
        var vm = this;
        vm.$state = $state;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.isHomePage = vm.$state.$current.name == 'home';

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.register = register;
        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.accountPhone = '帐号';

        $rootScope.globalVariable = {};
        $rootScope.globalVariable.nickName = null;
        $rootScope.globalVariable.id = -1;
        $rootScope.globalVariable.refreshBracket = false;
        $rootScope.globalVariable.avatarUrl = null;
        $rootScope.globalVariable.tsportName = null;
        $rootScope.globalVariable.phone = null;


        //重刷页面显示昵称
        $timeout(function () {
            //获取用户个人信息
            AccountCurrent.get(function onSuccess(response) {
                // console.log(response);
                if (response) {
                    $rootScope.globalVariable.nickName = response.nickName;
                    $rootScope.globalVariable.id = response.id;
                    $rootScope.globalVariable.phone = response.phone;
                    $rootScope.globalVariable.avatarUrl = response.avatarUrl;
                } else {
                    vm.isNavbarCollapsed = true;
                }
            }, function error() {

            });
            vm.dataIsReady = true;
        });

        $rootScope.$watch('globalVariable.phone', function () {
            if($rootScope.globalVariable.phone) {
                if($rootScope.globalVariable.nickName) {
                    vm.accountPhone = $rootScope.globalVariable.nickName;
                }else {
                    var fullPhone = $rootScope.globalVariable.phone;
                    vm.accountPhone = $rootScope.globalVariable.phone.replace(fullPhone.substring(3,6), '****');
                }
            }else {
                vm.accountPhone = '帐号';
            }
        });

        $rootScope.$watch('globalVariable.avatarUrl', function () {
            vm.accountAvatar = $rootScope.globalVariable.avatarUrl;
        });

        function register() {
            collapseNavbar();
            RegisterService.open('signup', function success() {
                $state.reload();
            }, function fail() {

            });
            angular.element('.homepage-register-text').blur();
        }

        function login() {
            collapseNavbar();
            // LoginService.open();
            RegisterService.open('signin', function success() {
                $state.reload();
            }, function fail() {

            });
            angular.element('.homepage-login-text').blur();
        }

        function logout() {
            $rootScope.globalVariable = {};
            $rootScope.globalVariable.nickName = null;
            $rootScope.globalVariable.id = -1;
            $rootScope.globalVariable.avatarUrl = null;
            $rootScope.globalVariable.tsportName = null;

            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
