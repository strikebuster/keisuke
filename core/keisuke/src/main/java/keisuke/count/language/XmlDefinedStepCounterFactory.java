package keisuke.count.language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import keisuke.count.StepCounter;
import keisuke.count.StepCutter;
import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.CommentExpr;
import keisuke.count.syntax.LabelHereDoc;
import keisuke.count.syntax.LineComment;
import keisuke.count.syntax.LiteralString;
import keisuke.count.syntax.ScriptBlock;
import keisuke.count.xmldefine.BlockCommentDefine;
import keisuke.count.xmldefine.CommentExpressionDefine;
import keisuke.count.xmldefine.LabelHereDocDefine;
import keisuke.count.xmldefine.LanguageCountRule;
import keisuke.count.xmldefine.LanguageDefineWithRuleFactory;
import keisuke.count.xmldefine.LanguageElementWithRule;
import keisuke.count.xmldefine.LineCommentDefine;
import keisuke.count.xmldefine.LiteralStringDefine;
import keisuke.count.xmldefine.ScriptBlockDefine;
import keisuke.report.classify.language.AbstractLanguageDefine;
import keisuke.report.classify.language.LanguageElement;
import keisuke.util.LogUtil;

/**
 * ステップカウンタのインスタンスを生成するファクトリ。
 * xmlファイルで言語ごとのルールを定義しCounterインスタンスを生成
 */
public class XmlDefinedStepCounterFactory extends AbstractLanguageDefine {

	private Map<String, GeneralStepCounter> langCounterMap = null;

	private static final String LANG_ASP = "ASP";
	private static final String LANG_ASPNET = "ASP.NET";
	private static final String LANG_COBOL = "COBOL";
	private static final String LANG_EMBEDDEDRUBY = "EmbeddedRuby";
	private static final String LANG_LITERATEHASKELL = "LiterateHaskell";
	private static final String LANG_LUA = "Lua";
	private static final String LANG_PERL = "Perl";
	private static final String LANG_RUBY = "Ruby";

	/**
	 * コンストラクタ
	 */
	public XmlDefinedStepCounterFactory() {
		this.makeLanguageDefineFactory();
		this.initialize();
	}

	/**
	 * LanguageDefineWithRuleFactoryを設定する
	 */
	@Override
	public void makeLanguageDefineFactory() {
		this.setLanguageDefineFactory(new LanguageDefineWithRuleFactory());
	}

	/**
	 * ユーザ定義のXMLファイルを読み込み定義に取り込む
	 * @param xmlname XMLファイル名
	 */
	public void appendCustomizeXml(final String xmlname) {
		if (xmlname == null) {
			return;
		}
		if (xmlname.length() > 0) {
			this.customizeLanguageDefinitions(xmlname);
		}
	}

	/**
	 * DiffCount用StepCutterインスタンス取得
	 * @param fileName カウント対象ファイル名
	 * @return Diffカウント用インスタンス
	 */
	public StepCutter getCutter(final String fileName) {
		StepCounter xcounter = this.getCounter(fileName);
		if (xcounter != null && xcounter instanceof StepCutter) {
			return (StepCutter) xcounter;
		}
		return null;
	}

	/**
	 * StepCount用StepCounterインスタンス取得
	 * @param fileName カウント対象ファイル名
	 * @return Stepカウント用インスタンス
	 */
	public StepCounter getCounter(final String fileName) {
		if (fileName == null) {
			return null;
		}
		if (this.extensionLanguageMap() == null) {
			LogUtil.warningLog("there is no language definition map.");
			return null;
		}
		if (this.langCounterMap == null) {
			this.langCounterMap = new HashMap<String, GeneralStepCounter>();
		}
		// 拡張子の取得
		String strext = FilenameUtils.getExtension(fileName);
		if (strext == null) {
			if (fileName.equals("Makefile")) {
				strext = "makefile";
			} else {
				return null;
			}
		}
		// 小文字に変換し、ピリオドを付与
		strext = "." + strext.toLowerCase();
		// XML定義されていた設定をMapから取得
		LanguageElement le = this.extensionLanguageMap().get(strext);
		return this.getCounterByLangElem(le);
	}

