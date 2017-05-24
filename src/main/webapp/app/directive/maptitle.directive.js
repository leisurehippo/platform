/**
 * Created by gsy on 2016/11/8.
 */
angular
    .module('jhipsterSampleApplicationApp')
    .directive('maptitle', maptitle);

function maptitle() {
    var directive = {
        restrict: 'E',
        link: linkFunc,
        replace: true
    };
    return directive;
    function linkFunc() {
        var width = 1100;
        var height = 100;
        var svg = d3.select("#title")
            .append("svg")
            .attr("width", width)		//设定宽度
            .attr("height", height);	//设定高度

        svg.append("line")
            .attr("x1",0)
            .attr("y1",35)
            .attr("x2",400)
            .attr("y2",35)
            .attr("stroke","#2e2e2f");

        svg.append("line")
            .attr("x1",700)
            .attr("y1",35)
            .attr("x2",1100)
            .attr("y2",35)
            .attr("stroke","#2e2e2f");

        svg.append("line")
            .attr("x1",400)
            .attr("y1",35)
            .attr("x2",430)
            .attr("y2",20)
            .attr("stroke","#2e2e2f");

        svg.append("line")
            .attr("x1",400)
            .attr("y1",50)
            .attr("x2",430)
            .attr("y2",65)
            .attr("stroke","#2e2e2f");

        svg.append("line")
            .attr("x1",700)
            .attr("y1",35)
            .attr("x2",670)
            .attr("y2",20)
            .attr("stroke","#2e2e2f");

        svg.append("line")
            .attr("x1",700)
            .attr("y1",50)
            .attr("x2",670)
            .attr("y2",65)
            .attr("stroke","#2e2e2f");


        svg.append("line")
            .attr("x1",0)
            .attr("y1",50)
            .attr("x2",400)
            .attr("y2",50)
            .attr("stroke","#2e2e2f");

        svg.append("line")
            .attr("x1",700)
            .attr("y1",50)
            .attr("x2",1100)
            .attr("y2",50)
            .attr("stroke","#2e2e2f");

        svg.append("text")
            .attr("x",430)
            .attr("y",50)
            .text("山东电网数据展示")
            .attr("fill","#2e2e2f")
            .attr("font-size", "30px");

    }
}
