(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('PersonController', PersonController);

    PersonController.$inject = ['$state', '$rootScope', 'Auth', 'Principal', 'AccountCurrent', 'RegisterService', 'GetMyPlayersInfo', 'AvatarUploadService', 'QiniuUploadAvatar',
                                'toaster', 'SweetAlert', 'UpdateRegistrationTeamPlayer', 'CheckBattleId'];

    function PersonController ($state, $rootScope, Auth, Principal, AccountCurrent, RegisterService, GetMyPlayersInfo, AvatarUploadService, QiniuUploadAvatar, toaster, SweetAlert,
                               UpdateRegistrationTeamPlayer, CheckBattleId) {
        var vm = this;
        vm.bindNickName = bindNickName;
        vm.changeAvatar = changeAvatar;
        vm.isLogIn = false;
        vm.isBindNickName = false;
        vm.isCaptain = undefined;
        vm.showAddPlayerCard = false;
        vm.openAddPlayerCard = openAddPlayerCard;
        vm.closeAddPlayerCard = closeAddPlayerCard;
        vm.submitAddPlayer = submit;
        vm.addPlayerInfo = {};

        vm.regPhone = regPhone;
        vm.regIDCard = regIDCard;
        vm.regEmail = regEmail;

        Principal.identity().then(function(account) {
            if (account == null) {
                RegisterService.open('signin', function success() {
                    $state.reload();
                }, function fail() {

                });
            } else {
                vm.isLogIn = true;
                AccountCurrent.get({},{},function (res) {
                    vm.personInfo = res;
                    vm.isBindNickName = typeof vm.personInfo.nickName == 'string' && vm.personInfo.nickName.length > 0;
                });
                GetMyPlayersInfo.get({},{},function onSuccess(res) {
                    vm.teamInfo = res.data;
                    vm.teamInfo.teamMate = JSON.parse(vm.teamInfo.teamMate);
                    vm.isCaptain = account.nickName && account.nickName === vm.teamInfo.idGame;
                },function onError() {

                })
            }
        });

        function bindNickName() {
            gotoBlizzard()
        }

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

        function changeAvatar() {
            AvatarUploadService.open("ArticleImage", {aspectRatio: 1, compress: true, width:80, isAvatar: true},
                function (result) {
                    if (result) {
                        if(result.indexOf('http://omhemfx8a.bkt.clouddn.com') == -1) vm.thumbnailUrl = 'http://omhemfx8a.bkt.clouddn.com/' + result;
                            else vm.thumbnailUrl = result;
                        QiniuUploadAvatar.save({}, vm.thumbnailUrl, function success(success) {
                            vm.personInfo.avatarUrl = vm.thumbnailUrl;
                            $rootScope.globalVariable.avatarUrl = vm.thumbnailUrl;
                            toaster.pop('success', " ", '修改成功');
                        }, function error(error) {
                            toaster.pop('success', " ", '修改失败');
                        });
                    }
                }, function () {
                    toaster.pop('error', " ", '发生虾米事情了?上传未成功');
                });
        }

        function openAddPlayerCard() {
            vm.showAddPlayerCard = true;
        }

        function closeAddPlayerCard() {
            vm.showAddPlayerCard = false;
        }

        function submit(){
            var submitStr = '';

            if(!regIDCard(vm.addPlayerInfo.idCard)) submitStr += '替补身份证有误；';
            if(!regPhone(vm.addPlayerInfo.phone)) submitStr += '替补手机号有误；';
            if(!regEmail(vm.addPlayerInfo.email)) submitStr += '替补email有误；';
            if(!vm.addPlayerInfo.name) submitStr += '替补姓名为空；';
            if(!vm.addPlayerInfo.idGame) submitStr += '替补战网ID为空；';

            if(memberRepeat()) submitStr += "不允许有重复队员，请检查所填信息是否与现有队员重复；";

            if(submitStr){
                toaster.pop('error', " ", submitStr);
                return;
            }

            // 给后端的数据
            var post = JSON.parse(JSON.stringify(vm.teamInfo));
            post.teamMate.push(vm.addPlayerInfo);
            post.teamMate = JSON.stringify(post.teamMate);

            Principal.identity().then(function(account) {
                vm.account = account;
                if (account == null) {
                    RegisterService.open('signin', function success() {
                        $state.reload();
                    }, function fail() { });
                } else {
                    SweetAlert.swal({
                            title: "是否确定添加替补人员？",
                            text: "添加之后不能修改人员信息，并且最多只能有2名替补。",
                            type: "warning",
                            showCancelButton: true,
                            backgroundColor: "#292e3a",
                            confirmButtonColor: "#cb6228",
                            confirmButtonText: "确认报名",
                            //cancelButtonColor: "#2a2e39",
                            cancelButtonText: "取消"
                        },
                        function (isConfirm) {
                            if (isConfirm) {
                                CheckBattleId.get({battleid:vm.addPlayerInfo.idGame},{}, function success(res) {
                                    if(res.message == '战网id已被占用') {
                                        UpdateRegistrationTeamPlayer.update({id:post.id},post,function (res) {
                                            toaster.pop('success', " ", res.message);
                                            $state.reload();
                                        }, function (response) {
                                            toaster.pop('error', " ", "当前无法添加");
                                        });
                                    } else {
                                        toaster.pop('error', " ", "所填的战网id不存在或未绑定");
                                    }
                                }, function fail() {});
                            }
                        });
                }
            });
        }

        // 验证手机号
        function regPhone(phone){
            return (/^1(3|4|5|7|8)\d{9}$/.test(phone))
        }
        // 验证身份证号
        function regIDCard(idCard){
            return (/^\d{17}([0-9]|X|x)$/.test(idCard))
        }
        // 验证邮箱
        function regEmail(email){
            return (/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email))
        }

        function memberRepeat(){
            var teamMate = vm.teamInfo.teamMate;
            var len = teamMate.length;
            var testKey = ['idCard','idGame'];//要判重的字段

            for(var i=0; i<testKey.length; i++){
                var key=testKey[i];
                if(vm.addPlayerInfo[key] == vm.teamInfo[key]) return true;//与队长判重
                for (var j = 0; j < len; j++) {
                    if(vm.addPlayerInfo[key] == teamMate[j][key]) return true;//与队员判重
                }
            }
            return false;
        }
    }
})();
