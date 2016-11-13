package keisuke.count;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import jp.sf.amateras.stepcounter.StepCounter;
import keisuke.AbstractLanguageDefine;
import keisuke.LanguageElement;
import keisuke.count.xmldefine.BlockCommentDefine;
import keisuke.count.xmldefine.CommentExpressionDefine;
import keisuke.count.xmldefine.LabelHereDocDefine;
import keisuke.count.xmldefine.LanguageCountRule;
import keisuke.count.xmldefine.LanguageElementWithRule;
import keisuke.count.xmldefine.LineCommentDefine;
import keisuke.count.xmldefine.LiteralStringDefine;
import keisuke.count.xmldefine.ScriptBlockDefine;
import keisuke.count.xmldefine.XmlElementWithRuleFactory;


/**
 * ステップカウンタのインスタンスを生成するファクトリ。
 * このクラスを修正することで簡単に対応する形式を追加することができます。
 *
 * keisuke: xmlファイルで言語ごとのルールを定義しCounterインスタンスを生成
 */
public class XmlDefinedStepCounterFactory extends AbstractLanguageDefine {

	//private String customizeXml = null;
	private Map<String, DefaultStepCounter> langCounterMap = null;
	
	private final static String LANG_ASP = "ASP";
	private final static String LANG_ASPNET = "ASP.NET";
	private final static String LANG_COBOL = "COBOL";
	private final static String LANG_EMBEDDEDRUBY = "EmbeddedRuby";
	private final static String LANG_LITERATEHASKELL = "LiterateHaskell";
	private final static String LANG_LUA = "Lua";
	private final static String LANG_PERL = "Perl";
	private final static String LANG_RUBY = "Ruby";
	
	/** コンストラクタ */
	public XmlDefinedStepCounterFactory() {
		this.xmlElemFactory = new XmlElementWithRuleFactory();
		init();
	}
	
	/** ユーザ定義のXMLファイルを読み込み定義に取り込む */
	public void appendCustomizeXml(String xmlname) {
		if (xmlname == null) {
			return;
		}
		//this.customizeXml = xmlname;
		if (xmlname.length() > 0) {
			customizeLanguageDefinitions(xmlname);
		}
	}
	
	/** DiffCount用Cutterインスタンス取得 */
	public Cutter getCutter(String fileName) {
		StepCounter xcounter = getCounter(fileName);
		if (xcounter != null && xcounter instanceof Cutter) {
			return (Cutter)xcounter;
		}
		return null;
	}
	
