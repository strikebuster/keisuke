package keisuke;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * Classifier about file extensions.
 *
 */
class ExtensionClassifyFunc implements IfClassifyFunc {

	protected ExtensionClassifyFunc() { }

	/** {@inheritDoc} */
	public String getClassifyName(final String strpath) {
		if (strpath == null) {
			return null;
		}
		String strext = FilenameUtils.getExtension(strpath);
		if (strext == null) {
			return null;
		}
		return strext.toLowerCase();
	}

	/** {@inheritDoc} */
	public String getClassifyNameForReport(final String classifyname) {
		return classifyname;
	}

	/** {@inheritDoc} */
	public List<String> getClassifyFixedList() {
		return null;
	}
}
