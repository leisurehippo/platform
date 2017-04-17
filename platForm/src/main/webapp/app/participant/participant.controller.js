(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('ParticipantController', ParticipantController);

    ParticipantController.$inject = ['$state', 'RegistrationsEnrollTeamPlayer', 'toaster','CheckTeamName', 'SweetAlert', 'Principal',
                                    'RegisterService', 'GetMyPlayersInfo', 'AccountCurrent'];

    function ParticipantController($state, RegistrationsEnrollTeamPlayer, toaster, CheckTeamName, SweetAlert, Principal,
                                   RegisterService, GetMyPlayersInfo, AccountCurrent) {
        var vm = this;

        vm.idCards = [];
        vm.idGames = [];
        vm.phones = [];
        vm.emails = [];

        vm.submit = submit;

        vm.regPhone = regPhone;
        vm.regIDCard = regIDCard;
        vm.regEmail = regEmail;

        vm.checkTeamName =checkTeamName;
        vm.isTeamNameSame = true;

        vm.hasNickName = false;

        // 队长信息有误
        vm.teamLeaderError = false;
        // 队员信息有误
        vm.teamMateError = false;
        vm.dataIsReady = false;


        vm.sex = [
            {value: 'male', text: '男'},
            {value: 'female', text: '女'}
        ];

        vm.teamName = '';
        vm.teamLeader = {name: '', phone: '', sex:'male', idCard: '', idGame: '', email: ''};


        AccountCurrent.get(function (response) {
            var result = response;
            if(result.nickName){
                vm.hasNickName = true;
                vm.teamLeader.idGame = result.nickName;
            }
            vm.dataIsReady = true;
        });
        // 限制队名，中文8个字，英文16个字
        String.prototype.gblen = function() {
            var len = 0;
            for (var i=0; i<this.length; i++) {
                if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {
                    len += 2;
                } else {
                    len ++;
                }
            }
            return len;
        };

        GetMyPlayersInfo.get({},{},function onSuccess(res) {
            console.log(res);
            vm.isTeam = res.data;
            vm.canParticipant = false;
        },function onError(res) {
            console.log(res);
            vm.canParticipant = true;
        });





        var player1 = {name: '', phone: '', idCard: '', idGame: '', email: ''};
        var player2 = angular.copy(player1);
        var player3 = angular.copy(player1);
        var player4 = angular.copy(player1);
        var player5 = angular.copy(player1);

        // 队员5人
        vm.players = [player1, player2, player3, player4, player5];

        var reservePlayer1 = angular.copy(player1);
        var reservePlayer2 = angular.copy(player1);

        // 替补2人
        vm.reservePlayers = [reservePlayer1, reservePlayer2];

        // 验证手机号
        function regPhone(phone){
            return (/^1(3|4|5|7|8)\d{9}$/.test(phone));
        }
        // 验证身份证号
        function regIDCard(idCard){
            return (/^\d{17}([0-9]|X|x)$/.test(idCard));
        }
        // 验证邮箱
        function regEmail(email){
            return (/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email));
        }

        function isObjectValueEqual(a, b) {

            var aProps = Object.getOwnPropertyNames(a);
            var bProps = Object.getOwnPropertyNames(b);
            if (aProps.length != bProps.length) {
                return false;
            }
            for (var i = 0; i < aProps.length; i++) {
                var propName = aProps[i];
                if(Object.prototype.toString(a[propName]) == '[Object Object]'||Object.prototype.toString(b[propName]) == '[Object Object]'){
                    isObjectValueEqual(a[propName],b[propName])
                }
                if (a[propName] !== b[propName]) {
                    return false;
                }
            }
            return true;
        }

        function arrRepeat(arr){
            // var arrStr = JSON.stringify(arr);
            // for (var i = 0; i < arr.length; i++) {
            //     if (arrStr.indexOf(arr[i]) != arrStr.lastIndexOf(arr[i])){
            //         return true;
            //     }
            // }
            // return false;
            var len = arr.length;
            for (var i = 0; i < len; i++) {
                for(var j = i+1; j < len-1-i; j++){
                    if(arr[i] == arr[j]){
                        return true;
                    }
                }
            }
            return false;

            // var idx =false;
            // for(var i=0,len=arr.length;i<len;i++){
            //     for(var j=0;j<i;j++){
            //         idx = isObjectValueEqual(arr[i],arr[j])
            //     }
            // }
            // return idx;
        }


        function checkTeamName(){
            CheckTeamName.get({teamname:vm.teamName}, function (response) {
                if (response.message == 'teamName被占用'){
                    vm.isTeamNameSame = true;
                    vm.isCheck = true;
                } else {
                    vm.isTeamNameSame = false;
                    vm.isCheck = true;
                }
            });
        }

        function submit(){

            vm.idCards = [vm.teamLeader.idCard];
            vm.idGames = [vm.teamLeader.idGame];
            vm.phones = [vm.teamLeader.phone];
            vm.emails = [vm.teamLeader.email];

            var submitStr = '';
            for(var i in vm.teamLeader){
                switch (i){
                    case 'idCard':
                        !regIDCard(vm.teamLeader[i]) ? submitStr += '队长身份证有误；': '';
                        break;
                    case 'phone':
                        // console.log(typeof i);
                        // console.log(vm.teamLeader[i]);
                        !regPhone(vm.teamLeader[i]) ? submitStr += '队长手机号有误；': '';

                        break;
                    case 'email':
                        !regEmail(vm.teamLeader[i]) ? submitStr += '队长email有误；': '';
                        break;
                }
            }
            vm.players.forEach(function (item, index) {
                vm.idCards.push(item.idCard);
                vm.idGames.push(item.idGame);
                vm.phones.push(item.phone);
                vm.emails.push(item.email);
                var idx = index+1;
                for(var i in item){
                    switch (i){
                        case 'idCard':
                            !regIDCard(item[i]) ? submitStr += "队员"+idx+"身份证有误；": '';
                            break;
                        case 'phone':
                            !regPhone(item[i]) ? submitStr += "队员"+idx+"手机号有误；": '';
                            break;
                        case 'email':
                            !regEmail(item[i]) ? submitStr += "队员"+idx+"email有误；": '';
                            break;
                    }
                }
            });

            // 判断替补队员是否填写，如果填写 加到正式队员之后传给后端
            vm.reservePlayers.forEach(function(item, index){
                if(item.idCard != '' && item.phone != ''&& item.email != ''){
                    vm.players.push(item);
                }
            });

            // 组  给后端的数据
            var post = {};
            post.registration = {
                "captainNickName": vm.teamLeader.idGame,
                "email": vm.teamLeader.email,
                "idCard": vm.teamLeader.idCard,
                "idGame": vm.teamLeader.idGame,
                "name": vm.teamLeader.name,
                "phone": vm.teamLeader.phone,
                "sex": vm.teamLeader.sex,
                "teamMate": JSON.stringify(vm.players),
                "teamName": vm.teamName,
                "type": "online"
            };

            var leader = {
                state: "captain",
                registration: post.registration,
                teamName : vm.teamName,
                battleTag: vm.teamLeader.idGame
            };
            post.teamPlayers = [leader];
            vm.players.forEach(function(item, index){
                post.teamPlayers[index+1] = {};
                post.teamPlayers[index+1].registration = post.registration;
                post.teamPlayers[index+1].battleTag= item.idGame;
                post.teamPlayers[index+1].teamName = vm.teamName;
                post.teamPlayers[index+1].state= "normal";
            });

            Principal.identity().then(function(account) {
                vm.account = account;
                // console.log(vm.account);
                if (account == null) {
                    // console.log('account == null');
                    RegisterService.open('signin', function success() {
                        $state.reload();
                    }, function fail() { });
                } else {
                    // console.log(vm.idCards);
                    // console.log(vm.idGames);
                    // console.log(vm.phones);
                    // console.log(vm.emails);
                    //
                    // console.log(arrRepeat(vm.idCards));
                    // console.log(arrRepeat(vm.idGames));
                    // console.log(arrRepeat(vm.phones));
                    // console.log(arrRepeat(vm.emails));
                    if(arrRepeat(vm.idCards) || arrRepeat(vm.idGames) || arrRepeat(vm.phones) || arrRepeat(vm.emails)){
                        toaster.pop('error', " ", "队员不允许重复");
                    }else{
                        if(submitStr === ''){
                            SweetAlert.swal({
                                title: "是否确认报名？",
                                text: "成功报名之后不能修改队伍信息，不过可以增加替补人员。",
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
                                    RegistrationsEnrollTeamPlayer.post(post, function (res) {
                                        // console.log(res);
                                        if(res.message == '用户已经报名'){
                                            toaster.pop('success', " ", "当前用户已经报名，请勿重复报名");
                                        }else if(res.message.indexOf('战网id不存在') !== -1){
                                            console.log(res);
                                            toaster.pop('error', " ", res.message);
                                        }else{
                                            toaster.pop('success', " ", "报名成功");
                                            toaster.pop('success', " ", "邮件会在72小时之内发送，请注意查收");
                                            $state.reload()
                                        }
                                    }, function (response) {
                                        toaster.pop('error', " ", "当前无法报名");
                                        // console.log(response);
                                    });
                                }
                            });
                        } else {
                            SweetAlert.swal({
                                title: "您的信息有误",
                                text: submitStr,
                                type: "warning",
                                showCancelButton: true,
                                backgroundColor: "#292e3a",
                                confirmButtonColor: "#cb6228",
                                confirmButtonText: "返回修改",
                                //cancelButtonColor: "#2a2e39",
                                cancelButtonText: "放弃"
                            },
                            function (isConfirm) {
                                if (isConfirm) {
                                }
                            });
                        }
                    }
                }
            });
        }

    }
})();
