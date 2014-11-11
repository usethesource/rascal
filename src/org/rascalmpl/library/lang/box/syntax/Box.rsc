@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}

module lang::box::\syntax::Box

start syntax Main = Boxx WhitespaceAndComment*;

syntax Boxx
        = StrCon
        | BoxOperator box_operator "[" Boxx* list "]"
        | FontOperator font_operator "[" Boxx* list "]"
        | "LBL" "[" StrCon "," Boxx "]"
        | "REF" "[" StrCon "," Boxx "]"
        | "CNT" "[" StrCon "," StrCon "]"
     // | "O" SOptions "[" Boxx BoxString Boxx "]" 
 ;
 

lexical StrCon
	= [\"] StrChar* chars [\"] 
	;
	
lexical StrChar
    = "\\" [\" \' \< \> \\ b f n r t] 
    | ![\" \' \< \> \\] 
    ;
	

lexical NatCon = [0-9]+ ;

syntax BoxOperator
        = "A" AlignmentOptions alignments SpaceOption* options
        | "R"
        | "H" SpaceOption* options
        | "V" SpaceOption* options
        | "HV" SpaceOption* options
        | "HOV" SpaceOption* options
        | "I" SpaceOption* options
        | "WD"
        /*
        | "COMM"
        | "F" FontOption* options
        | "G" GroupOption* options
        | "SL" GroupOption* options
        */
        ;
 
syntax FontOperator
        = "KW"
        | "VAR"
        | "NUM"
        | "MATH"
        | "ESC"
        | "COMM"
        | "STRING"
        ;
 
syntax AlignmentOption
        = "l" SpaceOption* options
        | "c" SpaceOption* options
        | "r" SpaceOption* options
        ;
        
syntax AlignmentOptions
        = "(" {AlignmentOption ","}* ")"
        ;
        
syntax SpaceSymbol
        = "hs"
        | "vs"
        | "is"
        | "ts"
        ;
 
syntax SpaceOption
        = SpaceSymbol "=" NatCon
        ;

syntax Context
        = "H"
        | "V"
 ;
/*
syntax FontValue
        = NatCon
        | FontId
 ;
syntax FontOption
        =
        FontParam "=" FontValue
 ;
syntax FontParam
        = "fn"
        | "fm"
        | "se"
        | "sh"
        | "sz"
        | "cl"
 ;

 */
syntax GroupOption
        = "gs" "=" NatCon
        | "op" "=" BoxOperator
        ;
 
layout WhiteSpace =
            WhitespaceAndComment*
            !>> [\ \t\n\r]
            !>> "%"
            ;
            
lexical WhitespaceAndComment 
  = [\ \t\n\r]
  | "%" [!%]* "%"
  | "%%" [!\n]* "\n"
  ;
