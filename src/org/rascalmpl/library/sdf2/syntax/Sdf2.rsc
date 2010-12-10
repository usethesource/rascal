@bootstrapParser // Stop generating because we have a "`" in here!!!!
module Sdf2

syntax Sort = lex OneChar: [A-Z] |
              lex MoreChars: [A-Z] [A-Za-z0-9]* [A-Za-z0-9]
              - "LAYOUT"
              # [A-Za-z0-9]
              ;

syntax Symbols = Symbol*
                 - StrCon "(" {Symbol ","}* ")"
                 ;

syntax NatCon = lex Digits: [0-9]+
                # [0-9]
                ;

syntax NumChar = lex Digits: [0-9]+
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
                   lex Escaped: [\\] [\0-\31A-Za-mo-qsu-z0-9]
                   ;

syntax Renaming = Symbol: Symbol "=\>" Symbol |
                  production: Production "=\>" Production
                  ;

syntax Renamings = Renamings: "[" Renaming* "]"
                   ;

syntax IdCon = lex Default: [A-Za-z] [A-Za-z\-0-9]*
               # [A-Za-z\-0-9]
               ;

syntax CharClass = SimpleCharClass: "[" OptCharRanges "]" |
                   Bracket: "(" CharClass ")" |
                   Comp: "~" CharClass >
                   Diff: CharClass "/" CharClass >
                   ISect: CharClass "/\\" CharClass >
                   Union: CharClass "\\/" CharClass
                   ;

syntax CharRange = Character |
                   Range: Character "-" Character
                   ;

syntax CharRanges = CharRange |
                    Conc: CharRanges CharRanges |
                    Bracket: "(" CharRanges ")"
                    ;

syntax OptCharRanges = Absent: |
                       Present: CharRanges
                       ;

syntax Attribute = Id: "id" "(" ModuleName ")" |
                   Reject: "reject" |
                   Prefer: "prefer" |
                   Avoid: "avoid" |
                   Bracket: "bracket" |
                   Assoc: Associativity
                   ;

syntax ATermAttribute = Default: ATerm |
                        Term: Attribute
                        - Associativity |
                          "reject" |
                          "prefer" |
                          "avoid" |
                          "bracket" |
                          "id" "(" ModuleName ")"
                         ;

syntax Attributes = Attrs: "{" {Attribute ","}* "}" |
                    NoAttrs: 
                    ;

syntax Production = Prod: "-\>" Symbol Attributes |
                    PrefixFun: "(" {Symbol ","}* ")" Attributes // Avoid
                    ;

syntax Productions = Production*
                     ;

syntax Grammar = Bracket: "(" Grammar ")" |
                 Syntax: "syntax" Productions |
                 Aliases: "aliases" Aliases |
                 Sorts: "sorts" Symbols |
                 ImpSection: ImpSection |
                 Restrictions: "restrictions" Restrictions |
                 LexicalSyntax: "lexical" "syntax" Productions |
                 ContextFreeSyntax: "context-free" "syntax" Productions |
                 Variables: "variables" Productions |
                 LexicalVariables: "lexical" "variables" Productions |
                 LexicalPriorities: "lexical" "priorities" Priorities |
                 ContextFreePriorities: "context-free" "priorities" Priorities |
                 LexicalRestrictions: "lexical" "restrictions" Restrictions |
                 ContextFreeRestrictions: "context-free" "restrictions" Restrictions |
                 KernalStartSymbols: "start-symbols" Symbols |
                 LexicalStartSymbols: "lexical" "start-symbols" Symbols |
                 ContextFreeStartSymbols: "context-free" "start-symbols" Symbols |
                 Priorities: "priorities" Priorities |
                 EmptyGrammar: "(/)" |
                 assoc ConcGrammars: Grammar Grammar
                 ;

syntax Label = Quoted: StrCon |
               IdCon: IdCon
               - Associativity
               ;

