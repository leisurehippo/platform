function bigDiv(obj,num)
{
    obj.getElementsByTagName("img")[0].src="img/icon"+num+"-big.png";
    obj.style="background-color: #EDEDED";
    obj.getElementsByTagName("div")[0].style="color: #292929";
}
function normalDiv(obj,num)
{
    obj.getElementsByTagName("img")[0].src="img/icon"+num+".png";
    obj.style="";
    obj.getElementsByTagName("div")[0].style="color: #8a8a8a";
}
