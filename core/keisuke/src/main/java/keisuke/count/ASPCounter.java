package keisuke.count;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ASP, ASP.NET用のステップカウンタ
 */

public class ASPCounter extends DefaultStepCounter {

	private static final String SCRIPT_TAG = "<SCRIPT";
	private static final String SCRIPT_DIRECTIVE = "<%@";
	private static final String TAG_ATTR_LANG = "LANGUAGE=";

	// language属性で指定された言語を保持
	private String tagLang = null;
	// デフォルト言語
	private ProgramLangRule defaultLang = null;

	// VBScript用定義
	private LineComment vbsLineComment = new LineComment("'");
	private LiteralString vbsLiteralString = new LiteralString("\"", "\"", "\"\"");
	// JScript用定義
	private LineComment jsLineComment = new LineComment("//");
	private AreaComment jsAreaComment = new AreaComment("/*", "*/");
	private LiteralString jsLiteralString = new LiteralString("\"", "\"", "\\\"");
	// VB.NET用定義
	private List<LineComment> vbLineComments = Arrays.asList(new LineComment("'"), new LineComment("^rem "));
	private LiteralString vbLiteralString = new LiteralString("\"", "\"", "\"\"");
	// C#用定義
	private LineComment csLineComment = new LineComment("//");
	private AreaComment csAreaComment = new AreaComment("/*", "*/");
	private LiteralString csLiteralString = new LiteralString("\"", "\"", "\\\"");
	// 共通定義
	private AreaComment scriptAreaComment = new AreaComment("<%--", "--%>");


	/**
	 * コンストラクター
	 * @param type ASPかASP.NETかを指定する
	 */
	public ASPCounter(final String type) {
		super(true);

		if (type.equals("ASP")) {
			this.setFileType("ASP");

			// VBScript定義
			ProgramLangRule vbsRule = new ProgramLangRule();
			vbsRule.setLanguageName("VBScript");
			vbsRule.addAreaComment(scriptAreaComment);
			vbsRule.addLineComment(vbsLineComment);
			vbsRule.addLiteralString(vbsLiteralString);
			this.addScriptLang(vbsRule);
			this.defaultLang = vbsRule;

			// JScript定義
			ProgramLangRule jsRule = new ProgramLangRule();
			jsRule.setLanguageName("JScript");
			jsRule.addAreaComment(scriptAreaComment);
			jsRule.addLineComment(jsLineComment);
			jsRule.addAreaComment(jsAreaComment);
			jsRule.addLiteralString(jsLiteralString);
			this.addScriptLang(jsRule);
		} else {
			this.setFileType("ASP.NET");

			// VB定義
			ProgramLangRule vbRule = new ProgramLangRule();
			vbRule.setLanguageName("VB");
			vbRule.addAreaComment(scriptAreaComment);
			for (LineComment lc : vbLineComments) {
				vbRule.addLineComment(lc);
			}
			vbRule.addLiteralString(vbLiteralString);
			this.addScriptLang(vbRule);
			this.defaultLang = vbRule;

			// C#定義
			ProgramLangRule csRule = new ProgramLangRule();
			csRule.setLanguageName("C#");
			csRule.addAreaComment(scriptAreaComment);
			csRule.addLineComment(csLineComment);
			csRule.addAreaComment(csAreaComment);
			csRule.addLiteralString(csLiteralString);
			this.addScriptLang(csRule);
		}

		// Scriptコメント、Scriptlet開始・終了
		this.addScriptBlock(new ScriptBlock("<%@", "%>"));
		this.addScriptBlock(new ScriptBlock("<%", "%>"));
		this.addScriptBlock(new ScriptBlock("<script runat=\"server\">", "</script>"));
	}

