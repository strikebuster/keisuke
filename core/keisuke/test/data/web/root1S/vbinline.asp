<%@ LANGUAGE=VBScript %>
<html>
<!-- HTMLコメント１ -->
<body>
<%
' コメントその１
Response.Write "Hello World"
%>

<%
' コメントその２
For Each name In Request.ServerVariables
    Response.Write name & " = " & Request.ServerVariables(name) & "<br>"
Next
%>
</body>
<!-- HTMLコメント2 -->
</html> 
