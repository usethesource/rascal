package org.rascalmpl.library.util;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Concise implementation of Semantic Versioning as described at http://semver.org
 * Many examples and tests inspired by https://github.com/npm/node-semver
 *
 */
public class SemVer {
	private int major = 0;			// >-0 when used as (major/minor/patch) version number, or -1 when used as wildcard
	private int minor = 0;
	private int patch = 0;
	private String prerelease = "";
	private String build = "";
	
	static final Pattern wildCard = Pattern.compile("[xX\\*]");
	static final Pattern number = Pattern.compile("0|([1-9][0-9]*)");
	static final Pattern part = Pattern.compile("(0|([1-9][0-9]*))|[-0-9A-Za-z]+");
	static final Pattern or = Pattern.compile("\\s*\\|\\|\\s*");
	static final Pattern spaces = Pattern.compile("\\s+");
	static final Pattern hyphen = Pattern.compile(" - ");
	static final Pattern startVersion = Pattern.compile("[xX\\*]|([0-9]+)");
	
	static final String rangeTokens = "((\\s+\\-\\s+)|\\+|\\-|\\~|\\^|<|>|=|(\\s+\\|\\|\\s+)|\\s)";
	static final String versionTokens = "(\\+|\\-)";
	static final String digits = "[0-9]+";
	static final String delimiter = "\\.";

	SemVer(int major, int minor, int patch, String prerelease, String build){
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.prerelease = prerelease;
		this.build = build;
	}
	
	public int getMajor() {
        return major;
    }
	
	public int getMinor() {
        return minor;
    }
	
	public int getPatch() {
        return patch;
    }
	
	public String getPrerelease() {
        return prerelease;
    }
	
	public String getBuild() {
        return build;
    }
	
	/**
	 * @param version Semantic Version string
	 * @throws RuntimeException when version is invalid
	 */
	public SemVer(String version){
		this(new Scanner(version.replaceAll(versionTokens, ".$1.")), false);
	}
	
	private SemVer(Scanner s){
		this(s, true);
	}
	
	private SemVer(Scanner s, boolean isPattern){
		if(!isPattern && s.hasNext("[=v]")){
			s.next("[=v]");
		}
		s.useDelimiter(delimiter);
		if(!s.hasNext(startVersion)){
			throw new RuntimeException("Invalid version string");
		}
		
		major = hasNextXorN(s) ? nextXorN(s) : -1;
		minor = hasNextXorN(s) ? nextXorN(s) : -1;
		patch = hasNextXorN(s) ? nextXorN(s) : -1;
		
		prerelease = s.hasNext("\\-") ? nextParts(s) : "";
		build = s.hasNext("\\+") ? nextParts(s) : "";
		if(isPattern){
			if(s.hasNext() && !(s.hasNext(spaces) || s.hasNext(hyphen) || s.hasNext(or))){
				throw new RuntimeException("Invalid version pattern");
			}
		} else {
			if(s.hasNext()){
				throw new RuntimeException("Invalid version string");
			}
			s.close();
		}
	}
	
	/**
	 * @return SemVer with incremented major version number and cleared prerelease and build strings
	 */
	public SemVer incMajor(){
		return new SemVer(major+1, 0, 0, "", "");
	}
	
	/**
	 * @return SemVer with incremented minor version number and cleared prerelease and build strings
	 */
	public SemVer incMinor(){
		return new SemVer(major, minor+1, 0, "", "");
	}
	
	/**
	 * @return SemVer with incremented patch version number and cleared prerelease and build strings
	 */
	public SemVer incPatch(){
		return new SemVer(major, minor, patch+1, "", "");
	}
	
	/****************************************************************************/
	/*	Utilities for parsing version strings									*/
	/****************************************************************************/
	
	private boolean hasNextXorN(Scanner s){
		return s.hasNext(wildCard) || s.hasNext(number);
	}
	
	private int nextXorN(Scanner s){
		if(s.hasNext(wildCard)){
			s.next();
			return -1;
		}
		if(s.hasNext(number)){
			return Integer.valueOf(s.next());
		}
		throw new RuntimeException("Expected nextXorN");
	}
	
	private boolean hasNextPart(Scanner s){
		return s.hasNext(part);
	}
	
	private String nextPart(Scanner s){
		return s.next(part);
	}
	
