package org.rascalmpl.core.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkDetector {
  public enum Type {
    SOURCE_LOCATION, HYPERLINK;
  }

  public static Type typeOf(String link) {
    return link.startsWith("|") ? Type.SOURCE_LOCATION : Type.HYPERLINK;
  }
  
  private static final Pattern findLinks = Pattern.compile(
      "(" // first alternative, Markdown
        + "\\[[^\\]]*\\]" // [Title]
        + "\\((?<markdown>[^\\)]*)\\)" // (link)
      + ")" 
      + "|" 
      + "(?<sourceLocation>" // or the other alternative, any rascal location
        + "\\|[^\\t-\\n\\r\\s\\|]*://[^\\t-\\n\\r\\s\\|]*\\|" // |location|
        + "(?:\\([^\\)]*\\))?" // (optional offset)
      + ")");


  public static String findAt(String input, int offset) {
    if (input == null || 0 > offset || offset > input.length()) {
      return null;
    }
    Matcher m = findLinks.matcher(input);
    while (m.find()) {
      if (m.start() > offset) {
        return null; // past the offset, so no link at that offset
      }
      if (m.end() < offset) {
        continue; // we aren't there yet
      }

      return m.group("markdown") != null ? m.group("markdown") : m.group("sourceLocation");
    }
    return null;
  }
  
}