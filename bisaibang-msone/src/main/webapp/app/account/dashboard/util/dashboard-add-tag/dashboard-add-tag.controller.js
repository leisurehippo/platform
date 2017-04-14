/**
 * Created by arslan on 1/31/17.
 */
(function () {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('DashboardAddTagController', DashboardAddTagController);

    DashboardAddTagController.$inject = ['CreateVideoTag', 'toaster', '$state'];

    function DashboardAddTagController(CreateVideoTag, toaster, $state) {
        var vm = this;
        vm.submit = submit;
        vm.thumbnailUploadStatus = '';
        vm.tag = {
            name: ''
        };

        function submit() {
            CreateVideoTag.save(vm.tag, function success(result) {
                toaster.pop('success', " ", '已成功');
                $state.reload();
            }, function error(result) {
                toaster.pop('error', " ", '失败');
            });

        }
    }
})();
