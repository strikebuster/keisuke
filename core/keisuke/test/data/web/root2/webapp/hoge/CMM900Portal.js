function changeRowColor() {
  var id1 = "table1";	// TrinidadテーブルのID
  var id2 = "table2";	// TrinidadテーブルのID
  var c1 = "bk1";		// 奇数行適用のCSS
  var c2 = "";			//偶数行適用のCSS
  tableStyleManager.bgColor(id1, c1, c2);
  tableStyleManager.bgColor(id2, c1, c2);
}
  
    /**
   * 機能グループの表示状態を変更します。
   *
   * @author SKI杉山
   */
function changeOpenClose(id, kensu)
   {
     var elem = DomManager.getClientId(id);
     if (elem != null) {
       var prevElem = elem.previousSibling;
       while (prevElem != null && prevElem.tagName == "!")
       {
         prevElem = prevElem.previousSibling;
       }
       var imgElem = DomManager.getClientTags("img", prevElem);
       if (imgElem != null && imgElem.length > 0)
       {
         //0件だったらエリアを閉じる
         if(kensu == 0)
         {
           openclose(id, imgElem[0]);
         }
       }
     }
   }

  /**
   * フラグを見て告知メッセージエリアの開閉状態を変更します。
   *
   * @author NRI重松
   */
function kokuchimesseage(id, flg)
   {
     var elem = DomManager.getClientId(id);
     if (elem != null) {
       var prevElem = elem.previousSibling;
       while (prevElem != null && prevElem.tagName == "!")
       {
         prevElem = prevElem.previousSibling;
       }
       var imgElem = DomManager.getClientTags("img", prevElem);
       if (imgElem != null && imgElem.length > 0)
       {
         //　フラグの状態が"close"ならエリアを閉じる
         if(flg == "close")
         {
           openclose(id, imgElem[0]);
         }
       }
     }
   }