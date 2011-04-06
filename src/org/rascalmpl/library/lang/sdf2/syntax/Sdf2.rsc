@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl - CWI}
module lang::sdf2::syntax::Sdf2

syntax Sort = lex OneChar: [A-Z] |
              lex MoreChars: [A-Z] [A-Za-z0-9\-]* [A-Za-z0-9]
              - "LAYOUT"
              # [A-Za-z0-9]
              ;

syntax Syms = Sym*
              // - StrCon "(" {Sym ","}* ")" 
              ;

syntax NatCon = lex Digits: [0-9]+
                # [0-9]
                ;

syntax NumChar = lex Digits: [\\] [0-9]+
                 # [0-9]
                 ;

start syntax SDF = Definition: "definition" Definition
                   ;

syntax Character = Numeric: NumChar |
                   short: ShortChar |
                   top: "\\TOP" |
                   eof: "\\EOF" |
                   bot: "\\BOT" |
                   label_start: "\\LABEL_START"
                   ;

syntax ShortChar = lex Regular: [a-zA-Z0-9] |
                   lex Escaped: [\\] ![A-Za-mo-qsu-z0-9] // -\0-\31
                   ;

syntax Renaming = Sym: Sym "=\>" Sym |
                  production: Prod "=\>" Prod
                  ;

syntax Renamings = Renamings: "[" Renaming* "]"
                   ;

syntax IdCon = lex Default: [A-Za-z] [A-Za-z\-0-9]*
               # [A-Za-z\-0-9]
               ;

syntax Class = SimpleCharClass: "[" OptRanges "]" |
                   Bracket: "(" Class ")" |
                   Comp: "~" Class >
                   Diff: Class "/" Class >
                   left ISect: Class "/\\" Class >
                   left Union: Class "\\/" Class
                   ;

syntax Range = Character |
                   Range: Character "-" Character
                   ;

syntax Ranges = Range |
                    right Conc: Ranges Ranges |
                    Bracket: "(" Ranges ")"
                    ;

syntax OptRanges = Absent: |
                       Present: Ranges
                       ;

syntax Attribute = Id: "id" "(" ModuleName ")" |
                   Term: ATermAttribute |
                   Reject: "reject" |
                   Prefer: "prefer" |
                   Avoid: "avoid" |
                   Bracket: "bracket" |
                   Assoc: Assoc
                   ;

syntax ATermAttribute = Default: ATerm a  
                        - "reject"
                        - "prefer"
                        - "avoid"
                        - "bracket"
                        - "id" "(" ModuleName ")"
                        ;

syntax Attrs = Attrs: "{" {Attribute ","}* "}" |
                    NoAttrs: 
                    ;

syntax Prod = Prod: Syms "-\>" Sym Attrs |
                    PrefixFun: FunctionName "(" {Sym ","}* ")" Attrs // Avoid
                    ;

syntax Prods = Prod*
                     ;

syntax Grammar = Bracket: "(" Grammar ")" |
                 Aliases: "aliases" Aliases |
                 Sorts: "sorts" Syms |
                 ImpSection: ImpSection |
                 Syntax: "syntax" Prods |
                 KernalStartSyms: "start-symbols" Syms |
                 Variables: "variables" Prods |
                 Priorities: "priorities" Priorities |
                 Restrictions: "restrictions" Restrictions |
                 LexicalSyntax: "lexical" "syntax" Prods |
                 LexicalStartSyms: "lexical" "start-symbols" Syms |
                 LexicalVariables: "lexical" "variables" Prods |
                 LexicalPriorities: "lexical" "priorities" Priorities |
                 LexicalRestrictions: "lexical" "restrictions" Restrictions |
                 ContextFreeSyntax: "context-free" "syntax" Prods |
                 ContextFreeStartSyms: "context-free" "start-symbols" Syms |
                 ContextFreePriorities: "context-free" "priorities" Priorities |
                 ContextFreeRestrictions: "context-free" "restrictions" Restrictions |
                 EmptyGrammar: "(/)" |
                 assoc ConcGrammars: Grammar Grammar
                 ;

syntax Label = Quoted: StrCon |
               IdCon: IdCon
               - "left"
               - "right"
               - "assoc"
               - "non-assoc"
               ;

syntax Sym = Sort: Sort |
                Lit: StrCon |
                CILit: SingleQuotedStrCon |
                Class: Class |
                Layout: "LAYOUT" |
                Lifting: "`" Sym "`" |
                Empty: "(" ")" |
                Bracket: "(" Sym ")" |
                Seq: "(" Sym Sym+ ")" |
                Strategy: "(" Sym "-\>" Sym ")" |
                Func: "(" Syms "=\>" Sym ")" |
                Opt: Sym "?" |
                Iter: Sym "+" |
                IterStar: Sym "*" |
                IterSep: "{" Sym Sym "}" "+" |
                IterStarSep: "{" Sym Sym "}" "*" |
                Start: "\<START\>" |
                FileStart: "\<Start\>" |
                CF: "\<" Sym "-CF" "\>" |
                Lex: "\<" Sym "-LEX" "\>" |
                Varsym: "\<" Sym "-VAR" "\>" |
                Tuple: "\<" Sym "," {Sym ","}+ "\>" |
                ParameterizedSort: Sort "[[" {Sym ","}+ "]]" >
                right Alt: Sym "|" Sym >
                Label ":" Sym
                ;

