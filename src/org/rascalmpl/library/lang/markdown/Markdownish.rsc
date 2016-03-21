module lang::markdown::Markdownish[&Cmd, &Expr]

start syntax WorkBook
    = workBookItems: WorkBookItem*
    ;

syntax WorkBookItem
  = docSection: DocSection
  | codeSection: CodeSection
  ;
  
syntax CodeSection
  = indented: [\ ] << () !>> [\-*•◦] &Cmd
  ;

lexical ResultSegment
  = "{" ResultText* "}"
  | ![$]
  ;
  
lexical ResultText
  = ResultSegment+
  ;
  
syntax ExpressionResult
  = @category="Result" "⇨ " ResultText >> [}]
  ; 

syntax DocInterpolation
  = &Expr ExpressionResult?  
  ;
  
// TODO: only allow horizontal layout in Sections
//@nolayout  
lexical Segment
  = @category="Interpolation" expression: "${" DocInterpolation "}"
  | escape: [\\][\\$]
  | dollar: [$] !>> [{]
  | content: ![$\\\n\r_]
  
  // These two must not start at the beginning of line/file (!^)
  | @category="Topic" [\n] !<< "[" QualifiedName "]"
  | @category="Link" [\n] !<< "[" Segment "]" "(" Segment ")"
  //| @category="Strong" "*" Segment* "*"
  //| @category="Emphasis" "_" Segment* "_"
  ;
   
// TODO should be syntax and only horizontal layout.   
lexical DocSection
  = @category="Documentation" line: ^ () !>> [\ \t#\-*•◦] Segment* $
  | @category="Heading1" heading1: ^ "#" !>> [#] Segment* $
  | @category="Heading2" heading2: ^ "##" !>> [#] Segment* $
  | @category="Heading3" heading3: ^ "###" !>> [#] Segment* $
  | @category="Heading4" heading4: ^ "####" !>> [#] Segment* $
  | itemized: ^ Bullet Segment* $
  | itemized: [\ \t] << Bullet Segment* $
  ;   
  
lexical Bullet
  = @category="Bullet" [\-*•◦]
  ;    