	private String nextParts(Scanner s){
		s.next(); // skip + or -;
		String parts = nextPart(s);
		while(hasNextPart(s)){
			parts += "." + nextPart(s);
		}
		return parts;
	}
	
	/****************************************************************************/
	/*	Comparisons																*/
	/****************************************************************************/
	
	/**
	 * @param other SemVer
	 * @return true, when both SemVers are equivalent (excludes build info)
	 */
	public boolean equivalentVersion(SemVer other){
		return equal(major, other.major) && equal(minor, other.minor) && equal(patch, other.patch) && prerelease.equals(other.prerelease);
	}
	
	/**
	 * @param other SemVer
	 * @return true, when both SemVers are equals (includes build info)
	 */
	public boolean equalVersion(SemVer other){
		return this.equivalentVersion(other) && build.equals(other.build);
	}
	
	private boolean matchEqualTo(SemVer other){
		return matchEqual(major, other.major) && matchEqual(minor, other.minor) && matchEqual(patch, other.patch);
	}
	
	private boolean matchEqual(int n, int m){
		return n >= 0 ? (m < 0 || n == m) : true;
	}
	
	private boolean equal(int n, int m){
		return n >= 0 && m >= 0 && n == m;
	}
	
	private boolean less(int n, int m) {
		return n >= 0 ? (m >= 0 && n < m) : false;
	}
	
	private boolean greater(int n, int m) {
		return n >= 0 ? (m >= 0 && n > m) : false;
	}
	
	/**
	 * @param other SemVer
	 * @return true if this SemVer is less than other
	 */
	public boolean lessVersion(SemVer other){
		return less(major, other.major) || less(minor, other.minor) || less(patch, other.patch) ||
				(!prerelease.equals(other.prerelease) && lessPrerelease(other));
	}
	
	/**
	 * @param other SemVer
	 * @return true if this SemVer is greater than other
	 */
	public boolean greaterVersion(SemVer other){
		return greater(major, other.major) || greater(minor, other.minor) || greater(patch, other.patch) || (!prerelease.equals(other.prerelease) && !lessPrerelease(other));
	}
	
	/**
	 * @param other SemVer
	 * @return true if this SemVer is less than or equal to other
	 */
	public boolean lessEqualVersion(SemVer other){
		return lessVersion(other) || equalVersion(other);
	}
	
	/**
	 * @param other SemVer
	 * @return true if this SemVer is greater than or equal to other
	 */
	public boolean greaterEqualVersion(SemVer other){
		return greaterVersion(other) ||
				equalVersion(other);
	}
	
	private boolean lessPrerelease(SemVer other){
		String[] pre1 = prerelease.split(delimiter);
		String[] pre2 = other.prerelease.split(delimiter);
		int n = Integer.min(pre1.length, pre2.length);
		for(int i = 0; i < n; i++){
			String id1 = pre1[i];
			String id2 = pre2[i];
			if(!id1.isEmpty() && id2.isEmpty()){
				return true;
			}
			if(id1.matches(digits)){
				if(id2.matches(digits)){
					if(Integer.valueOf(id1) < Integer.valueOf(id2)){
						return true;
					}
				} else {
					return true;
				}
			} else if(id2.matches(digits)){
				return true;
			}
			if(id1.compareTo(id2) < 0){
				return true;
			}
		}
		return pre1.length < pre2.length;
	}
	
	/****************************************************************************/
	/*	Satisfies																*/
	/****************************************************************************/
	
	/**
	 * Check that this SemVer instance satisfies a range-set as defined by:
	 * (See https://github.com/npm/node-semver):
	 * 
	 * <verbatim>
	 * range-set  ::= range ( logical-or range ) *
     * logical-or ::= ( ' ' ) * '||' ( ' ' ) *
     * range      ::= hyphen | simple ( ' ' simple ) * | ''
     * hyphen     ::= partial ' - ' partial
     * simple     ::= primitive | partial | tilde | caret
     * primitive  ::= ( '<' | '>' | '>=' | '<=' | '=' | ) partial
     * partial    ::= xr ( '.' xr ( '.' xr qualifier ? )? )?
     * xr         ::= 'x' | 'X' | '*' | nr
     * nr         ::= '0' | ['1'-'9'] ( ['0'-'9'] ) *
     * tilde      ::= '~' partial
     * caret      ::= '^' partial
     * qualifier  ::= ( '-' pre )? ( '+' build )?
     * pre        ::= parts
     * build      ::= parts
     * parts      ::= part ( '.' part ) *
     * part       ::= nr | [-0-9A-Za-z]+
     * 
	 * @param rangeSet to be checked
	 * @return true if this SemVer instance satisfies rangeSet
	 */
	public boolean satisfiesVersion(String rangeSet){
		try(Scanner s = new Scanner(insertDelimiters(rangeSet))){
			s.useDelimiter(delimiter);
			if(satisfiesRange(s)){
				if(!s.hasNext() || s.hasNext(or)){
					return true;
				} else {
					throw new RuntimeException("Invalid range string");
				}
			}
			while(s.hasNext(or)){
				s.next(or);
				if(satisfiesRange(s)){
					return true;
				}
			}
			return false;
		}
	}
	