syntax Symbol = Bracket: "(" Symbol ")" |
                Lifting: "`" Symbol "`" |
                Sort: Sort |
                Lit: StrCon |
                CILit: SingleQuotedStrCon |
                CharClass: CharClass |
                CF: "\<" Symbol "-CF" "\>" |
                Lex: "\<" Symbol "-LEX" "\>" |
                Varsym: "\<" Symbol "-VAR" "\>" |
                Layout: "LAYOUT" |
                Start: "\<START\>" |
                FileStart: "\<Start\>" |
                ParameterizedSort: Sort "[[" {Symbol ","}+ "]]" |
                Empty: "(" ")" |
                Seq: "(" Symbol Symbol+ ")" |
                Tuple: "\<" Symbol "," {Symbol ","}+ "\>" |
                Func: "(" Symbols "=\>" Symbol ")" |
                Strategy: "(" Symbol "-\>" Symbol ")" |
                Opt: Symbol "?" |
                Iter: Symbol "+" |
                IterStar: Symbol "*" |
                IterSep: "{" Symbol Symbol "}" "+" |
                IterStarSep: "{" Symbol Symbol "}" "*" >
                right Alt: Symbol "|" Symbol >
                Label ":" Symbol
                ;

layout LAYOUTLIST = LAYOUT*
                    # [\ \t\n\r%]
                    ;

syntax LAYOUT = lex Whitespace: [\ \t\n\r] |
                lex Line: "%%" ![\n]* [\n] |
                lex Nested: "%" ![%\n] "%"
                ;

syntax Alias = Alias: Symbol "-\>" Symbol
               ;

syntax Aliases = Alias*
                 ;

syntax StrChar = lex NewLine: "\\n" |
                 lex Tab: "\\t" |
                 lex Quote: "\\\"" |
                 lex Backslash: "\\\\" |
                 lex Decimal: "\\" [0-9] [0-9] [0-9] |
                 lex Normal: ![\0-\31\n\t\"\\]
                 ;

syntax StrCon = lex Default: [\"] StrChar* [\"]
                ;

syntax FunctionName = UnquotedFun: IdCon |
                      QuotedFun: StrCon
                      ;

syntax SingleQuotedStrCon = lex Default: [\'] SingleQuotedStrChar [\']
                            ;

syntax SingleQuotedStrChar = lex NewLine: "\\n" |
                              lex Tab: "\\t" |
                              lex Quote: "\\\'" |
                              lex Backslash: "\\\\" |
                              lex Decimal: "\\" [0-9] [0-9] [0-9] |
                              lex Normal: ![\0-\31\n\t\'\\]
                              ;

syntax RealCon = RealCon: IntCon "." NatCon OptExp
                 ;

syntax OptExp = Present: "e" IntCon |
                Absent: 
                ;

start syntax Module = Module: "module" ModuleName ImpSection* Sections
                      ;

syntax ModuleName = Unparameterized: ModuleId |
                    Parameterized: ModuleId "[" Symbols "]"
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

syntax Lookahead = CharClass: CharClass |
                   Seq: CharClass "." Lookaheads
                   ;

syntax Lookaheads = Single: Lookahead |
                    right Alt: Lookaheads "|" Lookaheads |
                    Bracket: "(" Lookaheads ")" |
                    List: "[[" {Lookahead ","}* "]]"
                    ;

syntax Restriction = Follow: Symbols "-/-" Lookaheads
                     ;

syntax Restrictions = Restriction
                      ;

syntax Associativity = Left: "left" |
                       Right: "right" |
                       NonAssoc: "non-assoc" |
                       Assoc: "assoc"
                       ;

syntax ArgumentIndicator = Default: "\<" {NatCon ","}+ "\>"
                           ;

syntax Group = non-assoc WithArguments: Group ArgumentIndicator |
               non-assoc NonTransitive: Group "." |
               ProdsGroup: "{" Productions "}" |
               AssocGroup: "{" Associativity ":" Productions "}" |
               SimpleGroup: Production
               ;

syntax Priority = Chain: {Group "\>"}+ |
                  Assoc: Group Associativity Group
                  ;

syntax Priorities = {Priority ","}*
                    ;

syntax AFun = Quoted: StrCon |
              Unquoted: IdCon
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
