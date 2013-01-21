package org.rascalmpl.uri;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkifiedString {
	
	private List<Integer> linkOffsets;
	private List<Integer> linkLengths;
	private List<String> linkTargets;
	private String linkedString; 
	
	
	public String getString() {
		return linkedString;
	}
	
	public boolean containsLinks() {
		return linkOffsets != null && linkOffsets.size() > 0;
	}
	
	public int linkCount() {
		return linkOffsets == null ? 0 : linkOffsets.size();
	}
	
	public int linkOffset(int index) {
		if (linkOffsets == null) {
			throw new IllegalStateException("There are no links"); 
		}
		return linkOffsets.get(index);	
	}
	
	public int linkLength(int index) {
		if (linkLengths == null) {
			throw new IllegalStateException("There are no links"); 
		}
		return linkLengths.get(index);	
	}
	public String linkTarget(int index) {
		if (linkTargets == null) {
			throw new IllegalStateException("There are no links"); 
		}
		return linkTargets.get(index);	
	}
	

	// scan for both \uE007 + [Title](url) and all |loc|'s
	private static final Pattern findLinks = Pattern.compile(
			"(?:\\uE007" // first alternative, Markdown
				+ "\\[([^\\]]*)\\]" // [Title]
				+ "\\(([^\\)]*)\\)" // (link)
			+ ")"
			+ "|"
			+ "(" // or the other alternative, any rascal location 
				+ "\\|[^\\t-\\n\\r\\ \\<\\|]*://[^\\t-\\n\\r\\ \\<\\|]*\\|" // |location|
				+ "(?:\\([^\\)]*\\))?" // (optional offset)
			+ ")");
	
	public LinkifiedString(String input) {
		linkedString = input;
		
		StringBuffer sb = null; 
		Matcher m = findLinks.matcher(input);
		while (m.find()) {
			if (linkOffsets == null) {
				linkOffsets = new ArrayList<Integer>();
				linkLengths = new ArrayList<Integer>();
				linkTargets = new ArrayList<String>();
			}
			if (m.group(3) == null) {
				if (sb == null) {
					// first link found, so we have to recut the string
					sb = new StringBuffer(input.length());
				}
				// markdown link
				String name =  m.group(1) + "\uD83D\uDD17" ;
				String url = m.group(2);
				linkTargets.add(url);
				m.appendReplacement(sb, name);
				linkOffsets.add((sb.length() - name.length()) + 1);
				linkLengths.add(name.length());
			}
			else {
				// source location
				String loc = m.group(3);
				linkTargets.add(loc);
				if (sb != null) {
					// we are re-appending
					m.appendReplacement(sb, loc);
					linkOffsets.add((sb.length() - loc.length()) + 1);
				}
				else {
					linkOffsets.add(m.start() + 1);
				}
				linkLengths.add(loc.length());
			}
		}
		if (sb != null) {
			// we have a new string 
			m.appendTail(sb);
			linkedString = sb.toString();
		}
	}
}
