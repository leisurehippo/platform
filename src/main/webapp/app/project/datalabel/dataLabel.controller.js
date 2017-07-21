/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .controller('DataLabelController',DataLabelController);

DataLabelController.$inject = ['$scope', '$http', '$state', 'dataLabelservice','Submitservice'];
function DataLabelController($scope, $http, $state, dataLabelservice,Submitservice) {
    var vm = this;
    vm.run = run;
    vm.change=change;
    vm.submit=submit;
    $scope.keywords=" ";
    $scope.selectdb=false;
    $scope.dbname=" ";
    $scope.selectsina=false;
    $scope.selecttime=false;
    $scope.timestart=" ";
    $scope.timeend=" ";
    $scope.writedb=" ";
    $scope.oldlabel=" ";
    $scope.newlabel=" ";
    $scope.selectoldlabel=false;
    vm.text="";


    var perpage=10;
    var element = $('#page');
    var totalPages=2;

     $('#myModal').modal({
                keyboard: true,
                backdrop: "static",
                show:false
                })
    function run() {

        dataLabelservice.get({keywords:$scope.keywords,selectdb:$scope.selectdb,dbname:$scope.dbname,selectsina:$scope.selectsina,selecttime:$scope.selecttime,timestart
        :$scope.timestart,timeend:$scope.timeend,selectoldlabel:$scope.selectoldlabel,oldlabel:$scope.oldlabel,newlabel:$scope.newlabel,writedb:$scope.writedb}, function success(result) {
            console.log(result);
            vm.dataset=result.dataset;
            vm.len = result.dataset.length;
            $scope.choose = new Array(vm.len);
            for(x in result.dataset){
                var id= result.dataset[x].since_id;
                $scope.choose[id]=false;
            }
            totalPages=Math.ceil(vm.len / perpage);


            var options = {
                                 bootstrapMajorVersion:3,
                                 currentPage: 1,
                                 numberOfPages: perpage,
                                 totalPages:totalPages,
                                 onPageClicked: function(e,originalEvent,type,page)
                                 {
                //                      $('#123').text("Page item clicked page: "+page);
                                      var start=(page-1)*perpage;
                                      var end = page*perpage;
                                      if(end>vm.len)
                                      {
                                        end=vm.len;
                                      }
                //                      .items=new Array(end-start);
                                        vm.items=[];
                                      for(var i =start;i<end;i++)
                                      {
                                            vm.items[i-start]=vm.dataset[i];

                                      }
                                      console.log(vm.items);

//                                      document.getElementById("get_content").innerHTML="";
                                      vm.text="";
                                      for(i in vm.items)
                                      {

                                        vm.text+="<div style=\"margin-top:10px;margin-bottom:10px;position: relative;display: block;\" ><input type=\"checkbox\" ng-click=\"vm.change()\">&nbsp"+vm.items[i].weibo_content+"</div>";


                                      }

                                 }
                            }
                 element.bootstrapPaginator(options);
                 options.onPageClicked("","","",1);




        },function failure() {

        });
    }

    function submit(){
        var postdata=new Array();
        for(var i in $scope.items)
        {

            var item=$scope.items[i];
            if(document.getElementById(item.since_id).checked)
            {
                   postdata.push(item.since_id) ;
            }
        }

        Submitservice.post({labelresult:postdata}, function success(result) {
         console.log(result);
         },function () {});

    }


    function change(){
        alert("1234");
        $scope.choose[id]=~$scope.choose[id];

    }

}
