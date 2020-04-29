package org.jenkinsci.plugins.keisuke;

/**
 * Enumerator for Displaying Step kinds in graph
 */
public enum DisplayStepKindEnum {

	/** Executable code steps */
	CODE("code_only"),
	/** Executable code and comment steps */
	CODE_AND_COMMENT("with_comment"),
	/** All of executable code and comment and blank steps */
	CODE_AND_COMMENT_AND_BLANK("all_steps");

	// 文字列
	private String kindName = "";

	DisplayStepKindEnum(final String name) {
		this.kindName = name;
	}

	/**
	 * Gets string for this displaying step kind
	 * return string for this displaying step kind
	 */
	@Override
	public String toString() {
		return this.kindName;
	}

	/**
	 * Gets name of this displaying step kind
	 * @return name of this displaying step kind
	 */
	public String getValue() {
		return this.kindName;
	}

	/**
	 * Is this CODE.
	 * @return if this is CODE, returns true
	 */
	public boolean isCodeOnly() {
		if (this == CODE) {
			return true;
		}
		return false;
	}

	/**
	 * Is this CODE_AND_COMMENT.
	 * @return if this is CODE_AND_COMMENT, returns true
	 */
	public boolean isCodeAndComment() {
		if (this == CODE_AND_COMMENT) {
			return true;
		}
		return false;
	}

	/**
	 * Is this code with comment.
	 * alias for isCodeAndComment().
	 * @return if this is CODE_AND_COMMENT, returns true
	 */
	public boolean isWithComment() {
		return this.isCodeAndComment();
	}

	/**
	 * Is this CODE_AND_COMMENT_AND_BLANK.
	 * @return if this is CODE_AND_COMMENT_AND_BLANK, returns true
	 */
	public boolean isCodeAndCommentAndBlank() {
		if (this == CODE_AND_COMMENT_AND_BLANK) {
			return true;
		}
		return false;
	}

	/**
	 * Is this all steps.
	 * alias for isCodeAndCommentAndBlank().
	 * @return if this is CODE_AND_COMMENT_AND_BLANK, returns true
	 */
	public boolean isAllSteps() {
		return this.isCodeAndCommentAndBlank();
	}
}
