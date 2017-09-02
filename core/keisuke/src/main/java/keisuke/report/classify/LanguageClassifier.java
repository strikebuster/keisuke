package keisuke.report.classify;

import static keisuke.report.classify.ClassifierConstant.*;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import keisuke.Classifier;
import keisuke.report.classify.language.AbstractLanguageDefine;
import keisuke.report.classify.language.LanguageSpecifics;

/**
 * Classifier about source languages.
 *
 */
class LanguageClassifier extends AbstractLanguageDefine implements Classifier {

	private String classify = CLASSIFY_LANGUAGE;

	/**
	 * 言語分類（デフォルト）のコンストラクタ
	 */
	protected LanguageClassifier() {
		super();
		this.makeLanguageDefineFactory();
		this.initialize();
	}

	/**
	 * 言語グループ分類の場合のコンストラクタ
	 * @param str 'group'
	 */
	protected LanguageClassifier(final String str) {
		if (str != null && str.equals(CLASSIFY_LANGGROUP)) {
			this.classify = str;
		}
		this.makeLanguageDefineFactory();
		this.initialize();
	}

	/**
	 * 言語分類の区分を返す
	 * @return classifier string 'language' or 'group'
	 */
	protected String classifier() {
		return this.classify;
	}

	/** {@inheritDoc} */
	public String getClassifyName(final String strpath) {
		if (strpath == null) {
			return null;
		}
		if (this.extensionLanguageMap() == null) {
			System.err.println("![WARN] there is no language definition map.");
			return null;
		}
		// 拡張子の取得
		String strext = FilenameUtils.getExtension(strpath);
		if (strext == null) {
			return null;
		}
		// 小文字に変換し、ピリオドを付与
		strext = "." + strext.toLowerCase();
		// XML定義されていた設定をMapから取得
		LanguageSpecifics langspec = this.extensionLanguageMap().get(strext);
		if (langspec == null) {
			return "Unsupport";
		} else if (this.classify.equals(CLASSIFY_LANGGROUP)) {
			return langspec.getGroup();
		} else {
			return langspec.getName();
		}
	}

	/** {@inheritDoc} */
	public String getClassifyNameForReport(final String classifyname) {
		return classifyname;
	}

	/** {@inheritDoc} */
	public List<String> getClassifyFixedList() {
		return null;
	}

}
