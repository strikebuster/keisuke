package org.jenkinsci.plugins.keisuke;

/**
 * Renderer for DiffCountAddedGraph.
 */
class DiffCountAddedGraphRenderer extends AbstractDiffCountGraphRenderer {

	private static final long serialVersionUID = 0L;  // since ver.2.0.0

	DiffCountAddedGraphRenderer(final String path) {
		super(path);
	}

	@Override
	void initDiffKind() {
		this.setDiffLabel(Messages.added());
	}
}