	/** StepCount用StepCounterインスタンス取得 */
	public StepCounter getCounter(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (this.langExtMap == null) {
			System.err.println("![WARN] there is no language definition map.");
			return null;
		}
		if (this.langCounterMap == null) {
			this.langCounterMap = new HashMap<String, DefaultStepCounter>();
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
		LanguageElement le = this.langExtMap.get(strext);
		return getCounterByLangElem(le);
	}
	
	private StepCounter getCounterByLangElem(LanguageElement le) {
		if (le == null) {
			// サポート対象外の拡張子
			return null;
		} else if (!(le instanceof LanguageElementWithRule)) {
			System.err.println("![WARN] langExtMap has illegal class.");
			return null;
		}
		LanguageElementWithRule lewr = (LanguageElementWithRule)le;
		String langName = lewr.getName();
		String langGroup = lewr.getGroup();
		LanguageCountRule lcr = lewr.getCountRule();
		if (lcr == null) {
			System.err.println("![WARN] not defined count rules for " + langGroup + "." + langName);
			return null;
		}
		if (lcr.getSpecialized()) {
			//System.out.println("[DEBUG] specialized. lang=" + langName);
			// 専用カウンターの必要な言語
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
			} else {
				System.err.println("![WARN] specialized language but we don't have the counter.");
				return null;
			}
		}
		
		// 汎用カウンタ
		// 既に作成済みのカウンターが登録してあればそれを返す
		DefaultStepCounter madeCounter = this.langCounterMap.get(langName);
		if (madeCounter != null) {
			madeCounter.reset();
			//System.out.println("[DEBUG] use the existed counter. lang=" + langName);
			return madeCounter;
		}
		
		// 未登録なので当該言語用のカウンターを作成して返す
		DefaultStepCounter counter = null;
		if (lcr.getFunctional()) {
			//System.out.println("[DEBUG] functional. lang=" + langName);
			counter = new FunctionalLangCounter();
		} else if (lcr.getLabelHereDoc()) {
			//System.out.println("[DEBUG] labelHereDoc. lang=" + langName);
			counter = new LabelHereDocScriptCounter(lcr.getScriptlet());
		} else {
			//System.out.println("[DEBUG] defaultStepCounter. lang=" + langName);
			counter = new DefaultStepCounter(lcr.getScriptlet());
		}
		if (lcr.getCaseInsense()) {
			//System.out.println("[DEBUG] caseInsense. lang=" + langName);
			counter.setCaseInsenseTrue();
		}
		if (lcr.getIndentSense()) {
			//System.out.println("[DEBUG] indentSense. lang=" + langName);
			counter.setIndentOptTrue();
		}
		// 既に定義済みの言語のルールを流用する場合
		String samelang = lcr.getSameAs();
		if (samelang != null) {
			DefaultStepCounter another = this.langCounterMap.get(samelang);
			if (another == null) {
				// まだ流用元のカウンターが未作成だったのでここで作成
				//System.out.println("[DEBUG] sameAs lang=" + samelang + " not exist, so search & make it.");
				LanguageElement le2 = searchLangExtMapByName(samelang);
				StepCounter another2 = getCounterByLangElem(le2);
				if (another2 == null) {
					System.err.println("![WARN] " + langName + " sameAs " + samelang + " but no such lang.");
				} else {
					another = (DefaultStepCounter)another2;
				}
			} else {
				another.reset();
			}	
			//System.out.println("[DEBUG] reuse the similar counter :" + samelang + " for lang=" + langName);
			counter.copyFrom(another);
		}
		// 分類の設定
		counter.setFileType(langName);
		// スキップパターンの設定
		List<String> skipPtns = lcr.getSkipPatterns();
		if (skipPtns != null) {
			for (String ptn : skipPtns) {
				//System.out.println("[DEBUG] addSkipPattern("+ ptn + ")");
				counter.addSkipPattern(ptn);
			}
		}
		// 行コメントの設定
		List<LineCommentDefine> lineComs = lcr.getLineComments();
		if (lineComs != null) {
			for (LineCommentDefine lcd : lineComs) {
				String str = lcd.getStartDefineString();
				String esc = lcd.getEscapeString();
				if (esc == null) {
					//System.out.println("[DEBUG] addLineComment("+ str + ")");
					counter.addLineComment(new LineComment(str));
				} else {
					//System.out.println("[DEBUG] addLineComment("+ str + ", " + esc + ")");
					counter.addLineComment(new LineComment(str, esc));
				}
			}
		}
		// ブロックコメントの設定
		List<BlockCommentDefine> blockComs = lcr.getBlockComments();
		if (blockComs != null) {
			for (BlockCommentDefine bcd : blockComs) {
				String start = bcd.getStart();
				String end = bcd.getEnd();
				boolean nest = bcd.getNest();
				//System.out.println("[DEBUG] addBlockComment("+ start + "," + end + "," + nest + ")");
				counter.addAreaComment(new AreaComment(start, end, nest));
			}
		}
		// コメント化式の設定
		List<CommentExpressionDefine> comExprs = lcr.getCommentExpressions();
		if (comExprs != null) {
			for (CommentExpressionDefine ced : comExprs) {
				String start = ced.getStart();
				//System.out.println("[DEBUG] addCommentExpression("+ start + ")");
				counter.addAreaComment(new CommentExpr(start));
			}
		}
		// リテラル文字列の設定
		List<LiteralStringDefine> literalStrs = lcr.getLiteralStrings();
		if (literalStrs != null) {
			for (LiteralStringDefine lsd : literalStrs) {
				String start = lsd.getStart();
				String end = lsd.getEnd();
				String escape = lsd.getEscape();
				//System.out.println("[DEBUG] addLiteralString("+ start + "," + end + "," + escape + ")");
				counter.addLiteralString(new LiteralString(start, end, escape));
			}
		}
		// ラベル終了ヒアドキュメントの設定
		List<LabelHereDocDefine> hereDocs = lcr.getLabelHereDocs();
		if (hereDocs != null) {
			for (LabelHereDocDefine lhdd : hereDocs) {
				String start = lhdd.getStart();
				String end = lhdd.getEnd();
				//System.out.println("[DEBUG] addLabelHereDoc("+ start + "," + end + ")");
				counter.addLiteralString(new LabelHereDoc(start, end));
			}
		}
		// Scriptletブロックの設定
		List<ScriptBlockDefine> scriptBlks = lcr.getScriptBlocks();
		if (scriptBlks != null) {
			for (ScriptBlockDefine sbd : scriptBlks) {
				String start = sbd.getStart();
				String end = sbd.getEnd();
				//System.out.println("[DEBUG] addScriptBlock("+ start + "," + end + ")");
				counter.addScriptBlock(new ScriptBlock(start, end));
			}
		}
		// カウンターの登録と返却
		this.langCounterMap.put(langName, counter);
		//System.out.println("[DEBUG] insert into map. lang=" + langName);
		return counter;
	}
	
	
	private LanguageElement searchLangExtMapByName(String name) {
		if (this.langExtMap == null || this.langExtMap.isEmpty()) {
			return null;
		}
		//System.out.println("[DEBUG] search in LangExtMap, lang=" + name);
		for (LanguageElement le : this.langExtMap.values()) {
			String langName = le.getName();
			//System.out.println("[DEBUG] map value lang=" + langName);
			if (name.equals(langName)) {
				return le;
			}
		}
		//System.out.println("[DEBUG] not found lang=" + name);
		return null;
	}
}
