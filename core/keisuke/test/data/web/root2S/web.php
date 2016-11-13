<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=Shift_JIS" />
<title>サンプル</title>
</head>
<!-- HTMLコメント１ zzz 追加-->
<body>
<? $expression = true; ?>
<?php if ($expression == true): ?>
  条件式が真の場合にこれが表示されます。
<?php else: ?>
  それ以外の場合にこちらが表示されます。
<?php endif; ?>

<script language="php">
// コメントその１ zzz追加
if ($expression == true):
  echo '条件式が真の場合にこれが表示されます。';
else:
  echo 'それ以外の場合にこちらが表示されます。';
endif;
</script>

<%
/* コメントその２　zzz追加 */
if ($expression == true) {
  print "条件式が".$expression."の場合にこれが表示されます。";
} else {
  print $expression."以外の場合にこちらが表示されます。";
}
%>
<%-- zzz Scriptletコメント追加 --%>
<!-- HTMLコメント2 -->
</html>
