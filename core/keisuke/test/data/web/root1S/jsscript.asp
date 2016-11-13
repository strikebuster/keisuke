<%@ language=jscript %>
<html><body>
<!-- HTMLコメント１ -->
<script language="JScript" runat="Server">
   // コメント１　すべての Date オブジェクトに追加する monthNum を宣言します。
   function monthNum() {
      return this.getMonth() + 1;
   }
   Date.prototype.monthNum = monthNum
   /* コメント２
      すべての Date オブジェクトの既定の toString メソッドをオーバーライドします。
   */
   function Date.prototype.toString() {
      return this.monthNum() + "/" +
             this.getDate() + "/" +
             this.getYear();
   }

   var dt = new Date();
   Response.Write("月は " + dt.monthNum() +" です。<BR>\n");
   Response.Write("日付は " + dt +" です。<BR>\n");
</script>
<!-- HTMLコメント２ -->
</body></html>
