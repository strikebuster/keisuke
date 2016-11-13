<html>
<head>
<title>ColdFusionページ</title>
</head>
<body>
<strong>このページはColdFusionで処理しました。</strong>
<br>
<cfoutput>今日は#DateFormat(Now())#です。</cfoutput>
<!--- コメントです 
 ここもコメント
　ここでコメント終わり --->
<cfset pref="東京都">
<cfif pref EQ "神奈川県">
あなたは神奈川県に住んでいます。<br />
<cfelse>
あなたは神奈川県以外に住んでいます。<br />
</cfif>
<cfform action="postpage.cfm">
  年齢：<cfinput type="text" name="age" required="yes" validate="integer" range="0,130"
         message="年齢は整数で入力下さい" >
  <cfinput type="submit" name="submit" value="送信">
</cfform>
<cfset msg='I said "Hello." to him.'>
<cfset msg2="I said ""Hello."" to him.">
<!--- コメント --->
</body>
</html>
