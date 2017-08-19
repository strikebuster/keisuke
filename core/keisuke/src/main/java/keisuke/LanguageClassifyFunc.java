package keisuke;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * Classifier about source languages.
 *
 */
class LanguageClassifyFunc extends AbstractLanguageDefine implements IfClassifyFunc {

	private String classify = CommonDefine.OPTVAL_LANGUAGE;

	/**
	 * 言語分類（デフォルト）のコンストラクタ
	 */
	protected LanguageClassifyFunc() {
		super();
		this.setLanguageDefineFactory(new XmlElementFactory());
		this.initialize();
	}

	/**
	 * 言語グループ分類の場合のコンストラクタ
	 * @param str 'group'
	 */
	protected LanguageClassifyFunc(final String str) {
		if (str != null && str.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			this.classify = str;
		}
		this.setLanguageDefineFactory(new XmlElementFactory());
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
		LanguageElement le = this.extensionLanguageMap().get(strext);
		if (le == null) {
			return "Unsupport";
		} else if (this.classify.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			return le.getGroup();
		} else {
			return le.getName();
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
