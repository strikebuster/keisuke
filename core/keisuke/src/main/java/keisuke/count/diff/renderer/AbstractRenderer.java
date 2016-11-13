package keisuke.count.diff.renderer;

import keisuke.MessageDefine;
import keisuke.count.diff.DiffFolderResult;

public abstract class AbstractRenderer implements Renderer {

	protected MessageDefine msgdef;
	
	public AbstractRenderer() {	}
	
	public void setMessageDefine(MessageDefine md) {
		this.msgdef = md;
	}

	public String getMessageText(String key) {
		if (key == null) {
			return "";
		}
		if (this.msgdef == null) {
			this.msgdef = new MessageDefine("diff.render.");
		}
		return this.msgdef.getMessage(key);
	}

	public abstract byte[] render(DiffFolderResult result);

}
