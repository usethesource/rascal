module lang::rascalcore::compile::Examples::Tst2

/*
 * Note: this grammar allows nesting of *,/,% and ^
 */

lexical Comment
  = ";" CommentBody >> "\r\n"
  | ";" CommentBody !>> ![] //To handle files ending with a comment
  ;

lexical CommentBody = @Foldable (![&\r\n]+ !>> ![&\r\n] | "&" !>> "\r\n" | "&\r\n")*; // "&\r\n" is used for multi-line comments

lexical Layout
  = @category="comment" Comment
  | "&\r\n"
  | [\t\ ]+ !>> [\t\ ]
  ;

lexical LayoutList = Layout* !>> [\t\ ;] !>> "&\r\n"; // "&\r\n" is used for multi-line comments and statements

layout LayoutWithContinuation
  = LayoutList !>> "&"
  | LayoutList "&" !>> ![] //To handle files ending with an &
  ;

lexical Integer = integer: [0-9]+ !>> [0-9];

lexical Decimal = Integer "." Integer | Integer "." !>> [0-9];

lexical StringConstant = "\'" ![\']* chars "\'";
  
lexical String = StringConstant+;

lexical Id
  = [a-z A-Z 0-9 _ .] !<< [a-z A-Z.$][a-z A-Z 0-9 _ .]* !>> [a-z A-Z 0-9 _ .]
  | [a-z A-Z 0-9 _ .] !<< [0-9]+ [a-z A-Z _]+ !>> [a-z A-Z]
  | [a-z A-Z 0-9 _ .] !<< [0-9]+ [a-z A-Z _]+ !>> [a-z A-Z_] [a-z A-Z 0-9 _ .]+ !>> [a-z A-Z 0-9 _ .]
  ;

lexical Keywords1
  /*Statement*/
  = //'ASC' | 'CASE' | 'DES' | 'ELSE' | 'ENDIF' | 'ENDSCAN' | 'ENDSELECT' | 'GSET' | 'IF' | 'IS' | 'NOT' | 'REFERENCE' | 'SCAN' | 'SELECT' | 'SET' | 'SORT' | 'THEN' | 'TO' | 'WEND' | 'WHILE'
  ;
lexical Keywords2
  /*Expression*/ 
  = 'AND' | 'IN' | 'K1SPECIALA' | /*NOT |*/ 'OR'
  ;
lexical Keywords3
  //TODO: pending an implementation in Rascal, replace the following lines by productions for the lexical nonterminals
  /*BasicStatement*/
  = //'ABORT' | 'ALLUNITRECALC' | 'CLEARALLGTEMPS' | 'CLEARGNUMS' | 'CLEARGSTRS' | 'CLEARLGSTRS' | 'ENDSTOREUD' | 'ENDUDREAD' | 'ENDUDREADEX' | 'FORGETALLUD' | 'K1SPECIALA' | 'RECALCALL' | 'REMOVEOTHERUD' | 'REMOVEOTHERUDEX' | 'RESETINPUT' | 'STARTUDREAD' | 'STARTUDREADEX' | 'TABLEDISCARD'
  ;
lexical Keywords4
  /*BasicStatementWithArgs*/ 
  = //'ABORTIO' | 'ADATE' | 'ALLOCK4CHILD' | 'ALLOCK4PARENT' | 'ALLOCK5CHILD' | 'ALLOCK5PARENT' | 'ALLOCPTRINFO' | 'ALPHA' | 'ASSOCIATE' | 'ASSOCIATE2' | 'ATTACH' | 'ATTACHSTRING' | 'BLOCK' | 'BREAK' | 'CATCR' | 'CATFIELDDEF' | 'CATLF' | 'CHANGEFORMAT' | 'COMMDATA' | 'COPY' | 'DBDATEFIELD' | 'DBDELDEF' | 'DBFIELD' | 'DBRUNEDITS' | 'DBXEQDIAGS' | 'DELETE' | 'DELETEUNIT' | 'DESC' | 'DESCTOENUM' | 'DETACHSTRING' | 'DIAG' | 'DTDATE' | 'DTDELDEF' | 'DTENDDEF' | 'DTENUM' | 'DTNUMBER' | 'DTSTRING' | 'EFABORT' | 'EFPRINT' | 'ENUM' | 'ERROR' | 'EVAL' | 'EXPORTK1DATA' | 'FLAGCLEAR' | 'FLAGSET' | 'FORCE' | 'FORGETALLUDEX' | 'FORGETUD' | 'IGNORETABLE' | 'JUSTIFY' | 'K1ACTIVE' | 'K1ALLOCI' | 'K1ALLOCP' | 'K1SUP' | 'K1TIME' | 'LOOK' | 'MODFORTE' | 'NEXT' | 'PARACOPY' | 'PFCOMMIT' | 'PFCOPY' | 'PFCOPYFORM' | 'PFDELETE' | 'PFMOVE' | 'PFMOVEFORM' | 'PFMOVELNO' | 'PICKNAME' | 'PRIORITY' | 'REMOVEOTHERUD' | 'RESETFORMAT' | 'RETURN' | 'SCANSEL' | 'SETCHECKED' | 'SETFILLED' | 'SETFORMMOD' | 'SETFORMTOLOAD' | 'SETFORMTOLOADEX' | 'SETGROUPNAME' | 'SETKEYED' | 'SETMEMBER' | 'SETMODIFIED' | 'SPLIT' | 'STOREUD' | 'STRCAT' | 'STRUPPER' | 'SUBSTR' | 'TABLEMOVE' | 'TRANS' | 'UNITORDER' | 'UPPERCASE' | 'WARN'
  ;
lexical Keywords5
  /*BasicStatementNoParens*/
  = 'CLEAR' | 'GCLEAR' | 'UNUSE' | 'USE'
  ;
lexical Keywords6
  /*BuiltIn*/
  = //'ABS' | 'ALL' | 'CLOSE' | 'CREATE' | 'CREATEDBY' | 'CREATEDBYOCC' | 'DATE' | 'DAY' | 'DBENDDEF' | 'DBEXPORT' | 'DBIMPORT' | 'DBSTARTDEF' | 'DIALOG' | 'DRILL' | 'DTSTARTDEF' | 'EFEND' | 'EFGETDCN' | 'EFSTART' | 'EFWRITEFN' | 'EFWRITEFP' | 'EFWRITEFS' | 'ENDIO' | 'GCOMMDATA' | 'GETERROR' | 'GETFILE' | 'GETLINE' | 'GPARENTFORMOF' | 'GPARENTOCCUROF' | 'INT' | 'ISPRINTOFF' | 'MAX' | 'MDY' | 'MESSAGEBOX' | 'MIN' | 'MONITORUD' | 'MONTH' | 'NUMERIC' | 'OCCUROF' | 'OPEN' | 'PARENTFORMOF' | 'PARENTOCCUROF' | 'PERMCREATE' | 'POPBOOKMARK' | 'READFIXED' | 'READLINE' | 'READUD' | 'READUDEX' | 'READVAR' | 'RELAX' | 'ROUND' | 'SETBOOKMARK' | 'SORTOCCUROF' | 'STARTIO' | 'STRLEN' | 'STRPOS' | 'TABLEREAD' | 'UNIT' | 'UNITCANEXIST' | 'UNITEXISTS' | 'WRITE' | 'WRITEEOL' | 'YEAR'
  ;
lexical Keywords7
  /*BoolFunc*/
  = 'ATEND' | 'BLANK' | 'CALCKEYED' | 'EXISTS' | 'KEYEDIN' | 'MODIFIED'
  ;
  
keyword Keywords
  = Keywords1 | Keywords2 | Keywords3 | Keywords4 | Keywords5 | Keywords6 | Keywords7
  ;
  
lexical Identifier = @category="variable" Id \ Keywords;

// Start of FX grammar

start syntax Formula = {FormulaBlock Eol+}* blocks MainFormula mainFormula;

syntax FormulaBlock = ('PreEdit' | 'Diag') blockType "{" Eol ignore StatementBlock block "}";

syntax MainFormula = StatementBlock block LastStatement? lastStm;

//Final optional symbol handles files without a trailing new line
syntax LastStatement = Statement stm () $;
start syntax StatementBlock
  = (Statement? stm Terminator)* statements
  ;

lexical Eol = "&" !<< "\r\n"; //"\r\n" is the Windows line separator. If preceded by an "&", it is considered layout

lexical Terminator
  = Eol
  | "|"
  ;

lexical BasicStatement = 'ABORT' | 'ALLUNITRECALC' | 'CLEARALLGTEMPS' | 'CLEARGNUMS' | 'CLEARGSTRS' | 'CLEARLGSTRS' | 'ENDSTOREUD' | 'ENDUDREAD' | 'ENDUDREADEX' | 'FORGETALLUD' | 'K1SPECIALA' | 'RECALCALL' | 'REMOVEOTHERUD' | 'REMOVEOTHERUDEX' | 'RESETINPUT' | 'STARTUDREAD' | 'STARTUDREADEX' | 'TABLEDISCARD';

lexical BasicStatementWithArgs = ;//'ABORTIO' | 'ADATE' | 'ALLOCK4CHILD' | 'ALLOCK4PARENT' | 'ALLOCK5CHILD' | 'ALLOCK5PARENT' | 'ALLOCPTRINFO' | 'ALPHA' | 'ASSOCIATE' | 'ASSOCIATE2' | 'ATTACH' | 'ATTACHSTRING' | 'BLOCK' | 'BREAK' | 'CATCR' | 'CATFIELDDEF' | 'CATLF' | 'CHANGEFORMAT' | 'COMMDATA' | 'COPY' | 'DBDATEFIELD' | 'DBDELDEF' | 'DBFIELD' | 'DBRUNEDITS' | 'DBXEQDIAGS' | 'DELETE' | 'DELETEUNIT' | 'DESC' | 'DESCTOENUM' | 'DETACHSTRING' | 'DIAG' | 'DTDATE' | 'DTDELDEF' | 'DTENDDEF' | 'DTENUM' | 'DTNUMBER' | 'DTSTRING' | 'EFABORT' | 'EFPRINT' | 'ENUM' | 'ERROR' | 'EVAL' | 'EXPORTK1DATA' | 'FLAGCLEAR' | 'FLAGSET' | 'FORCE' | 'FORGETALLUDEX' | 'FORGETUD' | 'IGNORETABLE' | 'JUSTIFY' | 'K1ACTIVE' | 'K1ALLOCI' | 'K1ALLOCP' | 'K1SUP' | 'K1TIME' | 'LOOK' | 'MODFORTE' | 'NEXT' | 'PARACOPY' | 'PFCOMMIT' | 'PFCOPY' | 'PFCOPYFORM' | 'PFDELETE' | 'PFMOVE' | 'PFMOVEFORM' | 'PFMOVELNO' | 'PICKNAME' | 'PRIORITY' | 'REMOVEOTHERUD' | 'RESETFORMAT' | 'RETURN' | 'SCANSEL' | 'SETCHECKED' | 'SETFILLED' | 'SETFORMMOD' | 'SETFORMTOLOAD' | 'SETFORMTOLOADEX' | 'SETGROUPNAME' | 'SETKEYED' | 'SETMEMBER' | 'SETMODIFIED' | 'SPLIT' | 'STOREUD' | 'STRCAT' | 'STRUPPER' | 'SUBSTR' | 'TABLEMOVE' | 'TRANS' | 'UNITORDER' | 'UPPERCASE' | 'WARN';

lexical BasicStatementNoParens = 'CLEAR' | 'GCLEAR' | 'UNUSE' | 'USE';

start syntax Statement
  = Assignable ("[" Expression "]")? "=" Expression
  | @category="keyword" BasicStatement
  | @category="keyword" BasicStatementWithArgs "(" Arguments ")"
  | @category="keyword" BasicStatementNoParens Identifier id
  | @category="keyword" endScan: 'ENDSCAN' "(" Identifier id ")"
  | @category="keyword" gset: 'GSET' Identifier id ":" Expression cond
  | @category="keyword" @Foldable _if: 'IF' Expression cond 'THEN' Eol ignore StatementBlock body 'ENDIF'
  | @category="keyword" @Foldable ifElse: 'IF' Expression cond 'THEN' Eol ignore StatementBlock body 'ELSE' Eol ignore2 StatementBlock elseBody 'ENDIF'
  | @category="keyword" reference: 'REFERENCE' 'TO' Identifier id1 'IS' Identifier id2
  | @category="keyword" @Foldable _select: 'SELECT' Eol+ ignore3 Case* cases 'ENDSELECT'
  | @category="keyword" _set: 'SET' Identifier id ":" 'NOT'? negated {Expression!booleanNot ","}+ conds
  | @category="keyword" scan: 'SCAN' "(" Identifier id ")"
  | @category="keyword" _sort:'SORT' "(" Identifier id "," {(('ASC'|'DES') direction "," Identifier otherId) ","}* moreIds ")"
  | @category="keyword" @Foldable _while: 'WHILE' Expression cond Eol ignore StatementBlock body 'WEND'
  ;

syntax Case = @Foldable \case: 'CASE' Expression condition Eol eol StatementBlock block;

lexical Relop
  = @category="keyword.operator" "\<"
  | @category="keyword.operator" "\>"
  | @category="keyword.operator" "\<="
  | @category="keyword.operator" "\>="
  | @category="keyword.operator" "="
  | @category="keyword.operator" "\<\>"
  ;

lexical Assignable
  = @category="variable" simpleId: Identifier
  | @category="variable" fieldRef: Identifier "." Identifier
  | @category="variable" localFieldRef: "." Identifier
  | @category="variable" "$" Identifier
  | @category="variable" "$" Identifier "." Identifier
  ;

lexical EnumLiteral = ![}]* !>> ![}];

