package keisuke.count.format;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.format.ResultFormatter;
import keisuke.MessageDefine;

public abstract class AbstractFormatter implements ResultFormatter {

	protected MessageDefine msgdef = null;
	
	public AbstractFormatter() {
		this.msgdef = new MessageDefine("count.format.");
	}
	
	public String getMessageText(String key) {
		if (key == null) {
			return "";
		}
		return this.msgdef.getMessage(key);
	}
	
	public abstract byte[] format(CountResult[] results);
}
