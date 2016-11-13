package keisuke.count.diff.renderer;

import keisuke.MessageDefine;

/**
 * 
 * keisuke: パッケージの変更
 * Rendererの実装の中のMessageDefineを設定するメソッド追加
 *
 */
public class RendererFactory {

	public static Renderer getRenderer(String name) {
		AbstractRenderer render = null;
		if(name != null){
			if (name.equals("text")) {
				render = new SimpleRenderer();

			} else if (name.equals("html")) {
				render = new HTMLRenderer();

			} else if (name.equals("excel")) {
				render = new ExcelRenderer();
			}
		} else {
			render = new SimpleRenderer();
		}
		return render;
	}
	
	public static Renderer getRenderer(String name, MessageDefine md) {
		AbstractRenderer render = (AbstractRenderer) getRenderer(name);
		render.setMessageDefine(md);
		return render;
	}

}
