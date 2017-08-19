package keisuke.count.diff.renderer;

import keisuke.MessageDefine;

/**
 * 出力形式に応じたRendererの実装クラスを生成するファクトリ
 */
public final class RendererFactory {

	private RendererFactory() { }

	/**
	 * 出力形式に応じたRendererの実装インスタンスを返す
	 * @param name 出力形式名称
	 * @return 差分計測結果の出力形式に応じたRendererインスタンス
	 */
	public static Renderer getRenderer(final String name) {
		AbstractRenderer render = null;
		if (name != null) {
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

	/**
	 * 出力形式に応じたRendererの実装インスタンスを返す
	 * @param name 出力形式名称
	 * @param md メッセージ定義インスタンス
	 * @return 差分計測結果の出力形式に応じたRendererインスタンス
	 */
	public static Renderer getRenderer(final String name, final MessageDefine md) {
		AbstractRenderer render = (AbstractRenderer) getRenderer(name);
		render.setMessageDefine(md);
		return render;
	}

}