lexical BuiltIn = ;//'ABS' | 'ALL' | 'CLOSE' | 'CREATE' | 'CREATEDBY' | 'CREATEDBYOCC' | 'DATE' | 'DAY' | 'DBENDDEF' | 'DBEXPORT' | 'DBIMPORT' | 'DBSTARTDEF' | 'DIALOG' | 'DRILL' | 'DTSTARTDEF' | 'EFEND' | 'EFGETDCN' | 'EFSTART' | 'EFWRITEFN' | 'EFWRITEFP' | 'EFWRITEFS' | 'ENDIO' | 'GCOMMDATA' | 'GETERROR' | 'GETFILE' | 'GETLINE' | 'GPARENTFORMOF' | 'GPARENTOCCUROF' | 'INT' | 'ISPRINTOFF' | 'MAX' | 'MDY' | 'MESSAGEBOX' | 'MIN' | 'MONITORUD' | 'MONTH' | 'NUMERIC' | 'OCCUROF' | 'OPEN' | 'PARENTFORMOF' | 'PARENTOCCUROF' | 'PERMCREATE' | 'POPBOOKMARK' | 'READFIXED' | 'READLINE' | 'READUD' | 'READUDEX' | 'READVAR' | 'RELAX' | 'ROUND' | 'SETBOOKMARK' | 'SORTOCCUROF' | 'STARTIO' | 'STRLEN' | 'STRPOS' | 'TABLEREAD' | 'UNIT' | 'UNITCANEXIST' | 'UNITEXISTS' | 'WRITE' | 'WRITEEOL' | 'YEAR';

