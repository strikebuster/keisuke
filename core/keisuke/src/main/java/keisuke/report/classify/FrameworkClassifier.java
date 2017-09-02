package keisuke.report.classify;

import java.util.ArrayList;
import java.util.List;

import keisuke.Classifier;
import keisuke.report.classify.framework.AbstractFwDefine;
import keisuke.report.classify.framework.FwPatternElement;
import keisuke.report.classify.framework.FwSpecificElement;

/**
 * Classifier about artifact type on framework.
 */
class FrameworkClassifier extends AbstractFwDefine implements Classifier {

	private static final int MAX_NUMBER = 999;

	/**
	 * Framework定義をしたデフォルトXMLファイルから、Frameworkを識別する名称を指定して
	 * その定義内容を設定するコンストラクタ
	 * @param fwname Frameworkを指定する名称
	 */
	protected FrameworkClassifier(final String fwname) {
		super();
		this.setFrameworkName(fwname);
		this.initialize();
	}

	/**
	 * Framework定義をしたXMLファイルを指定し、その中で記述されたFrameworkを識別する
	 * 名称を指定して、その定義内容を設定するコンストラクタ
	 * @param fwname Frameworkを指定する名称
	 * @param fname カスタマイズしたFramework定義のXMLファイル名
	 */
	protected FrameworkClassifier(final String fwname, final String fname) {
		super();
		this.setFrameworkName(fwname);
		this.initialize(fname);
	}

	/** {@inheritDoc} */
	public String getClassifyName(final String strpath) {
		if (strpath == null) {
			return null;
		}
		FwPatternElement fwPatternElem = this.searchFwPatternElement(strpath);
		if (fwPatternElem == null) {
			return attachNumberingName(MAX_NUMBER, "Others");
		} else {
			return fwPatternElem.getName();
		}
	}

	/** {@inheritDoc} */
	public String getClassifyNameForReport(final String classifyname) {
		return detachNumberingName(classifyname);
	}

	/** {@inheritDoc} */
	public List<String> getClassifyFixedList() {
		if (this.fwSpecificList() == null || this.fwSpecificList().isEmpty()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (FwSpecificElement fwSpecElem : this.fwSpecificList()) {
			list.add(fwSpecElem.getName());
		}
		return list;
	}

	private static String attachNumberingName(final int num, final String name) {
		return (String.format("%03d", num) + "#" + name);
	}

	private static String detachNumberingName(final String numname) {
		if (numname == null) {
			return null;
		}
		int pos = numname.indexOf('#');
		if (pos < 0 || pos == numname.length() - 1) {
			return numname;
		}
		return numname.substring(pos + 1);
	}

}