	/* Script開始記号が含まれるかどうかをチェックし、有効な文字列を返す */
	@Override
	protected String searchScriptStart(final String line) {
		int sbidx = -1;
		int sbPosMin = -1;
		int sblen = 0;
		String scriptTag = null;

		// Script記号をチェック
		for (int i = 0; i < this.scriptBlocks().size(); i++) {
			String tag = null;
			ScriptBlock block = this.scriptBlocks().get(i);
			String start = block.getStartString();
			int pos;

			if (start.toUpperCase().startsWith(SCRIPT_TAG)) {
				// SCRIPTタグの場合、先頭だけで検索する
				pos = line.toUpperCase().indexOf(SCRIPT_TAG);
				if (pos >= 0) {
					tag = this.findScriptTag(start, line.substring(pos));
					if (tag != null) {
						// SCRIPTタグの内容がマッチした
						start = tag;
						block.setScriptLang(tagLang);
					} else {
						// SCRIPTタグの内容がマッチしなかった
						pos = -1;
					}
				}
			} else if (start.startsWith(SCRIPT_DIRECTIVE)) {
				// SCRIPTディレクティブ宣言タグ
				pos = line.indexOf(start);
				if (pos >= 0) {
					tag = this.findDirectiveTag(line.substring(pos));
					if (tag != null) {
						// タグの内容がマッチした
						start = tag;
						if (this.tagLang != null) {
							// 言語指定があったなら設定
							block.setScriptLang(this.tagLang);
						}
					}
				}
			} else {
				// SCRIPTタグ以外
				pos = block.searchStartingMark(line);
			}

			if (pos >= 0) {
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (sbPosMin < 0 || pos < sbPosMin
						|| (pos == sbPosMin && start.length() > sblen)) {
					// 最左の複数行コメント記号
					sbPosMin = pos;
					sbidx = i;
					sblen = start.length();
					scriptTag = tag;
				}
			}
		}
		if (sbPosMin < 0) {
			// Script外部のリテラル行
			return line;
		}
		// Script記号を見つけた
		StringBuilder sb = new StringBuilder();
		ScriptBlock block = this.scriptBlocks().get(sbidx);
		// Script記号が言語指定をするものか確認して言語を設定
		String start = block.getStartString();
		if (start.toUpperCase().startsWith(SCRIPT_TAG)) {
			// SCRIPTタグ
			String lang = block.getScriptLang();
			this.setCurrentLang(this.getScriptLang(lang));
		} else if (start.startsWith(SCRIPT_DIRECTIVE)) {
			// SCRIPTディレクティブ宣言タグ
			String lang = block.getScriptLang();
			this.setCurrentLang(this.getScriptLang(lang));
			if (this.currentLang() != null) {
				this.defaultLang = this.currentLang();
				//System.out.println("[DEBUG] Directive Lang : " + defaultLang.getLangType());
			}
		}
		if (this.currentLang() == null) {
			this.setCurrentLang(this.defaultLang);
		}
		this.pushNewStatusAsScriptletCodeWith(block, this.currentLang());
		this.pushNewStatusAsProgramRule();
		//System.out.println("[DEBUG] Current Lang : " + currentLang.getLangType());
		// Script記号の左側は有効行なので返却対象
		String target = null;
		sb.append(line.substring(0, sbPosMin));
		if (scriptTag != null) {
			sb.append(scriptTag);
			target = line.substring(sbPosMin + scriptTag.length());
		} else {
			target = line.substring(sbPosMin);
		}
		sb.append(removeCommentFromLeft(this.currentLang(), target, 1));
		return sb.toString();
	}

	/**
	 * スクリプトタグを探し、マッチする場合はLanguage属性を探して
	 * 言語名を保持する
	 * @param tag スクリプトレット開始タグ
	 * @param line  解析対象のソース１行の処理未済の行末までの部分文字列
	 * @return スクリプトタグにマッチすればその部分文字列、そうでなければnull
	 */
	protected String findScriptTag(final String tag, final String line) {
		// tagの分解
		String[] checkArray = tag.split("[ \\t]+", 0);
		// lineからタグの抽出
		Pattern pattern = Pattern.compile("(" + SCRIPT_TAG
				 + "([ \\t]+[\\w]+=['\"\\w]+)*[ \\t]*>)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.find()) {
			//タグが見つからない
			return null;
		}
		String target = matcher.group();
		//System.out.println("[DEBUG] Get Tag :"+target);
		String[] targetArray = target.split("[ \\t]+", 0);
		boolean checkAll = true;
		for (int i = 0; i < checkArray.length; i++) {
			boolean checkOK = false;
			String checkStr = checkArray[i].toUpperCase().replaceAll("['\">]", "");
			for (int j = 0; j < targetArray.length; j++) {
				String targetStr = targetArray[j].toUpperCase().replaceAll("['\">]", "");
				// サーバスクリプト指定とチェック
				if (targetStr.equals(checkStr)) {
					checkOK = true;
					//System.out.println("[DEBUG] Match : " + checkStr);
					break;
				}
			}
			if (!checkOK) {
				checkAll = false;
				break;
			}
		}
		if (!checkAll) {
			// マッチしなかった
			return null;
		}
		this.tagLang = null;
		for (int j = 0; j < targetArray.length; j++) {
			String targetStr = targetArray[j].toUpperCase().replaceAll("['\">]", "");
			// language属性かチェック
			if (targetStr.startsWith(TAG_ATTR_LANG)) {
				this.tagLang = targetStr.substring(TAG_ATTR_LANG.length());
				//System.out.println("[DEBUG] Language : " + tagLang);
				break;
			}
		}
		return target;
	}

	/**
	 * スクリプトのディレクティブ宣言を探し、マッチする場合はLanguage属性の
	 * 指定であれば言語名を保持する
	 * @param line 解析対象のソース１行の処理未済の行末までの部分文字列
	 * @return ディレクティブ宣言にマッチすればその部分文字列、そうでなければnull
	 */
	protected String findDirectiveTag(final String line) {
		// lineからタグの抽出
		Pattern pattern = Pattern.compile("(" + SCRIPT_DIRECTIVE
				+ "[ \\t]?([ \\t]*[\\w]+(=['\"\\w#]+)?)*[ \\t]*)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.find()) {
			//タグが見つからない
			return null;
		}
		String target = matcher.group();
		//System.out.println("[DEBUG] Get Tag :"+target);
		String[] targetArray = target.split("[ \\t]+", 0);
		for (int j = 0; j < targetArray.length; j++) {
			String targetStr = targetArray[j].toUpperCase().replaceAll("['\">]", "");
			// language属性かチェック
			if (targetStr.startsWith(TAG_ATTR_LANG)) {
				this.tagLang = targetStr.substring(TAG_ATTR_LANG.length());
				//System.out.println("[DEBUG] Language : " + tagLang);
				break;
			}
		}
		return target;
	}

}