	private StepCounter getCounterByLangElem(final LanguageElement le) {
		if (le == null) { // サポート対象外の拡張子
			return null;
		} else if (!(le instanceof LanguageElementWithRule)) {
			LogUtil.warningLog("langExtMap has illegal class.");
			return null;
		}
		LanguageElementWithRule lewr = (LanguageElementWithRule) le;
		String langName = lewr.getName();
		String langGroup = lewr.getGroup();
		LanguageCountRule lcr = lewr.getCountRule();
		if (lcr == null) {
			LogUtil.warningLog("not defined count rules for " + langGroup + "." + langName);
			return null;
		}
		if (lcr.getSpecialized()) { // 専用カウンターの必要な言語
			//LogUtil.debugLog("specialized. lang=" + langName);
			return this.getSpecializedCounterFor(langName);
		}
		// 汎用カウンタ
		// 既に作成済みのカウンターが登録してあればそれを返す
		GeneralStepCounter existedCounter = this.langCounterMap.get(langName);
		if (existedCounter != null) {
			existedCounter.reset();
			//LogUtil.debugLog("use the existed counter. lang=" + langName);
			return existedCounter;
		}
		// 未登録なので当該言語用のカウンターを作成して返す
		// LanguageCountRuleから生成する
		GeneralStepCounter counter = this.createCounterWith(langName, lcr);
		// カウンターの登録と返却
		this.langCounterMap.put(langName, counter);
		//LogUtil.debugLog("insert into map. lang=" + langName);
		return counter;
	}

