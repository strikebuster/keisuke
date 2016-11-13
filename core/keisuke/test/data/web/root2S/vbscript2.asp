<%@ LANGUAGE=VBSCRIPT %>
<html>
<!-- HTMLコメント１ -->
<body>
<SCRIPT LANGUAGE="VBScript" RUNAT="Server">
    ' コメントその１
    Response.Write("Hello World<BR>")
</SCRIPT> 

<SCRIPT LANGUAGE="VBScript" RUNAT="Server">
    ' コメントその２
    For Each name In Request.ServerVariables
        Response.Write(name & " = " & Request.ServerVariables(name) & "<BR>")
    Next
</SCRIPT> 
</body>
<!-- HTMLコメント2 -->
</html> 
