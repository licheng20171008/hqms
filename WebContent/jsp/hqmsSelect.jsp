<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/laydate/laydate.js"></script>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<title>亚心医院等级评审--明细查询</title>
</head>
<%
String selectName = (String)request.getAttribute("selectName");
String measureName = (String)request.getAttribute("measureName");
String beginDate = (String)request.getAttribute("beginDate");
String endDate = (String)request.getAttribute("endDate");
int mmcurpage = (Integer)request.getAttribute("mmcurpage");
int dtcurpage = (Integer)request.getAttribute("dtcurpage");
List<String> measureList = (List<String>) request.getAttribute("measureList");
List<List<String>> resultList = (List<List<String>>) request.getAttribute("resultList");
List<String> thList = (List<String>)request.getAttribute("thList");
List<List<String>> tdList = (List<List<String>>)request.getAttribute("tdList");
int mmrecordsize = resultList.size();
int mmprepage = 20;
int mmpagesize = mmrecordsize % mmprepage == 0 ? (mmrecordsize / mmprepage) : (mmrecordsize / mmprepage + 1);
int dtrecordsize = tdList.size();
int dtprepage = 20;
int dtpagesize = dtrecordsize % dtprepage == 0 ? (dtrecordsize / dtprepage) : (dtrecordsize / dtprepage + 1);
%>
<body>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<form id="likeselect" action="Init.action" method="post">
<input type="hidden" name="hiddenValue" value="1"/>
<span>模糊查找(FUZZY SEARCH)：</span><input id="selectName" type="text" name="selectName" style="width: 167px; height: 20px" maxlength="50" value="<%=selectName %>"/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="查询(SEARCH)" onclick="buttonSubmit(0)"/>
</form>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<form id="detailSelect" action="Init.action" method="post">
<input type="hidden" name="hiddenValue" value="2"/>
<div><span>Measure Name：</span>
<select id="measureName" name="measureName" style="width: 167px; height: 20px;">
<option value="">--please select after fuzzy search--</option>
<%
for(int i = 0; i < (measureList.size() > 100?100:measureList.size()); i++) {
	%><option value="<%=measureList.get(i) %>"><%=measureList.get(i) %></option><%
}
%>
</select>
</div>
<br/>
<div>
<span>Begin Date ：</span>
<input id="beginDate" name="beginDate" class="laydate-icon-dahong" onclick="laydate()" value="<%=beginDate %>"/>
</div>
<br/>
<div>
<span>End Date ：</span>
<input id="endDate" name="endDate" class="laydate-icon-dahong" onclick="laydate()" value="<%=endDate %>"/>
</div>
<br/>
<div>
<input type="button" value="详细查询(DETAILED INQUIRY)" onclick="buttonSubmit(1)"/>
</div>
</form>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<form id="tableSelect" action="Init.action" method="post">
<input type="hidden" name="hiddenValue" value="3"/>
<input type="hidden" id="measure" name="measure"/>
		<div align="right">
			共&nbsp;<%=mmrecordsize %>&nbsp;条记录&nbsp;&nbsp;&nbsp;每页&nbsp;<%=mmprepage %>&nbsp;条&nbsp;&nbsp;&nbsp;共&nbsp;<%=mmpagesize %>&nbsp;页&nbsp;&nbsp;&nbsp;当前第&nbsp;<%=mmcurpage %>&nbsp;页
			<a <%if(mmcurpage > 1){%>onclick="mmOnPage(1)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >首页</a>
			<a <%if(mmcurpage > 1){ %>onclick="mmOnPage(<%=mmcurpage-1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >上一页</a>
			<a <%if(mmcurpage < mmpagesize && mmpagesize > 1){ %>onclick="mmOnPage(<%=mmcurpage+1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >下一页</a>
			<a <%if(mmpagesize > 1){ %>onclick="mmOnPage(<%=mmpagesize%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >尾页</a>
			<input type="button" onclick="mmOnPage(-1)" value="转到(JUMP)"/> 第 
			<input id="mmTop" class=text style="TEXT-ALIGN: left" size=3 value=""> 页
		</div>
		<br/>
		<table id="mmtable" border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
<tr>
<th>MEASURE</th>
<th>MEASURE_VALUE</th>
</tr>
<%
int mmBeginIndex = (mmcurpage - 1)*mmprepage;
int mmEndIndex = 0;
if (mmcurpage == mmpagesize) {
	mmEndIndex = mmrecordsize - 1;
} else {
	mmEndIndex = mmcurpage*mmprepage - 1;
}
	for (int j = 0; j < mmrecordsize; j++) {
		if (mmBeginIndex <= j && j <= mmEndIndex) {
%>
<tr>
<td><%=resultList.get(j).get(0) %></td>
<td><a style="text-decoration:underline;color:#FF0000;cursor:default;" onclick="detailSubmit(this)"><%=resultList.get(j).get(1) %></a></td>
</tr>
<%}}
%>
</table>
<br/>
<div align="right">
            共&nbsp;<%=mmrecordsize %>&nbsp;条记录&nbsp;&nbsp;&nbsp;每页&nbsp;<%=mmprepage %>&nbsp;条&nbsp;&nbsp;&nbsp;共&nbsp;<%=mmpagesize %>&nbsp;页&nbsp;&nbsp;&nbsp;当前第&nbsp;<%=mmcurpage %>&nbsp;页
            <a <%if(mmcurpage > 1){%>onclick="mmOnPage(1)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >首页</a>
            <a <%if(mmcurpage > 1){ %>onclick="mmOnPage(<%=mmcurpage-1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >上一页</a>
            <a <%if(mmcurpage < mmpagesize && mmpagesize > 1){ %>onclick="mmOnPage(<%=mmcurpage+1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >下一页</a>
            <a <%if(mmpagesize > 1){ %>onclick="mmOnPage(<%=mmpagesize%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >尾页</a>
            <input type="button" onclick="mmOnPage(-2)" value="转到(JUMP)"/> 第 
            <input id="mmDown" class=text style="TEXT-ALIGN: left" size=3 value=""> 页
        </div>
</form>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<form id="pageForm" action="Init.action" method="post">
<input id="submitPage" type="hidden" name="hiddenValue" value="4"/>
<input id="mmcurpage" type="hidden" name="mmcurpage" value="<%=mmcurpage %>"/>
<input id="dtcurpage" type="hidden" name="dtcurpage" value="<%=dtcurpage %>"/>
</form>
<div><div align="center"><marquee width="300" direction="right"><strong>list of detail</strong></marquee></div>
<div align="left"><input id="compareButton" type="button" value="明细对比(DETAIL CONTRAST)" onclick="detailComp()"/></div>
</div>
<%
if (thList.size() != 0) {
	%>
	<div align="right">
            共&nbsp;<%=dtrecordsize %>&nbsp;条记录&nbsp;&nbsp;&nbsp;每页&nbsp;<%=dtprepage %>&nbsp;条&nbsp;&nbsp;&nbsp;共&nbsp;<%=dtpagesize %>&nbsp;页&nbsp;&nbsp;&nbsp;当前第&nbsp;<%=dtcurpage %>&nbsp;页
            <a <%if(dtcurpage > 1){%>onclick="dtOnPage(1)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >首页</a>
            <a <%if(dtcurpage > 1){ %>onclick="dtOnPage(<%=dtcurpage-1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >上一页</a>
            <a <%if(dtcurpage < dtpagesize && dtpagesize > 1){ %>onclick="dtOnPage(<%=dtcurpage+1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >下一页</a>
            <a <%if(dtpagesize > 1){ %>onclick="dtOnPage(<%=dtpagesize%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >尾页</a>
            <input type="button" onclick="dtOnPage(-1)" value="转到(JUMP)"/> 第 
            <input id="dtTop" class=text style="TEXT-ALIGN: left" size=3 value=""> 页
        </div>
        <br/>
	<table id="dttable" border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
	<tr />
	<tr>
	<%
	for (int m = 0; m < thList.size(); m++) {
		%>
		<th><%=thList.get(m) %></th>
		<%
	}
	%>
	</tr>
	<%
	int dtBeginIndex = (dtcurpage - 1)*dtprepage;
	int dtEndIndex = 0;
	if (dtcurpage == dtpagesize) {
	    dtEndIndex = dtrecordsize - 1;
	} else {
	    dtEndIndex = dtcurpage*dtprepage - 1;
	}
	for (int n = 0; n < tdList.size(); n++) {
		if (dtBeginIndex <= n && n <= dtEndIndex) {
		%>
		<tr>
		<%
		for (int l = 0; l < thList.size(); l++) {
			%>
			<td><%=tdList.get(n).get(l) %></td>
			<%
		}
		%>
		</tr>
		<%
		}
	}
	%>
	</table>
	<br/>
	<div align="right">
            共&nbsp;<%=dtrecordsize %>&nbsp;条记录&nbsp;&nbsp;&nbsp;每页&nbsp;<%=dtprepage %>&nbsp;条&nbsp;&nbsp;&nbsp;共&nbsp;<%=dtpagesize %>&nbsp;页&nbsp;&nbsp;&nbsp;当前第&nbsp;<%=dtcurpage %>&nbsp;页
            <a <%if(dtcurpage > 1){%>onclick="dtOnPage(1)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >首页</a>
            <a <%if(dtcurpage > 1){ %>onclick="dtOnPage(<%=dtcurpage-1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >上一页</a>
            <a <%if(dtcurpage < dtpagesize && dtpagesize > 1){ %>onclick="dtOnPage(<%=dtcurpage+1%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >下一页</a>
            <a <%if(dtpagesize > 1){ %>onclick="dtOnPage(<%=dtpagesize%>)" style="text-decoration:underline;color:#0000FF;cursor:default;"<%}%> >尾页</a>
            <input type="button" onclick="dtOnPage(-2)" value="转到(JUMP)"/> 第 
            <input id="dtDown" class=text style="TEXT-ALIGN: left" size=3 value=""> 页
        </div>
	<%
}
%>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
</body>
<script type="text/javascript">
$(document).ready(function() {
    var measureName = '<%=measureName %>';
    var mmpagesize = '<%=mmpagesize %>';
    var dtpagesize = '<%=dtpagesize %>';
    $("#measureName").children().each(function(){
    	if ($(this).val() == measureName){
    		$(this).attr("selected", true);
    	}
    });
    if (dtpagesize > 0) {
    	$("#dttable").focus();
    } else if (mmpagesize > 0) {
    	$("#mmtable").focus();
    }
    
});
function buttonSubmit(value){
	if (value == 0) {
		if ($("#selectName").val() == "") {
			alert("请输入查找内容！！");
			return false;
		}
		$("#likeselect").submit();
	} else if (value == 1) {
		if ($("#measureName").val() == "") {
            alert("请选择内容！！");
            return false;
        }
		if ($("#beginDate").val() == "" || $("#endDate").val() == "") {
			alert("请填写完整时间！！");
			return false;
		} else if ($("#beginDate").val() > $("#endDate").val()) {
			alert("请填写正确时间！！");
            return false;
		}
		$("#detailSelect").submit();
	}
}

function detailSubmit(obj){
	$("#measure").val($(obj).parent().prev().text());
    $("#tableSelect").submit();
}

function mmOnPage(page){
	var mmpagesize = '<%=mmpagesize %>';
	if (page == -1) {
		var mmTop = $("#mmTop").val();
		if (mmTop == "" || isNaN(mmTop) || mmTop > mmpagesize) {
			alert("请输入正确的页数！！");
			return false;
		}
		$("#mmcurpage").val(mmTop);
	}else if (page == -2) {
		var mmDown = $("#mmDown").val();
        if (mmDown == "" || isNaN(mmDown) || mmDown > mmpagesize) {
            alert("请输入正确的页数！！");
            return false;
        }
        $("#mmcurpage").val(mmDown);
	} else {
		$("#mmcurpage").val(page);
	}
	$("#pageForm").submit();
}

function dtOnPage(page){
    var dtpagesize = '<%=dtpagesize %>';
    $("#submitPage").val(4);
    if (page == -1) {
        var dtTop = $("#dtTop").val();
        if (dtTop == "" || isNaN(dtTop) || dtTop > dtpagesize) {
            alert("请输入正确的页数！！");
            return false;
        }
        $("#dtcurpage").val(dtTop);
    }else if (page == -2) {
        var dtDown = $("#dtDown").val();
        if (dtDown == "" || isNaN(dtDown) || dtDown > dtpagesize) {
            alert("请输入正确的页数！！");
            return false;
        }
        $("#dtcurpage").val(dtDown);
    } else {
        $("#dtcurpage").val(page);
    }
    $("#pageForm").submit();
}

function detailComp(){
	var dtpagesize = '<%=dtpagesize %>';
	if (dtpagesize == 0) {
		alert("请先查询明细再进行对比！！");
		return false;
	}
	$("#submitPage").val(5);
	$("#pageForm").submit();
}
</script>
</html>