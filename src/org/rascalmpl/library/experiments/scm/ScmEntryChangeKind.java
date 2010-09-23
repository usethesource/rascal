package org.rascalmpl.library.experiments.scm;

public interface ScmEntryChangeKind {
	
	public ChangeCode getStatusCode();

	
	/**
     * Represents the type of the change applied to an item in SVN. 
     * This type can be one of the following: 
     * <span class="javastring">'M'</span> - Modified, 
     * <span class="javastring">'A'</span> - Added, 
     * <span class="javastring">'D'</span> - Deleted,
     * <span class="javastring">'R'</span> - Replaced (what means that the 
     * object is first deleted, then another object of the same name is 
     * added, all within a single revision).
     * 
     * @return a type of the change as a char label
     */
	public static enum SvnChangeKind implements ScmEntryChangeKind {
		ADDED('A', ChangeCode.ADDED), 
		DELETED('D', ChangeCode.DELETED),
		MODIFIED('M', ChangeCode.MODIFIED),
		REPLACED('R', ChangeCode.REPLACED);

		private final char code;
		private final ChangeCode statusCode;
		
		private SvnChangeKind(char code, ChangeCode statusCode) {
			this.code = code;
			this.statusCode = statusCode;
		}
		
		public ChangeCode getStatusCode() {
			return statusCode;
		}
		
		public char getCode() {
			return code;
		}
		
		public static SvnChangeKind from(char code) {
			for (SvnChangeKind svnChangeCode : values()) {
				if (svnChangeCode.code == code) {
					return svnChangeCode;
				}
			}
			throw new IllegalArgumentException("Don't have an SvnChangeCode for the code '" + code + "'");
		}
		
		@Override
		public String toString() {
			return Character.toString(code);
		}
		
	}
	
	//ACDMRTUXB
	public static enum GitChangeKind implements ScmEntryChangeKind {
		ADDED('A', ChangeCode.ADDED),
		COPIED('C', ChangeCode.COPIED), 
		DELETED('D', ChangeCode.DELETED),
		
		MODIFIED('M', ChangeCode.MODIFIED),
		RENAMED('R', ChangeCode.RENAMED),
		TYPE_CHANGED('T', ChangeCode.TYPE_CHANGED),
		
		UNKNOWN('U', ChangeCode.UNKNOWN),
		BROKEN('B', ChangeCode.BROKEN);

		private final char code;
		private final ChangeCode statusCode;
		
		private GitChangeKind(char code, ChangeCode statusCode) {
			this.code = code;
			this.statusCode = statusCode;
		}
		
		public ChangeCode getStatusCode() {
			return statusCode;
		}
		
		public char getCode() {
			return code;
		}
		
		public static GitChangeKind from(char code) {
			for (GitChangeKind gitChangeCode : values()) {
				if (gitChangeCode.code == code) {
					return gitChangeCode;
				}
			}
			throw new IllegalArgumentException("Don't have an GitChangeCode for the code '" + code + "'");
		}
		
		@Override
		public String toString() {
			return Character.toString(code);
		}
		
		public static ScmEntryChangeKind from(String input) {
			char firstChar = input.charAt(0);
			for (GitChangeKind gitChangeCode : values()) {
				if (gitChangeCode.code == firstChar) {
					if (input.length() > 1) {
						return new ChangeCodeValue(gitChangeCode.getStatusCode(), input.substring(1));
					}
					return gitChangeCode.getStatusCode();
				}
			}
			throw new IllegalArgumentException("Don't have an GitChangeCode for the input '" + input + "'");
		}
		
	}
	
	
	public static enum ChangeCode implements ScmEntryChangeKind {
		ADDED(), COPIED(), DELETED(),
		MODIFIED(), RENAMED(), REPLACED(),
		TYPE_CHANGED(),
		UNMERGED(), 
		UNKNOWN(), BROKEN();
		
		public ChangeCode getStatusCode() {
			return this;
		}
		
	}
	
	public static class ChangeCodeValue implements ScmEntryChangeKind {
		private final ChangeCode status;
		private final String value;
		
		private ChangeCodeValue(ChangeCode status, String value) {
			this.status = status;
			this.value = value;
		}

		public ChangeCode getStatusCode() {
			return status;
		}

		public String getStatusValue() {
			return value;
		}

		@Override
		public String toString() {
			return status.toString() + value;
		}
		
		
	}
}
