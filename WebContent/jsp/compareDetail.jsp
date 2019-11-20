<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery/jquery-1.7.2.min.js"></script>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<title>亚心医院等级评审--数据对比</title>
</head>
<%
String measure = (String)request.getAttribute("measure");
String message = (String)request.getAttribute("message");
String fileMeasure = (String)request.getAttribute("fileMeasure");
String fileTDNumber = (String)request.getAttribute("filetdNum");
String fileTHContext = (String)request.getAttribute("filethContext");
List<String> thList = (List<String>)request.getAttribute("thList");
if (thList == null) {
	thList = new ArrayList<String>();
}
List<List<String>> tdList = (List<List<String>>)request.getAttribute("tdList");
if (tdList == null) {
	tdList = new ArrayList<List<String>>();
}
List<String> selectList = (List<String>)request.getAttribute("selectList");
if (selectList == null) {
	selectList = new ArrayList<String>();
}
List<List<String>> sameComList = (List<List<String>>) request.getAttribute("sameComList");
if (sameComList == null) {
	sameComList = new ArrayList<List<String>>();
}
List<List<String>> fileDiffComList = (List<List<String>>) request.getAttribute("fileDiffComList");
if (fileDiffComList == null) {
	fileDiffComList = new ArrayList<List<String>>();
}
List<List<String>> diffComList = (List<List<String>>) request.getAttribute("diffComList");
if (diffComList == null) {
	diffComList = new ArrayList<List<String>>();
}
int thNumber = thList.size();
int tdNumber = tdList.size();
int thSize = selectList.size();
String thContext = "";
for (String arg : thList) {
	thContext = thContext + arg + "  ";
}
%>
<body>
<hr id="messageLine" style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<b id="message" style="color:#FF0000;"><span>错误消息提示(ERROR MESSAGE)：</span><%=message %></b>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<form id="back" action="file.action" method="post">
<input id="uploadHidden" type="hidden" name="uploadHidden" value="0"/>
</form>
<form id="fileUpload" action="fileUpload.action" method="post" enctype="multipart/form-data">
<div><span><strong>文件路径选择(FILE PASS SELECT)：</strong></span><input id="filePath" type="file" name="file"/></div>
<br/>
<div>
<input type="button" value="文件上传(FILE UPLOAD)" onclick="buttonSubmit(1)"/>
<input type="button" value="返回明细查询(BACK TO DETAILS INQUIRE)" onclick="buttonSubmit(0)"/>
</div>
</form>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<div align="center"><strong>指标明细显示(DETAIL MEASURE VIEW)</strong></div>
<table border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
<tr><td width="15%"><span>指标ID(MEASURE ID)：</span></td><td width="35%"><span><%=measure %></span></td><td width="15%"><span>指标明细数目(DETAIL MEASURE NUMBER)：</span></td><td><span><%=tdNumber %></span></td></tr>
<tr><td width="15%"><span>指标明细栏位(DETAIL MEASURE COLUMN)：</span></td><td colspan="3"><span><%=thContext %></span></td></tr>
</table>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<div align="center"><strong>对比文件指标明细显示(DETAIL MEASURE VIEW FOR CONTRAST FILE)</strong></div>
<table border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
<tr><td width="20%"><span>对比文件指标ID(MEASURE ID FOR CONTRAST FILE)：</span></td><td width="30%"><span><%=fileMeasure %></span></td><td width="20%"><span>对比文件指标明细数目(DETAIL MEASURE NUMBER FOR CONTRAST FILE)：</span></td><td><span><%=fileTDNumber %></span></td></tr>
<tr><td width="20%"><span>对比文件指标明细栏位(DETAIL MEASURE COLUMN FOR CONTRAST FILE)：</span></td><td colspan="3"><span><%=fileTHContext %></span></td></tr>
</table>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<form id="fileCompare" action="file.action" method="post">
<input type="hidden" name="uploadHidden" value="1"/>
<div align="left"><strong>请选择对比项(PLEASE SELECT CONTRAST COLUMN)</strong></div>
<table border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;" rules="rows">
<%
for (int j = 0; j < selectList.size(); j++) {
	if (j % 3 == 0) {
		%><tr><%
	}
	%>
	<td width="33%"><input type="checkbox" name="selectItem" value="<%=selectList.get(j) %>" width="100"/><%=selectList.get(j) %></td>
	<%
	if ((j + 1) % 3 == 0) {
	%>
	<tr/>
	<%
	} else if (j == (selectList.size() - 1)) {
		for (int k = 0; k < (3 - (j + 1)%3); k++) {
			%>
	        <td width="33%"/>
	        <%
		}
		%>
	    <tr/>
	    <%
	}
}
%>
</table>
<br/>
<div align="right"><input type="button" value="文件对比(FILE CONSTRASTED)" onclick="fileCompare()"/></div>
</form>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<div align="center"><strong>对比完成后共有明细(SAME DETAIL AFTER CONTRASTED)</strong></div>
<table border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
<%
for (int i = 0; i < sameComList.size(); i++) {
	List<String> sameItemList = sameComList.get(i);
	if (i == 0) {
		%><tr><%
		for (int j = 0; j < sameItemList.size(); j++) {
			%>
			<th><%=sameItemList.get(j) %></th>
			<%
		}
		%></tr><%
	} else {
		%><tr><%
        for (int j = 0; j < sameItemList.size(); j++) {
            %>
            <th><%=sameItemList.get(j) %></th>
            <%
        }
        %></tr><%
	}
}
%>
</table>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<div align="center"><strong>对比完成后数据库拥有明细(DETAIL FOR ORACLE AFTER CONTRASTED)</strong></div>
<table border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
<%
for (int i = 0; i < diffComList.size(); i++) {
    List<String> difftemList = diffComList.get(i);
    if (i == 0) {
        %><tr><%
        for (int j = 0; j < difftemList.size(); j++) {
            %>
            <th><%=difftemList.get(j) %></th>
            <%
        }
        %></tr><%
    } else {
        %><tr><%
        for (int j = 0; j < difftemList.size(); j++) {
            %>
            <th><%=difftemList.get(j) %></th>
            <%
        }
        %></tr><%
    }
}
%>
</table>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
<div align="center"><strong>对比完成后文件拥有明细(DETAIL FOR FILE AFTER CONTRASTED)</strong></div>
<table border="1" cellpadding="10" width="100%" cellspacing="0" style="font-family: '宋体'; font-size: 19px;">
<%
for (int i = 0; i < fileDiffComList.size(); i++) {
    List<String> filedifftemList = fileDiffComList.get(i);
    if (i == 0) {
        %><tr><%
        for (int j = 0; j < filedifftemList.size(); j++) {
            %>
            <th><%=filedifftemList.get(j) %></th>
            <%
        }
        %></tr><%
    } else {
        %><tr><%
        for (int j = 0; j < filedifftemList.size(); j++) {
            %>
            <th><%=filedifftemList.get(j) %></th>
            <%
        }
        %></tr><%
    }
}
%>
</table>
<hr style="FILTER: alpha(opacity = 1, finishopacity = 0, style = 3)" width="100%" color="#987cb9" size="3" />
</body>
<script type="text/javascript">
$(document).ready(function(){
	var message = '<%=message %>';
	if (message == "") {
		$("#messageLine").hide();
		$("#message").hide();
	} else {
		$("#messageLine").show();
        $("#message").show();
	}
});
function buttonSubmit(value){
	if (value == "1") {
		if ($("#filePath").val() == "") {
			alert("请选择EXCEL文件！！");
	        return false;
		} else {
			$("#fileUpload").submit();
		}
	} else if (value == "0") {
		$("#back").submit();
	}
	
}

function fileCompare(){
	var message = '<%=message %>';
	var thSize = '<%=thSize %>';
	if (message != "") {
        alert("请确认传入文件数据的正确性！！");
        return false;
    } else if (thSize == 0) {
        alert("请上传正确的文件！！");
        return false;
    }
	$("#fileCompare").submit();
}
</script>
</html>