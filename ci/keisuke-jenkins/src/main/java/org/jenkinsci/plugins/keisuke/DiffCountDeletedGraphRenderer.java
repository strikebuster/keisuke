package org.jenkinsci.plugins.keisuke;

/**
 * Renderer for DiffCountDeletedGraph.
 */
class DiffCountDeletedGraphRenderer extends AbstractDiffCountGraphRenderer {

	private static final long serialVersionUID = 0L;  // since ver.2.0.0

	DiffCountDeletedGraphRenderer(final String path) {
		super(path);
	}

	@Override
	protected void initDiffKind() {
		this.setDiffLabel(Messages.deleted());
	}
}
