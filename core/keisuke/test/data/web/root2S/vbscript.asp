<%@ LANGUAGE="VBSCRIPT" %>
<html>
<!-- HTML�R�����g�P zzz-->
<body>
<SCRIPT RUNAT="Server" LANGUAGE=JScript >
    // �R�����g���̂P zzz
    Response.Write("Hello World<BR>");
</SCRIPT> 

<SCRIPT RUNAT="Server">
    ' �R�����g���̂Q zzz
    For Each name In Request.ServerVariables
        Response.Write(name & " = " & Request.ServerVariables(name) & "<BR>")
    Next
</SCRIPT> 
</body>
<!-- HTML�R�����g2 -->
</html> 
