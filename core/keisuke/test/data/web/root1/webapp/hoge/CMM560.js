// 売上計上区分が月次出来高ではない場合、請求予定自動増幅は非表示
function cmm560JikaiZoufukuInit()
{
    var xxx = 0;
    // 売上計上区分が月次出来高ではない場合、請求予定自動増幅は非表示
    var uriage_keijo_kbn = document.getElementById("uriage_keijo_kbn").value
    if (uriage_keijo_kbn == "3")
    {
		document.getElementById("jidouZoufuku_area").style.display="block";
    }
    else
    {
		document.getElementById("jidouZoufuku_area").style.display="none";
    }
    
}

// チェックボックスのON、OFFで自動増幅設定の表示・非表示制御
function hiddenJidouZoufukuSet()
{
	var cmm560checkbox = document.getElementById("CMM560checkbox").checked;
	var imgObj = document.getElementById("imgObj");
	if(cmm560checkbox == true)
	{
		document.getElementById("JidouZoufuku").style.display="block";
		document.getElementById("CMM560_2").style.display="block";
		imgObj.src="../../img/common/co_ico_minusA.gif";
	}
//	else
//	{
		document.getElementById("JidouZoufuku").style.display="none";
		document.getElementById("CMM560_2").style.display="none";
		imgObj.src="../../img/common/co_ico_plusA.gif";
//	}
}
