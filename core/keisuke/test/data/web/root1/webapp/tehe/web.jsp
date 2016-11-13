<%@ page contentType="text/html;charset=UTF-8" %>
<%! String strVal=null; %>
<%
request.setCharacterEncoding("JISAutoDetect");
/* フォームデータの文字エンコーディングを自動判定（AutoJISDetect） */
strVal=request.getParameter("nam");
/* HTMLフォーム"nam"の内容を取得 zzz */
%>
<html>
<head>
<title>Hello World!</title>
</head>
<body>
<h1>Hello World!</h1>
<!-- HTMLコメント1 zzz -->
こんにちは、<%=strVal%>さん！ zzz<br/>
<jsp:include page="include.jsp" />

<% boolean blnFlag = true; %>
<% if(blnFlag==true){ %>
変数blnFlagはtrueです
<% } else { %>
変数blnFlagはfalseです
<% } %><br/>

<%
// コメント zzz
if(blnFlag==true){
out.print("変数blnFlagはtrueです");
} else {
out.print("変数blnFlagはfalseです");
}
%>
<br/>
<%-- Scriptletコメント zzz --%>
変数blnFlagの値は<%=blnFlag ? "true" : "false" %>です。<br/>
</body>
<!-- HTMLコメント2 -->
</html>

