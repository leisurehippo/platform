(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'AccountCurrent', 'JhiLanguageService', '$translate'];

    function SettingsController(Principal, Auth, AccountCurrent, JhiLanguageService, $translate) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.gotoBlizzard = gotoBlizzard;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                nickName: account.nickName,
                login: account.login
            };
        };

        Principal.identity().then(function (account) {
            vm.settingsAccount = copyAccount(account);
            vm.showNickName = typeof vm.settingsAccount.nickName == 'string' && vm.settingsAccount.nickName.length > 0;
        });

        function gotoBlizzard() {
            var uri = 'https://www.battlenet.com.cn/oauth/authorize';
            var params = ['client_id=banmchv2ucnwzvst64fj6hhbj96xqnzz',
                'redirect_uri=https://ow.bisaibang.com/api/ms-task/blizzard/callback',
                'response_type=code'];
            AccountCurrent.get(function (result) {
                var userId = result.id;
                params.push('state=' + userId);
                location.href = uri + '?' + params.join('&');
            });
        }

        function save() {
            Auth.updateAccount(vm.settingsAccount).then(function () {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function (account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function (current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();
