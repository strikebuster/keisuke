<%@Page Language="C#"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%
// �R�����g���̂P zzz
void Button1_Click(Object sender, EventArgs e)
{
    Label1.Text = "Built at " + DateTime.Now.ToString();
}
/* �R�����g���̂Q zzz*/
void listFruit_SelectedIndexChanged(Object sender, EventArgs e)
{
    Label1.Text = "You selected " + listFruit.SelectedItem.Text;
}
%>

<html xmlns="http://www.w3.org/1999/xhtml" >
<!-- HTML�R�����g�P zzz -->
<head runat="server">
  <title>ASP.NET XHTML Page</title>
</head>
<body>
  <form id="Form1" runat="server">
    <div>
      <h1>ASP.NET Sample Page for XHTML</h1>
      <p>
      <asp:listbox runat="server" id="listFruit" AutoPostBack="true" 
          onselectedindexchanged="listFruit_SelectedIndexChanged">
         <asp:listitem>Apple</asp:listitem>
         <asp:listitem>Banana</asp:listitem>
         <asp:listitem>Orange</asp:listitem>
      </asp:listbox>
      </p>
      <asp:label runat="server" id="Label1" ForeColor="white" 
          BackColor="black" />
      <br />
      <asp:button runat="server" id="Button1" onclick="Button1_Click" 
          Text="Click me"/>
    </div>
  </form>
</body>
<!-- HTML�R�����g2 -->
</html>
