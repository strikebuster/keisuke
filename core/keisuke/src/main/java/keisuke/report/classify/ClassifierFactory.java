package keisuke.report.classify;

import static keisuke.report.classify.ClassifierConstant.*;

import keisuke.Classifier;

/**
 * Factory to make an instance of Classifier.
 */
public final class ClassifierFactory {

	public ClassifierFactory() { }

	/**
	 * デフォルトの定義を使って分類定義インスタンスを作成
	 * @param type classifier
	 * @return Instance of Classifier
	 */
	public Classifier create(final String type) {
		if (type == null) {
			return null;
		} else if (type.equals(CLASSIFY_EXTENSION)) {
			return new ExtensionClassifier();
		} else if (type.equals(CLASSIFY_LANGUAGE)) {
			return new LanguageClassifier();
		} else if (type.equals(CLASSIFY_LANGGROUP)) {
			return new LanguageClassifier(CLASSIFY_LANGGROUP);
		} else if (type.startsWith(CLASSIFY_FW)) {
			int pos = CLASSIFY_FW.length();
			if (type.length() > pos) {
				String fw = type.substring(pos);
				return new FrameworkClassifier(fw);
			}
			System.err.println("!! not specified <name>, expected argument is fw:<name>");
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
	public Classifier create(final String type, final String fname) {
		if (type == null) {
			return null;
		} else if (type.equals(CLASSIFY_EXTENSION)) {
			return new ExtensionClassifier();
		} else if (type.equals(CLASSIFY_LANGUAGE)) {
			LanguageClassifier lct = new LanguageClassifier();
			lct.customizeLanguageDefinitions(fname);
			return lct;
		} else if (type.equals(CLASSIFY_LANGGROUP)) {
			LanguageClassifier lct = new LanguageClassifier(CLASSIFY_LANGGROUP);
			lct.customizeLanguageDefinitions(fname);
			return lct;
		} else if (type.startsWith(CLASSIFY_FW)) {
			int pos = CLASSIFY_FW.length();
			if (type.length() > pos) {
				String fw = type.substring(pos);
				return new FrameworkClassifier(fw, fname);
			}
			System.err.println("!! not specified <name>, expected argument is fw:<name>");
			return null;
		} else {
			return null;
		}
	}
}
