<%@ LANGUAGE=VBScript %>
<html>
<!-- HTML�R�����g�P -->
<body>
<%
' �R�����g���̂P
Response.Write "Hello World"
%>

<%
' �R�����g���̂Q
For Each name In Request.ServerVariables
    Response.Write name & " = " & Request.ServerVariables(name) & "<br>"
Next
%>
</body>
<!-- HTML�R�����g2 -->
</html> 
