<%@ Page Language="VB" %>
<script runat="server">
    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs)
        Dim strName As String
        strName = Session("userName")
        lblUserName.Text = strName
    End Sub
</script>
<html>
<!-- HTMLコメント１ -->
<body>
    <form id="form1" runat="server">
    <%-- コメントその１ --%>
    <asp:Label ID="lblUserName" runat="server" Text=""></asp:Label>
    <%-- コメントその２ --%>
    <% For i As Integer = 16 To 24 Step 2%>
    <div style="font-size: <% Response.Write(i)%>">
        Hello World<br />
    </div>
    <% Next%>
    <%=DateTime.Now.ToString() %>
    </form>
</body>
<!-- HTMLコメント2 -->
</html>
