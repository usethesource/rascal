module experiments::Compiler::Examples::Tst

//syntax AFun = Quoted: StrCon |
//              Unquoted: IdCon
//              ;

//syntax ATerm = Int: IntCon |
//               Real: RealCon 
//               //Fun: AFun |
//              // Appl: AFun "(" {ATerm ","}+ ")" |
//              // Placeholder: "\<" ATerm "\>" |
//              // List: "[" {ATerm ","}* "]" |
//              // Annotated: ATerm Annotation
//               ;

//syntax Annotation = Default: "{" {ATerm ","}+ "}"
 //                   ;

syntax IntCon = Natural: NatCon |
                Positive: "+" NatCon |
                Negative: "-" NatCon
                ;
                
syntax RealCon = RealCon: IntCon "." NatCon OptExp
                 ;   
                 
syntax OptExp = Present: "e" IntCon |
                Absent: 
                ;
                              
lexical NatCon = Digits: [0-9]+ !>> [0-9];                

//lexical StrChar = NewLine: [\\] [n] 
//                | Tab: [\\] [t] 
//                | Quote: [\\] [\"] 
//                |  Backslash: [\\] [\\] 
//                |  Decimal: [\\] [0-9] [0-9] [0-9] 
//                |  Normal: ![\n\t\"\\] 
//                ;
//
//lexical StrCon = Default: [\"] StrChar* [\"]
//                ;
               
//lexical IdCon = Default: [A-Za-z] [A-Za-z\-0-9]* !>> [A-Za-z\-0-9];