	private String insertDelimiters(String input){
		return input.replaceAll(rangeTokens, ".$1.").replaceAll(delimiter + delimiter, delimiter);
	}
	
	private SemVer setWildCardsToZero(){
		if(major < 0) major = 0;
		if(minor < 0) minor = 0;
		if(patch < 0) patch = 0;
		return this;
	}
	
	private boolean satisfiesCaret(SemVer pat){
		if(this.matchEqualTo(pat)){
			return true;
		}
		if(pat.major > 0){
			return this.greaterEqualVersion(pat) && this.lessVersion(pat.incMajor());
		}
		if(pat.minor > 0){
			return this.greaterEqualVersion(pat) && this.lessVersion(pat.incMinor());
		}
		if(pat.patch > 0){
			return this.greaterEqualVersion(pat) && this.lessVersion(pat.incPatch());
		}
		return false;
	}
	
	private boolean satisfiesSimple(Scanner s){
		s.skip("\\s*");
		if(s.hasNext("~")){
			s.next();
			SemVer pat = new SemVer(s);
			if(pat.minor >= 0){ // Allow patch level changes
				pat.setWildCardsToZero();
				return this.greaterEqualVersion(pat)  && this.lessVersion(pat.incMinor());
			}
			pat.setWildCardsToZero();
			return this.greaterEqualVersion(pat) && this.lessVersion(pat.incMajor());
		}
		if(s.hasNext("\\^")){
			s.next();
			return satisfiesCaret(new SemVer(s));
		}
		if(s.hasNext(">")){
			s.next();
			if(s.hasNext("=")){
				s.next();
				return this.greaterEqualVersion(new SemVer(s));
			}
			return this.greaterVersion(new SemVer(s));
		}
		if(s.hasNext("<")){
			s.next();
			if(s.hasNext("=")){
				s.next();
				return this.lessEqualVersion(new SemVer(s));
			}
			return this.lessVersion(new SemVer(s));
		}
		if(s.hasNext("=")){
			s.next();
			return this.equalVersion(new SemVer(s));
		}
		if(s.hasNext("[xX\\*]|[0-9]")){
			return this.equalVersion(new SemVer(s));
		}
		throw new RuntimeException("Invalid range string");
	}
	
	private boolean satisfiesRange(Scanner s){
		if(s.hasNext("\\s*[\\<\\=\\>\\^\\~]")){
			while(s.hasNext("\\s*[\\<\\=\\>\\^\\~]")){
				if(!satisfiesSimple(s)){
					if(s.hasNext() && !s.hasNext(spaces)){
						throw new RuntimeException("Invalid range string");
					}
					return false;
				}
				if(s.hasNext(spaces)){
					s.next(spaces);
				}
			}
			return true;
		} else {
			SemVer from = new SemVer(s);

			if(s.hasNext(hyphen)){
				s.next(hyphen);
				from.setWildCardsToZero();
				SemVer to = new SemVer(s).setWildCardsToZero();
				return this.greaterEqualVersion(from) && this.lessEqualVersion(to);
			} else {
				if(!this.matchEqualTo(from)){
					return false;
				}
			}
		}
		return true;
	}
	
	public String toString(){
		return vnum(major) + "." + vnum(minor) + "." + vnum(patch) + (prerelease.isEmpty() ? "" : "-" + prerelease) + (build.isEmpty() ? "" : "+" + build);
	}
	
	private String vnum(int n){
		return n < 0 ? "*" : String.valueOf(n);
	}
}
