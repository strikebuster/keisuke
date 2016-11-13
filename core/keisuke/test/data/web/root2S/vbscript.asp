<%@ LANGUAGE="VBSCRIPT" %>
<html>
<!-- HTMLコメント１ zzz-->
<body>
<SCRIPT RUNAT="Server" LANGUAGE=JScript >
    // コメントその１ zzz
    Response.Write("Hello World<BR>");
</SCRIPT> 

<SCRIPT RUNAT="Server">
    ' コメントその２ zzz
    For Each name In Request.ServerVariables
        Response.Write(name & " = " & Request.ServerVariables(name) & "<BR>")
    Next
</SCRIPT> 
</body>
<!-- HTMLコメント2 -->
</html> 
