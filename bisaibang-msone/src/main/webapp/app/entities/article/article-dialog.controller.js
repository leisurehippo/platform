(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Article', 'Term', 'User'];

    function ArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Article, Term, User) {
        var vm = this;

        vm.article = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.terms = Term.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.article.id !== null) {
                Article.update(vm.article, onSaveSuccess, onSaveError);
            } else {
                Article.save(vm.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bsbmsoneApp:articleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.editDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
