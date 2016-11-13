package keisuke.count.diff.renderer;

import keisuke.count.diff.DiffFolderResult;

public interface Renderer {

	public byte[] render(DiffFolderResult root);

}
