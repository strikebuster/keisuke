<%@ language=jscript %>
<html><body>
<!-- HTML�R�����g�P -->
<script language="JScript" runat="Server">
   // �R�����g�P�@���ׂĂ� Date �I�u�W�F�N�g�ɒǉ����� monthNum ��錾���܂��B
   function monthNum() {
      return this.getMonth() + 1;
   }
   Date.prototype.monthNum = monthNum
   /* �R�����g�Q
      ���ׂĂ� Date �I�u�W�F�N�g�̊���� toString ���\�b�h���I�[�o�[���C�h���܂��B
   */
   function Date.prototype.toString() {
      return this.monthNum() + "/" +
             this.getDate() + "/" +
             this.getYear();
   }

   var dt = new Date();
   Response.Write("���� " + dt.monthNum() +" �ł��B<BR>\n");
   Response.Write("���t�� " + dt +" �ł��B<BR>\n");
</script>
<!-- HTML�R�����g�Q -->
</body></html>
