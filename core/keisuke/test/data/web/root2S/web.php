<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=Shift_JIS" />
<title>�T���v��</title>
</head>
<!-- HTML�R�����g�P zzz �ǉ�-->
<body>
<? $expression = true; ?>
<?php if ($expression == true): ?>
  ���������^�̏ꍇ�ɂ��ꂪ�\������܂��B
<?php else: ?>
  ����ȊO�̏ꍇ�ɂ����炪�\������܂��B
<?php endif; ?>

<script language="php">
// �R�����g���̂P zzz�ǉ�
if ($expression == true):
  echo '���������^�̏ꍇ�ɂ��ꂪ�\������܂��B';
else:
  echo '����ȊO�̏ꍇ�ɂ����炪�\������܂��B';
endif;
</script>

<%
/* �R�����g���̂Q�@zzz�ǉ� */
if ($expression == true) {
  print "��������".$expression."�̏ꍇ�ɂ��ꂪ�\������܂��B";
} else {
  print $expression."�ȊO�̏ꍇ�ɂ����炪�\������܂��B";
}
%>
<%-- zzz Scriptlet�R�����g�ǉ� --%>
<!-- HTML�R�����g2 -->
</html>
