(function() {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;

        vm.register = register;

        vm.currentTag = 1;
        vm.selectTag = selectTag;
        vm.currentBracketTag = 1;
        vm.selectBracketTag = selectBracketTag;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function selectTag(num) {
            vm.currentTag = num;
        }

        function selectBracketTag(num) {
            vm.currentBracketTag = num;
        }
    }
})();
