module experiments::Compiler::Examples::Tst3

import lang::rascal::\syntax::Rascal;
//import lang::rascal::grammar::definition::Literals;
import ParseTree;
import IO;

public str character(StringCharacter c) {
  switch (c) {
    //case [StringCharacter] /^<ch:[^"'\\\>\<]>/        : return "<ch>";
    //case [StringCharacter] /^\\n/ : return "\n";
    //case [StringCharacter] /^\\t/ : return "\t";
    //case [StringCharacter] /^\\b/ : return "\b";
    //case [StringCharacter] /^\\r/ : return "\r";
    //case [StringCharacter] /^\\f/ : return "\f";
    //case [StringCharacter] /^\\\>/ : return "\>";
    //case [StringCharacter] /^\\\</ : return "\<";
    case [StringCharacter] /^\\<esc:["'\\ ]>/        : return "<esc>";
    //case [StringCharacter] /^\\u<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return stringChar(toInt("0x<hex>"));
    //case [StringCharacter] /^\\U<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return stringChar(toInt("0x<hex>"));
    //case [StringCharacter] /^\\a<hex:[0-7][0-9a-fA-F]>/ : return stringChar(toInt("0x<hex>")); 
    //case [StringCharacter] /^\n[ \t]* \'/            : return "\n";
    default: throw "missed a case <c>";
  }
}

value main(list[value] args) = character((StringCharacter) `\\'`); //== "\'";