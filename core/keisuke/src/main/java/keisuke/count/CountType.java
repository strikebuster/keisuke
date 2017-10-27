package keisuke.count;

/**
 * Enumerator for count type.
 */
public enum CountType {

	/** StepCount */
	STEP_COUNT {
		@Override
		public String toString() {
			return "StepCount";
		}
	},

	/*** DiffCount */
	DIFF_COUNT {
		@Override
		public String toString() {
			return "DiffCount";
		}
	};
}
