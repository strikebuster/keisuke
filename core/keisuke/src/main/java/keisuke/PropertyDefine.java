package keisuke;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties to be defined for keisuke command.
 * @author strikebuster
 *
 */
public class PropertyDefine {

	private static final String DEFAULTPROP = "/keisuke/default.properties";
	private Properties defaultProps = null;
	private Properties customProps = null;
	private Map<String, ReportColumn> columnMap = null;
	private Map<String, String> messageMap = null;

	/**
	 * デフォルト設定ファイルからデフォルトプロパティを設定するコンストラクタ
	 */
	public PropertyDefine() {
		this.defaultProps = new Properties();
		InputStream inputStream = null;
		String fname = null;
		try {
			fname = DEFAULTPROP;
			inputStream = this.getClass().getResourceAsStream(fname);
			this.defaultProps.load(inputStream);
		} catch (IOException e) {
			System.err.println("!! Read error : " + fname);
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * カスタマイズ定義ファイルを読み込みカスタマイズプロパティを設定する
	 * @param fname カスタマイズ定義ファイル名
	 */
	public void customizePropertyDefine(final String fname) {
		this.customProps = new Properties();
		InputStream inputStream = null;
		try {
			if (fname == null) {
				return;
			}
			inputStream = new FileInputStream(new File(fname));
			this.customProps.load(inputStream);
		} catch (IOException e) {
			System.err.println("!! Read error : " + fname);
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setProcProperties(final Properties props, final String prefix) {
		if (props == null) {
			return;
		}
		if (this.messageMap == null) {
			MessageDefine msgdef = new MessageDefine(prefix);
			this.messageMap = msgdef.getMessageMap();
		}
		Enumeration<?> en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String stridx = null;
			String label = null;
			if (key.startsWith(prefix)) {
				String value = props.getProperty(key);
				if (value.indexOf(',') > 0) {
					// 「列順,列タイトル」の形式
					String[] vals = value.split(",", 2);
					if (vals.length < 2) {
						// 書式エラー
						System.err.println("![WARN] illegal property : " + key + " = " + value);
						continue;
					}
					stridx = vals[0];
					label = vals[1];
				} else {
					// 「列順のみ」の形式
					stridx = value;
					label = this.messageMap.get(key);
				}
				try {
					int idx = Integer.parseInt(stridx);
					ReportColumn repcol = new ReportColumn(label, idx);
					columnMap.put(key, repcol);
				} catch (NumberFormatException nfex) {
					// 書式エラー
					System.err.println("![WARN] illegal property : " + key + " = " + value);
				}
			}
		}
	}

	/**
	 * メッセージproperties一式を返す
	 * @return メッセージproperties一式
	 */
	public Map<String, String> getMessageProperties() {
		if (this.messageMap == null) {
			System.err.println("![WARN] not ready for getting messageMap");
		}
		return this.messageMap;
	}

	private void setCountProperties() {
		this.columnMap = new HashMap<String, ReportColumn>();
		setProcProperties(this.defaultProps, "count.");
		setProcProperties(this.customProps, "count.");
	}

	/**
	 * CountReport用のproperties一式を返す
	 * @return CountReport用のproperties一式
	 */
	public Map<String, ReportColumn> getCountProperties() {
		if (this.columnMap == null) {
			setCountProperties();
		}
		return this.columnMap;
	}

	private void setDiffProperties() {
		this.columnMap = new HashMap<String, ReportColumn>();
		setProcProperties(this.defaultProps, "diff.");
		setProcProperties(this.customProps, "diff.");
	}

	/**
	 * DiffReport用のproperties一式を返す
	 * @return DiffReport用のproperties一式
	 */
	public Map<String, ReportColumn> getDiffProperties() {
		if (this.columnMap == null) {
			setDiffProperties();
		}
		return this.columnMap;
	}

}