	private GeneralStepCounter createCounterWith(final String langName, final LanguageCountRule rule) {
		GeneralStepCounter counter = null;
		if (rule.getFunctional()) {
			//LogUtil.debugLog("functional. lang=" + langName);
			counter = new FunctionalLangCounter();
		} else if (rule.getLabelHereDoc()) {
			//LogUtil.debugLog("labelHereDoc. lang=" + langName);
			counter = new LabelHereDocScriptCounter(rule.getScriptlet());
		} else {
			//LogUtil.debugLog("generalStepCounter. lang=" + langName);
			counter = new GeneralStepCounter(rule.getScriptlet());
		}
		if (rule.getCaseInsense()) {
			//LogUtil.debugLog("caseInsense. lang=" + langName);
			counter.setCaseInsense(true);
		}
		if (rule.getIndentSense()) {
			//LogUtil.debugLog("indentSense. lang=" + langName);
			counter.setUsingIndentBlock(true);
		}
		// 既に定義済みの言語のルールを流用する場合
		String samelang = rule.getSameAs();
		if (samelang != null) {
			GeneralStepCounter another = this.langCounterMap.get(samelang);
			if (another == null) {
				// まだ流用元のカウンターが未作成だったのでここで作成
				//LogUtil.debugLog("sameAs lang=" + samelang
				//			+ " not exist, so search & make it.");
				LanguageElement le2 = this.searchLangExtMapByName(samelang);
				StepCounter another2 = this.getCounterByLangElem(le2);
				if (another2 == null) {
					LogUtil.warningLog(langName + " sameAs "
							+ samelang + " but no such lang.");
				} else {
					another = (GeneralStepCounter) another2;
				}
			} else {
				another.reset();
			}
			//LogUtil.debugLog("reuse the similar counter :"
			//			+ samelang + " for lang=" + langName);
			counter.copyFrom(another);
		}
		// 分類の設定
		counter.setFileType(langName);
		// スキップパターンの設定
		List<String> skipPtns = rule.getSkipPatterns();
		if (skipPtns != null) {
			for (String ptn : skipPtns) {
				//LogUtil.debugLog("addSkipPattern("+ ptn + ")");
				counter.addSkipPattern(ptn);
			}
		}
		// 行コメントの設定
		List<LineCommentDefine> lineComs = rule.getLineComments();
		if (lineComs != null) {
			for (LineCommentDefine lcd : lineComs) {
				String str = lcd.getStartDefineString();
				String esc = lcd.getEscapeString();
				if (esc == null) {
					//LogUtil.debugLog("addLineComment("+ str + ")");
					counter.addLineComment(new LineComment(str));
				} else {
					//LogUtil.debugLog("addLineComment("+ str + ", " + esc + ")");
					counter.addLineComment(new LineComment(str, esc));
				}
			}
		}
		// ブロックコメントの設定
		List<BlockCommentDefine> blockComs = rule.getBlockComments();
		if (blockComs != null) {
			for (BlockCommentDefine bcd : blockComs) {
				String start = bcd.getStart();
				String end = bcd.getEnd();
				boolean nest = bcd.getNest();
				//LogUtil.debugLog("addBlockComment("+ start + "," + end + "," + nest + ")");
				counter.addAreaComment(new AreaComment(start, end, nest));
			}
		}
		// コメント化式の設定
		List<CommentExpressionDefine> comExprs = rule.getCommentExpressions();
		if (comExprs != null) {
			for (CommentExpressionDefine ced : comExprs) {
				String start = ced.getStart();
				//LogUtil.debugLog("addCommentExpression("+ start + ")");
				counter.addAreaComment(new CommentExpr(start));
			}
		}
		// リテラル文字列の設定
		List<LiteralStringDefine> literalStrs = rule.getLiteralStrings();
		if (literalStrs != null) {
			for (LiteralStringDefine lsd : literalStrs) {
				String start = lsd.getStart();
				String end = lsd.getEnd();
				String escape = lsd.getEscape();
				//LogUtil.debugLog("addLiteralString("+ start + ","
				//			+ end + "," + escape + ")");
				counter.addLiteralString(new LiteralString(start, end, escape));
			}
		}
		// ラベル終了ヒアドキュメントの設定
		List<LabelHereDocDefine> hereDocs = rule.getLabelHereDocs();
		if (hereDocs != null) {
			for (LabelHereDocDefine lhdd : hereDocs) {
				String start = lhdd.getStart();
				String end = lhdd.getEnd();
				//LogUtil.debugLog("addLabelHereDoc("+ start + "," + end + ")");
				counter.addLiteralString(new LabelHereDoc(start, end));
			}
		}
		// Scriptletブロックの設定
		List<ScriptBlockDefine> scriptBlks = rule.getScriptBlocks();
		if (scriptBlks != null) {
			for (ScriptBlockDefine sbd : scriptBlks) {
				String start = sbd.getStart();
				String end = sbd.getEnd();
				//LogUtil.debugLog("addScriptBlock("+ start + "," + end + ")");
				counter.addScriptBlock(new ScriptBlock(start, end));
			}
		}
		return counter;
	}

	private StepCounter getSpecializedCounterFor(final String langName) {
		if (langName == null || langName.isEmpty()) {
			return null;
		}
		if (langName.equals(LANG_ASP) || langName.equals(LANG_ASPNET)) {
			return new ASPCounter(langName);
		} else if (langName.equals(LANG_COBOL)) {
			return new CobolCounter();
		} else if (langName.equals(LANG_LUA)) {
			return new LuaCounter();
		} else if (langName.equals(LANG_PERL)) {
			return new PerlCounter();
		} else if (langName.equals(LANG_RUBY)) {
			return new RubyCounter();
		} else if (langName.equals(LANG_EMBEDDEDRUBY)) {
			return new EmbeddedRubyCounter();
		} else if (langName.equals(LANG_LITERATEHASKELL)) {
			return new LiterateHaskellCounter();
		}
		LogUtil.warningLog("specialized language but we don't have the counter.");
		return null;
	}

	private LanguageElement searchLangExtMapByName(final String name) {
		if (this.extensionLanguageMap() == null || this.extensionLanguageMap().isEmpty()) {
			return null;
		}
		//LogUtil.debugLog("search in LangExtMap, lang=" + name);
		for (LanguageElement le : this.extensionLanguageMap().values()) {
			String langName = le.getName();
			//LogUtil.debugLog("map value lang=" + langName);
			if (name.equals(langName)) {
				return le;
			}
		}
		//LogUtil.debugLog("not found lang=" + name);
		return null;
	}
}
