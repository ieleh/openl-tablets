<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1257">
<title>Desktop</title>
<link href="../css/style.css" rel="stylesheet" type="text/css">
</head>

<body link="#224E76" alink="#224E76" vlink="#224E76">

<script language="JavaScript">
function expandIt(whichEl,tab,image) {
	tabTable.style.display = "none";
	tabProperties.style.display = "none";
	tabSecurity.style.display = "none";
	tabDetail.style.display = "none";
	tab1.src = "../images/table_r.png"; 
	tab1Det.src = "../images/det_r.png"; 
	tab2.src = "../images/prop_r.png"; 
	tab3.src = "../images/sec_r.png"; 

	tab.src = "../images/" + image; 
	whichEl.style.display = "inline"; 
	//alert(image);
	}
</script>

<table><tr><td width="1"><td><img src="../images/s.gif" width="1" height="1"></td>
<td><br>
<table cellspacing="0" cellpadding="0">
<!-- <tr><td height="9"><img src="../images/s.gif"></td></tr> -->
	<tr>
		<td nowrap>
<!--		<img name="tab1" src="../images/tab_table_1.gif" width="78" height="25" border="0" alt="Table" onClick="expandIt(tabTable,tab1,'tab_table_1.gif')" onMouseOver='this.style.cursor="hand";' onMouseOut='this.style.cursor="";'>
-->
		<img name="tab1" src="../images/table_b.png" border="0" alt="Table" 
		                  onClick="expandIt(tabTable,tab1,'table_b.png')" onMouseOver='this.style.cursor="hand";' onMouseOut='this.style.cursor="";'>
		<img name="tab1Det" src="../images/det_r.png" border="0" alt="Details" 
		                  onClick="expandIt(tabDetail,tab1Det,'det_b.png')" onMouseOver='this.style.cursor="hand";' onMouseOut='this.style.cursor="";'>
		<img name="tab2" src="../images/prop_r.png" border="0" alt="Properties" 
		   onClick="expandIt(tabProperties,tab2,'prop_b.png')" onMouseOver='this.style.cursor="hand";' 
		   onMouseOut='this.style.cursor="";'>
		<img name="tab3" src="../images/sec_r.png" border="0" alt="Security" 
		onClick="expandIt(tabSecurity,tab3,'../images/sec_b.png')" onMouseOver='this.style.cursor="hand";' onMouseOut='this.style.cursor="";'>
		</td>
	</tr>
</table>

<!-- ============================== Table ============================== -->
<div id="tabTable" style="display: inline; position: relative; width: 100%; margin-top: 3 px">

<table width="718" class="outer" border="0" cellspacing="3">
	<tr>
		<td class="data">&nbsp;</td></tr>
	<tr>
		<td>

<%@include file="showTable.jsp"%>

		</td>
	</tr>
	<tr><td height="5"><img src="../images/s.gif" width="1" height="23"></td></tr>
</table>

</div>

<!-- ============================== Details ============================== -->
<div id="tabDetail" style="display: none; position: relative; width: 100%; margin-top: 3 px">

<table width="718" class="outer" border="0" cellspacing="3">
	<tr>
		<td class="data">&nbsp;</td></tr>
	<tr>
		<td>

<%=studio.getModel().showTable(elementID, null)%>

		</td>
	</tr>
	<tr><td height="5"><img src="../images/s.gif" width="1" height="23"></td></tr>
</table>

</div>


<!-- ============================== Properties ============================== -->
<div id="tabProperties" style="display: none; position: relative; width: 100%">
<div class="indent">
<table width="718" class="outer" border="0" cellspacing="3">
	<tr>
		<td class="data">&nbsp;</td></tr>
	<tr>
		<td>
<%@include file="showProperties.jsp"%>
		</td>
	</tr>
	<tr><td height="5"><img src="../images/s.gif" width="1" height="23"></td></tr>
</table>
</div>

</div>


<!-- ============================== Security ============================== -->
<div id="tabSecurity" style="display: none; position: relative; width: 100%">
<div class="indent">
<table width="718" class="outer" border="0" cellspacing="3">
	<tr>
		<td class="data">&nbsp;</td>
	<tr>
		<td>

   		<table class="data" border="0" width="718" cellspacing="0">
          <tr>
            <td class="column" width="16%">&nbsp;</td>
            <td class="column" width="22%">Permissions</td>
            <td class="column" width="25%">Actions</td>
            <td class="column">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 1</td>
            <td class="rowHeightOdd"><input name="Permissions-1" type="text" value="Modify" maxlength="20"></td>
            <td class="rowHeightOdd">Add Role:&nbsp;<select name="Roles-1" size="1"><option>All</option><option>Read</option><option>Execute</option></select></td>
            <td class="rowHeightOdd">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 2</td>
            <td class="rowHeightEven"><input name="Permissions-2" type="text" value="All" maxlength="20"></td>
            <td class="rowHeightEven">Add Role:&nbsp;<select name="Roles-2" size="1"><option>Read</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightEven">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 3</td>
            <td class="rowHeightOdd"><input name="Permissions-3" type="text" value="Read" maxlength="20"></td>
            <td class="rowHeightOdd">Add Role:&nbsp;<select name="Roles-3" size="1"><option>All</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightOdd">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 4</td>
            <td class="rowHeightEven"><input name="Permissions-4" type="text" value="Modify" length="20"></td>
            <td class="rowHeightEven">Add Role:&nbsp;<select name="Roles-4" size="1"><option>All</option><option>Read</option><option>Execute</option></select></td>
            <td class="rowHeightEven">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 5</td>
            <td class="rowHeightOdd"><input name="Permissions-5" type="text" value="All" maxlength="20"></td>
            <td class="rowHeightOdd">Add Role:&nbsp;<select name="Roles-5" size="1"><option>Read</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightOdd">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 6</td>
            <td class="rowHeightEven"><input name="Permissions-6" type="text" value="All" maxlength="20"></td>
            <td class="rowHeightEven">Add Role:&nbsp;<select name="Roles-6" size="1"><option>Read</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightEven">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 7</td>
            <td class="rowHeightOdd"><input name="Permissions-7" type="text" value="Read" maxlength="20"></td>
            <td class="rowHeightOdd">Add Role:&nbsp;<select name="Roles-7" size="1"><option>All</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightOdd">&nbsp;</td>
          </tr>
          <tr>
            <td class="column1">User 8</td>
            <td class="rowHeightEven"><input name="Permissions-8" type="text" value="Modify" length="20"></td>
            <td class="rowHeightEven">Add Role:&nbsp;<select name="Roles-8" size="1"><option>All</option><option>Read</option><option>Execute</option></select></td>
            <td class="rowHeightEven">&nbsp;</td>
          </tr>
<!--           <tr>
            <td class="column1">User 9</td>
            <td class="rowHeightOdd"><input name="Permissions-5" type="text" value="All" maxlength="20"></td>
            <td class="rowHeightOdd">Add Role:&nbsp;<select name="Roles-9" size="1"><option>Read</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightOdd">&nbsp;</td>
          </tr> -->
          <tr>
            <td class="column1">The Last User</td>
            <td class="rowHeightOddLast"><input name="Permissions-6" type="text" value="Read" maxlength="20"></td>
            <td class="rowHeightOddLast">Add Role:&nbsp;<select name="Roles-6" size="1"><option>All</option><option>Modify</option><option>Execute</option></select></td>
            <td class="rowHeightOddLast">&nbsp;</td>
          </tr>
        </table>
</td></tr>
<tr>
  <td>&nbsp;
</td>
</tr>
</table>

</div>
</div>

</body>

