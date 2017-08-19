package keisuke;

/**
 * Factory to make an instance which implements Classifier.
 * @author strikebuster
 *
 */
final class ClassifyFuncFactory {

	private ClassifyFuncFactory() { }

	/**
	 * デフォルトの定義を使って分類定義インスタンスを作成
	 * @param type classifier
	 * @return Instance of Classifier
	 */
	static IfClassifyFunc createClassifyFunc(final String type) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.OPTVAL_EXTENSION)) {
			return new ExtensionClassifyFunc();
		} else if (type.equals(CommonDefine.OPTVAL_LANGUAGE)) {
			return new LanguageClassifyFunc();
		} else if (type.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			return new LanguageClassifyFunc(CommonDefine.OPTVAL_LANGGROUP);
		} else if (type.startsWith(CommonDefine.OPTVAL_FW)) {
			int pos = CommonDefine.OPTVAL_FW.length();
			if (type.length() > pos) {
				String fw = type.substring(pos);
				return new FrameworkClassifyFunc(fw);
			}
			System.err.println("!! not specified <name> argument in the option -fw:<name>");
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 指定のXML定義ファイルを利用して分類定義インスタンスを作成
	 * @param type classifier
	 * @param fname xml file name which defines classifier
	 * @return Instance of Classifier
	 */
	static IfClassifyFunc createClassifyFunc(final String type, final String fname) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.OPTVAL_EXTENSION)) {
			return new ExtensionClassifyFunc();
		} else if (type.equals(CommonDefine.OPTVAL_LANGUAGE)) {
			LanguageClassifyFunc lct = new LanguageClassifyFunc();
			lct.customizeLanguageDefinitions(fname);
			return lct;
		} else if (type.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			LanguageClassifyFunc lct = new LanguageClassifyFunc(CommonDefine.OPTVAL_LANGGROUP);
			lct.customizeLanguageDefinitions(fname);
			return lct;
		} else if (type.startsWith(CommonDefine.OPTVAL_FW)) {
			int pos = CommonDefine.OPTVAL_FW.length();
			if (type.length() > pos) {
				String fw = type.substring(pos);
				return new FrameworkClassifyFunc(fw, fname);
			}
			System.err.println("!! not specified <name> argument in the option -fw:<name>");
			return null;
		} else {
			return null;
		}
	}
}
