package keisuke;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Classifier about artifact type on framework.
 *
 */
class FrameworkClassifyFunc implements IfClassifyFunc {

	private static final String DEFAULT_FW_XMLFILE = "/keisuke/framework.xml";
	private String classify = null;
	private List<FwSpecificElement> fwSpecificList = null;
	private List<FwPatternElement> fwPatternList = null;

	/**
	 * Framework定義をしたデフォルトXMLファイルから、Frameworkを識別する名称を指定して
	 * その定義内容を設定するコンストラクタ
	 * @param fwname Frameworkを指定する名称
	 */
	protected FrameworkClassifyFunc(final String fwname) {
		this.classify = fwname;
		try {
			URL urlfile = this.getClass().getResource(DEFAULT_FW_XMLFILE);
			String uriStr = urlfile.toURI().toString();
			initialize(uriStr);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Framework定義をしたXMLファイルを指定し、その中で記述されたFrameworkを識別する
	 * 名称を指定して、その定義内容を設定するコンストラクタ
	 * @param fwname Frameworkを指定する名称
	 * @param fname カスタマイズしたFramework定義のXMLファイル名
	 */
	protected FrameworkClassifyFunc(final String fwname, final String fname) {
		this.classify = fwname;
		initialize(fname);
	}

	private void initialize(final String xfile) {
		XmlFrameworkDefine xmlDef = new XmlFrameworkDefine(xfile);
		this.fwSpecificList = xmlDef.createFwSpecificList(this.classify);
		if (this.fwSpecificList == null) {
			return;
		}
		//debugFwSpecificList();
		createFwPatternList();
		//debugFwPatternList();
	}

	private void createFwPatternList() {
		// this.fwSpecificList -> this.fwPatternList
		this.fwPatternList = new ArrayList<FwPatternElement>();
		for (FwSpecificElement fwSpecElem : this.fwSpecificList) {
			int size = fwSpecElem.getPatternStrings().size();
			for (int i = 0; i < size; i++) {
				FwPatternElement fwPatternElem = new FwPatternElement(fwSpecElem, i);
				this.fwPatternList.add(fwPatternElem);
			}
		}
		Collections.sort(this.fwPatternList);
		Collections.reverse(this.fwPatternList);
	}

	/** {@inheritDoc} */
	public String getClassifyName(final String strpath) {
		final int maxNumber = 999;
		if (strpath == null) {
			return null;
		}
		FwPatternElement fwPatternElem = searchFwPatternElement(strpath);
		if (fwPatternElem == null) {
			return attachNumberingName(maxNumber, "Others");
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
		if (this.fwSpecificList == null || this.fwSpecificList.isEmpty()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (FwSpecificElement fwSpecElem : this.fwSpecificList) {
			list.add(fwSpecElem.getName());
		}
		return list;
	}

	private FwPatternElement searchFwPatternElement(final String path) {
		if (path == null) {
			return null;
		}
		if (this.fwPatternList == null) {
			return null;
		}
		for (FwPatternElement fwPatternElem : this.fwPatternList) {
			Matcher matcher = fwPatternElem.getPattern().matcher(path);
			if (matcher.matches()) {
				return fwPatternElem;
			}
		}
		return null;
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

	/**
	 * DEBUG用Framework定義のリスト内容を表示する
	 */
	public void debugFwSpecificList() {
		System.out.println("[DEBUG] fw:" + this.classify + ", fwSpecificList contains ");
		if (this.fwSpecificList == null) {
			System.out.println("[DEBUG] null ");
			return;
		}
		for (FwSpecificElement fwSpecElem : this.fwSpecificList) {
			System.out.println(fwSpecElem.debug());
		}
	}

	/**
	 * DEBUG用Frameworkの成果物タイプパターンの内容を表示する
	 */
	public void debugFwPatternList() {
		System.out.println("[DEBUG] fw:" + this.classify + ", fwPatternList contains ");
		if (this.fwPatternList == null) {
			System.out.println("[DEBUG] null ");
			return;
		}
		for (FwPatternElement fwPatternElem : this.fwPatternList) {
			System.out.println(fwPatternElem.debug());
		}
	}
}
