<%@ LANGUAGE=VBSCRIPT %>
<html>
<!-- HTML�R�����g�P -->
<body>
<SCRIPT LANGUAGE="VBScript" RUNAT="Server">
    ' �R�����g���̂P
    Response.Write("Hello World<BR>")
</SCRIPT> 

<SCRIPT LANGUAGE="VBScript" RUNAT="Server">
    ' �R�����g���̂Q
    For Each name In Request.ServerVariables
        Response.Write(name & " = " & Request.ServerVariables(name) & "<BR>")
    Next
</SCRIPT> 
</body>
<!-- HTML�R�����g2 -->
</html> 
