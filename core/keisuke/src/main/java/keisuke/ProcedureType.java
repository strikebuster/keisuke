package keisuke;

/**
 * Enumerator for procedure type.
 */
public enum ProcedureType {

	/**
	 * CountReport
	 */
	COUNT_PROC {
		@Override
		public String toString() {
			return "CountReport";
		}
	},

	/**
	 * DiffReport
	 */
	DIFF_PROC {
		@Override
		public String toString() {
			return "DiffReport";
		}
	},

	/**
	 * MatchExtract
	 */
	MATCH_PROC {
		@Override
		public String toString() {
			return "MatchExtract";
		}
	};
}