lexical BoolFunc = 'ATEND' | 'BLANK' | 'CALCKEYED' | 'EXISTS' | 'KEYEDIN' | 'MODIFIED';

syntax Expression
  = bracket parentheses: "(" Expression expression")"
  > @category="keyword" function: (Identifier|BuiltIn) function "(" Arguments arguments ")"
  | @category="keyword" 'K1SPECIALA'
  | @category="constant.numeric" integer: Integer integer
  | @category="constant.numeric" decimal: Decimal decimal
  | @category="string" string: String string
  | @category="variable.other.enummember" enum: "{" EnumLiteral enum "}"
  | @category="variable" dereference: Identifier id ("[" Expression expression "]")?
  > @category="keyword.operator" minus: "-" Expression expression
  > non-assoc @category="keyword.operator" power: Expression lhs "^" Expression rhs
  > left ( @category="keyword.operator" multiply: Expression lhs "*" Expression rhs
         | @category="keyword.operator" divide: Expression lhs "/" Expression rhs
         | @category="keyword.operator" modulo: Expression lhs "%" Expression rhs)
  > left ( @category="keyword.operator" plus: Expression lhs "+" Expression rhs
         | @category="keyword.operator" minus: Expression lhs "-" Expression rhs)
  > non-assoc ( binaryCompare: Expression lhs Relop Expression rhs
              | booleanIn: Expression 'IN' "(" Arguments arguments ")")
  | @category="keyword" booleanFunction: BoolFunc function "(" Arguments args ")"
  > @category="keyword.operator" booleanNot: 'NOT' Expression expression
  > left @category="keyword.operator" binaryAnd: Expression lhs 'AND' Expression rhs
  > left @category="keyword.operator" binaryOr: Expression lhs 'OR' Expression rhs
  ;

syntax Arguments = ","? {Expression!binaryCompare!binaryOr!binaryAnd!booleanFuction!booleanIn!booleanNot ","}*;
