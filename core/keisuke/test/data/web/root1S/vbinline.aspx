<%@ Page Language="VB" %>
<script runat="server">
    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs)
        Dim strName As String
        strName = Session("userName")
        lblUserName.Text = strName
    End Sub
</script>
<html>
<!-- HTML�R�����g�P -->
<body>
    <form id="form1" runat="server">
    <%-- �R�����g���̂P --%>
    <asp:Label ID="lblUserName" runat="server" Text=""></asp:Label>
    <%-- �R�����g���̂Q --%>
    <% For i As Integer = 16 To 24 Step 2%>
    <div style="font-size: <% Response.Write(i)%>">
        Hello World<br />
    </div>
    <% Next%>
    <%=DateTime.Now.ToString() %>
    </form>
</body>
<!-- HTML�R�����g2 -->
</html>
