(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('RegisterController', RegisterController);

    //修改处 注入$interval服务(验证码倒计时)
    RegisterController.$inject = ['$rootScope', '$translate', '$timeout', '$interval', 'Auth', '$state',
        '$filter', 'Principal', 'RegisterCountDown', 'SmsCodeSend', '$uibModalInstance', 'toaster', 'operation', 'onSuccess', 'onFail', 'AccountCurrent'];

    function RegisterController($rootScope, $translate, $timeout, $interval, Auth, $state,
                                $filter, Principal, RegisterCountDown, SmsCodeSend, $uibModalInstance, toaster, operation, onSuccess, onFail, AccountCurrent) {
        var vm = this;

        // console.log(operation);

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorLoginExists = false;
        vm.errorEmailExists = false;
        vm.errorNameExists = false;
        vm.errorPhoneExists = false;

        vm.register = register;
        vm.getCode = getCode;
        vm.registerAccount = {};
        vm.success = null;
        vm.fluentRequest = false;
        vm.userRegisted = false;
        vm.userNickNameExisted = false;
        vm.ALIDAYU_Error = false;
        vm.test = null;
        vm.cancel = cancel;
        vm.getcodeIsdisable = false;
        vm.submitIsAble = false;
        vm.btntime = "获得验证码";

        vm.authenticationError = false;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
        vm.rememberMe = true;
        vm.requestResetPassword = requestResetPassword;
        vm.username = null;
        vm.phone = null;

        vm.toggle_state = operation;
        vm.changeState = changeState;

        init();

        function init() {
            if (vm.toggle_state === 'signup') {
                $timeout(function () {
                    angular.element('#phone').focus();
                });
                if (RegisterCountDown.get() > 0) {
                    startCountDown();
                }
            } else {
                $timeout(function () {
                    angular.element('#username').focus();
                });
            }
        }

        function register() {
            vm.registerAccount.email = vm.registerAccount.login + "@bisaibang.com";
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                // vm.registerAccount.nickName = vm.registerAccount.login;
                // vm.registerAccount.login = vm.registerAccount.phone;
                vm.registerAccount.phone = vm.registerAccount.login;
                vm.registerAccount.nickName = 'noNickName';
                vm.doNotMatch = null;
                vm.error = null;
                vm.alert = null;

                //console.log(JSON.stringify(vm.registerAccount));

                Auth.createAccount(vm.registerAccount).then(function (response) {
                    // console.log(response);
                    // console.log(response.message);
                    if (!vm.submitIsAble) {
                        vm.codeMessage = "您输入的验证码不正确";
                        // toaster.pop('error', " ", "输入的验证码有误");
                    }
                    if (response.message === 'login already in use') {
                        vm.success = null;
                        vm.phoneAlert = "该手机号已注册，请检查";
                        // toaster.pop('error', " ", "该手机号已注册，请检查");
                        onFail();
                        // } else if (response.message === 'e-mail address already in use') {
                        //     vm.success = null;
                        //     vm.errorEmailExists = true;
                        //     onFail();
                        // } else if (response.message === 'name already in use') {
                        //     vm.success = null;
                        //     vm.errorNameExists = true;
                        //     onFail();
                        // } else if (response.message === 'phone already in use') {
                        //     vm.success = null;
                        //     vm.errorPhoneExists = true;
                        //     onFail();
                    } else if (response.message === 'nickname already in use') {
                        vm.success = null;
                        vm.nicknameAlert = "该昵称已被占用";
                        // toaster.pop('error', " ", "该昵称已被占用");
                        onFail();
                    } else if (response.message === '验证码错误') {
                        vm.success = null;
                        vm.codeAlert = "您输入的验证码不正确";
                        // toaster.pop('error', " ", "输入的验证码有误");
                        onFail();
                    } else if (response.message === '创建成功' && vm.submitIsAble) {
                        vm.success = 'OK';
                        vm.error = null;
                        // toaster.pop('success', " ", "注册成功，请登录");
                        vm.username = vm.registerAccount.login;
                        vm.password = vm.registerAccount.password;
                        vm.rememberMe = false;
                        login();
                    }

                });
            }
        }

        function cancel() {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.registerAccount = {};
            vm.authenticationError = false;
            $uibModalInstance.dismiss('cancel');
        }

        function getCode() {
            vm.sendSuccess = null;
            vm.getcodeIsdisable = true;
            vm.submitIsAble = true;
            //需要翻译
            //修改处
            // console.log(123);
            RegisterCountDown.set(60);

            if (vm.registerAccount.login) {
                // console.log(456);
                SmsCodeSend.save(vm.registerAccount.login, function success(response) {
                    // console.log(response);
                    vm.sendSuccess = null;
                    if (response.message === '操作成功') {
                        vm.sendSuccess = 'OK';
                        toaster.pop('success', " ", "验证码已发送，请稍等");
                        startCountDown();
                    } else if (response.message === '注册太频繁') {
                        vm.fluentRequest = true;
                        vm.alert = " 短信发送过于频繁,请等待60秒后再试";
                        toaster.pop('error', " ", " 短信发送过于频繁,请等待60秒后再试");
                        vm.getcodeIsdisable = false;
                    } else if (response.message === '该用户已经注册') {
                        vm.userRegisted = true;
                        vm.phoneAlert = "该手机号已注册，请检查";
                        toaster.pop('error', " ", "该手机号已注册");
                        vm.getcodeIsdisable = false;
                    } else {
                        //console.log(response.message);
                        vm.ALIDAYU_Error = true;
                        vm.alert = " 未知错误,可能是手机号有误";
                        toaster.pop('error', " ", " 未知错误,可能是手机号有误");
                        vm.getcodeIsdisable = false;
                    }
                }, function (err) {
                    //console.log(err.message);
                    vm.ALIDAYU_Error = true;
                    vm.alert = " 未知错误,可能是手机号有误";
                    toaster.pop('error', " ", " 未知错误,可能是手机号有误");
                    vm.getcodeIsdisable = false;
                });
            }
        }

        function startCountDown() {
            var countDown = $interval(function () {
                if (document.getElementById('modal-body') === null) {
                    $interval.cancel(countDown);
                }
                if (RegisterCountDown.get() <= 0) {
                    vm.getcodeIsdisable = false;
                    vm.btntime = "发送验证码";
                    $interval.cancel(countDown);
                } else {
                    vm.btntime = "等待" + RegisterCountDown.get() + "秒";
                    RegisterCountDown.set(RegisterCountDown.get() - 1);
                }
            }, 1000);
        }

        function login(event) {
            if (event) {
                // console.log("Login.")
                event.preventDefault();
            } else {
                // console.log("From register to login.");
                $uibModalInstance.dismiss('cancel');
            }
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                if (event) {
                    $uibModalInstance.dismiss('cancel');
                }
                //登录成功后显示昵称
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

                // vm.account = null;
                // Principal.identity().then(function (account) {
                //     vm.account = account;
                //     if (account !== null) {
                //         $rootScope.globalVariable.nickName = account.nickName;
                //         $rootScope.globalVariable.phone = account.phone;
                //         $rootScope.globalVariable.id = account.id;
                //     }
                // });

                toaster.pop('success', " ",
                    $filter('translate')('login.messages.success'));

                vm.authenticationError = false;

                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                    $state.go('home');
                }

                $rootScope.$broadcast('authenticationSuccess');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();

                    $state.go(previousState.name, previousState.params);
                }
                onSuccess();
            }).catch(function () {
                vm.authenticationError = true;
                onFail();
            });
            // $state.go($state.current.name, {}, {reload: true});
            // $state.go($state.current.name, {}, {reload: true});
            // console.log($state.$current, $state.current);
            // console.log("Test.");
        }

        function requestResetPassword() {
            $uibModalInstance.dismiss('cancel');
            $state.go('requestReset');
        }

        function changeState(state) {
            vm.toggle_state = state;
            init();
        }
    }
})();
