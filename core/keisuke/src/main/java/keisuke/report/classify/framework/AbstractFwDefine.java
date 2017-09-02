package keisuke.report.classify.framework;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Framework definitions for keisuke.
 * It uses default definition file "/keisuke/framework.xml"
 */
public abstract class AbstractFwDefine {

	private static final String DEFAULT_FW_XMLFILE = "/keisuke/framework.xml";
	private String classify = null;
	private List<FwSpecificElement> fwSpecificList = null;
	private List<FwPatternElement> fwPatternList = null;

	protected AbstractFwDefine() { }

	/**
	 * 分類で指定するFramework名称を設定する
	 * @param fwname Frameworkを指定する名称
	 */
	protected void setFrameworkName(final String fwname) {
		this.classify = fwname;
	}

	/**
	 * 定義XMLファイルから取り込んだFwSpecificElementのListを返す
	 * @return FwSpecificElementのList
	 */
	protected List<FwSpecificElement> fwSpecificList() {
		return this.fwSpecificList;
	}

	/**
	 * 定義XMLファイルから取り込んだFwPatternElementのListを返す
	 * @return FwPatternElementのList
	 */
	protected List<FwPatternElement> fwPatternList() {
		return this.fwPatternList;
	}

	/**
	 * Framework定義をしたデフォルトXMLファイルを読んで、Framework定義を設定する
	 */
	protected void initialize() {
		try {
			URL urlfile = this.getClass().getResource(DEFAULT_FW_XMLFILE);
			String uriStr = urlfile.toURI().toString();
			this.initialize(uriStr);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Framework定義をしたXMLファイルを指定し、Framework定義を設定する
	 * @param xfile カスタマイズしたFramework定義のXMLファイル名
	 */
	protected void initialize(final String xfile) {
		XmlFrameworkDefine xmlDef = new XmlFrameworkDefine(xfile);
		this.fwSpecificList = xmlDef.createFwSpecificList(this.classify);
		if (this.fwSpecificList == null) {
			return;
		}
		//this.debugFwSpecificList();
		this.createFwPatternList();
		//this.debugFwPatternList();
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

	/**
	 * 指定されたパスがパターンマッチする定義パターンを探して返す
	 * @param path 対象のファイルパス
	 * @return 見つかればパターン定義インスタンス、なければnull
	 */
	protected FwPatternElement searchFwPatternElement(final String path) {
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

	/**
	 * DEBUG用Framework定義のリスト内容を表示する
	 */
	protected void debugFwSpecificList() {
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
	protected void debugFwPatternList() {
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
