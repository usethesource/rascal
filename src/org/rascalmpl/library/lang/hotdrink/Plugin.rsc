module lang::hotdrink::Plugin

import lang::hotdrink::Adam;
import lang::hotdrink::Eve;

import util::IDE;
import ParseTree;

public void main() {
  registerLanguage("Adam", "adam", start[TranslationUnit](str src, loc l) {
    return parse(#start[TranslationUnit], src, l);
  });
  registerLanguage("Eve", "eve", start[LayoutSpecifier](str src, loc l) {
    return parse(#start[LayoutSpecifier], src, l);
  });
}