layout LAYOUTLIST = LAYOUT*
                    # [\ \t\n\r%]
                    ;

syntax LAYOUT = lex Whitespace: [\ \t\n\r] |
                lex @category="Comment" Line: "%%" ![\n]* [\n] |
                lex @category="Comment" Nested: "%" ![%\n] "%"
                ;

syntax Alias = Alias: Sym "-\>" Sym
               ;

syntax Aliases = Alias*
                 ;

syntax StrChar = lex NewLine: [\\] [n] | // "\\n"
                 lex Tab: [\\] [t] | // "\\t"
                 lex Quote: [\\] [\"] | //  "\\\""
                 lex Backslash: [\\] [\\] | // "\\\\"
                 lex Decimal: [\\] [0-9] [0-9] [0-9] | // "\\" [0-9] [0-9] [0-9]
                 lex Normal: ![\n\t\"\\] // -\0-\31
                 ;

syntax StrCon = lex Default: [\"] StrChar* [\"]
                ;

syntax FunctionName = UnquotedFun: IdCon |
                      QuotedFun: StrCon
                      ;

syntax SingleQuotedStrCon = lex Default: [\'] SingleQuotedStrChar* [\']
                            ;

syntax SingleQuotedStrChar = lex NewLine: [\\] [n] | // "\\n"
                             lex Tab: [\\] [t] | // "\\t"
                             lex Quote: [\\] [\'] | //  "\\\'"
                             lex Backslash: [\\] [\\] | // "\\\\"
                             lex Decimal: [\\] [0-9] [0-9] [0-9] | // "\\" [0-9] [0-9] [0-9]
                             lex Normal: ![\n\t\'\\] // -\0-\31
                             ;

syntax RealCon = RealCon: IntCon "." NatCon OptExp
                 ;

syntax OptExp = Present: "e" IntCon |
                Absent: 
                ;

start syntax Module = Module: "module" ModuleName ImpSection* Sections
                      ;

syntax ModuleName = Unparameterized: ModuleId |
                    Parameterized: ModuleId "[" Syms "]"
                    - "aliases"
                    - "lexical"
                    - "priorities"
                    - "context-free"
                    - "definition"
                    - "syntax"
                    - "variables"
                    - "module"
                    - "imports"
                    - "exports"
                    - "hiddens"
                    - "left"
                    - "right"
                    - "assoc"
                    - "non-assoc"
                    - "bracket"
                    - "sorts"
                    - "restrictions"
                    # [A-Za-z0-9_\-]
                    ;

syntax ModuleWord = lex Word: [A-Za-z0-9_\-]+
                    # [A-Za-z0-9_\-]
                    ;

syntax ModuleId = lex Leaf: ModuleWord |
                  lex Root: "/" ModuleId |
                  lex Path: ModuleWord "/" ModuleId
                  # [/]
                  ;

syntax Import = Module: ModuleName |
                RenamedModule: ModuleName Renamings |
                Bracket: "(" Import ")"
                ;

syntax Imports = Import*
                 ;

syntax Section = Exports: "exports" Grammar |
                 Hiddens: "hiddens" Grammar
                 ;

syntax Sections = Section*
                  ;

syntax ImpSection = Imports: "imports" Imports
                    ;

syntax Definition = Module*
                    ;

syntax Lookahead 
 = Class: Class class
 | Seq: Class class "." Lookaheads las { println("action!"); if (las is Alt) fail; } 
 ;
 
// [a-z] . [0-9] | [\"]
syntax Lookaheads 
  = Single: Lookahead 
  | right Alt: Lookaheads "|" Lookaheads 
  | Bracket: "(" Lookaheads ")" 
  | List: "[[" {Lookahead ","}* "]]"
  ;

syntax Restriction = Follow: Syms "-/-" Lookaheads
                     ;

syntax Assoc = Left: "left" |
                       Right: "right" |
                       NonAssoc: "non-assoc" |
                       Assoc: "assoc"
                       ;
                       
syntax Restrictions = Default: Restriction* ;



syntax ArgumentIndicator = Default: "\<" {NatCon ","}+ "\>"
                           ;

syntax Group = non-assoc WithArguments: Group ArgumentIndicator |
               non-assoc NonTransitive: Group "." |
               ProdsGroup: "{" Prods "}" |
               AssocGroup: "{" Assoc ":" Prods "}" |
               SimpleGroup: Prod
               ;

syntax Priority = Chain: {Group "\>"}+ |
                  Assoc: Group Assoc Group
                  ;

syntax Priorities = {Priority ","}*
                    ;

syntax AFun = Quoted: StrCon |
              Unquoted: IdCon
              - "left" 
              - "right" 
              - "assoc" 
              - "non-assoc"
              ;

syntax ATerm = Int: IntCon |
               Real: RealCon |
               Fun: AFun |
               Appl: AFun "(" {ATerm ","}+ ")" |
               Placeholder: "\<" ATerm "\>" |
               List: "[" {ATerm ","}* "]" |
               Annotated: ATerm Annotation
               ;

syntax Annotation = Default: "{" {ATerm ","}+ "}"
                    ;

syntax IntCon = Natural: NatCon |
                Positive: "+" NatCon |
                Negative: "-" NatCon
                ;
