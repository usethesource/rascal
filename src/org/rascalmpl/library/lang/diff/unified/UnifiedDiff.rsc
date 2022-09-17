@doc{
#### Synopsis

Syntax definition for Unified Diff format (see http://en.wikipedia.org/wiki/Diff_utility#Unified_format)
}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
module lang::diff::unified::UnifiedDiff

syntax Diff
  = Header old Header new Chunk* chunks
  ;

syntax Chunk
  = ChunkStart Line+
  ;

syntax ChunkStart
  = ^ "@@ -" Range " +" Range " @@" Content
  ;
  
syntax Header
  = ^ Indicator " " FileName DateTime?  "\n"
  ;
  
syntax DateTime
  = "\t" Date date " " Time time " " TimeZone timeZone
  ;
  
lexical FileName
  = ![\t\n]* 
  ;
  
syntax Indicator
  = old: "---" 
  | new: "+++"
  ;
  
syntax Date
  = Year year "-" Month month "-" Day day
  ;
  
syntax Time
  = Hours hours ":" Minutes minutes ":" Seconds seconds [0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]
  ;
  
lexical Year = [0-9][0-9][0-9][0-9];
lexical Month = [0-1][0-9];
lexical Day = [0-1][0-9];  
lexical Hours = [0-2][0-9]; 
lexical Minutes = [0-6][0-9];
lexical Seconds = [0-6][0-9];

  
syntax TimeZone
  = Sign sign Hours hours Minutes minutes 
  ;
  
lexical Sign = [+\-];

syntax Range
  = Decimal begin // chunksize = 1
  | Decimal begin "," Decimal size
  ;
  
lexical Decimal
  = [0-9]+ !>> [0-9]
  ;
  
syntax Line
  = common: ^ " " Content content 
  | onlyOld: ^ "-" Content content
  | onlyNew: ^ "+" Content content
  | noNewLine: ^"\\ No newline at end of file\n"
  ;
  
lexical Content
  = ![\n]* [\n]
  ;
