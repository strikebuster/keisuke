package keisuke.count.diff.renderer;

import keisuke.count.diff.Renderer;
import keisuke.report.property.MessageDefine;

import static keisuke.count.option.CountOptionConstant.*;

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
		AbstractRenderer renderer = null;
		if (name != null && !name.isEmpty()) {
			if (name.equals(OPTVAL_TEXT)) {
				renderer = new TextRenderer();

			} else if (name.equals(OPTVAL_HTML)) {
				renderer = new HtmlRenderer();

			} else if (name.equals(OPTVAL_EXCEL)) {
				renderer = new ExcelRenderer();
			}
		} else {
			renderer = new TextRenderer();
		}
		return renderer;
	}

	/**
	 * 出力形式に応じたRendererの実装インスタンスを返す
	 * @param name 出力形式名称
	 * @param msgdef メッセージ定義インスタンス
	 * @return 差分計測結果の出力形式に応じたRendererインスタンス
	 */
	public static Renderer getRenderer(final String name, final MessageDefine msgdef) {
		AbstractRenderer renderer = (AbstractRenderer) getRenderer(name);
		if (renderer != null && msgdef != null) {
			renderer.setMessageMap(msgdef);
		}
		return renderer;
	}

}
