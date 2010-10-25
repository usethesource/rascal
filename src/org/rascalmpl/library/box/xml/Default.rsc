module box::xml::Default

import ParseTree;
import box::Concrete;
import box::Box;
import IO;

import languages::xml::syntax::\XML;

alias UserDefinedFilter = Box(Tree t) ;

str lt = "\<", gt = "\>";


public Box getTags(Tree q) {
   if (STag a:=q) 
   switch(a) {
	   case (STag)`<<Name name><Attribute* attributes>>`:  
                 return  H(0, [KW(L("<lt>")), KW(evPt(name)), L(" "), HV(1, getArgs(attributes)), KW(L("<gt>"))]);                                             
        }
    if (ETag a:=q) 
        return  KW(L("<a>"));
        /*
        switch(a) {
           case  (ETag)`</<Name name>>` :  {
                 println(name);
                 return  HV(0, [L("<lt>/"), KW(evPt(name))  ,  L("<gt>")]);
                 }   
            }
            }
        */
    if (EmptyElementTag a:=q)  
        switch(a) {
           case  (EmptyElementTag)`<<Name name><Attribute* attributes>/>` :  
                 return H(0, [KW(L("<lt>")), KW(evPt(name)), L(" "), HV(0, getArgs(attributes)), KW(L("/<gt>"))]);   
            } 
    if (AttValue v:=q) {
        return  STRING(L("<v>"));
        }
    return NULL();
    }

list[UserDefinedFilter] userDefinedFilters = [ 
       getTags
       ];

list[int] isBlok(list[Symbol] q, list[Tree] z) {
     if (isScheme(q , ["NonEmptyElementTag"])) return isBody(z, 0); // pattern with action
     return [];
     } 
        
bool isKeyword(Symbol a) {
     if (\lit(str s):=a) {
         if (s=="begin" || s == "end" || s == "declare" || s == "while" || s == "if"
            || s == "then" || s == "do" || s == "od" || s == "fi") return true;
         }
     return false;
     }
     
void setUserRules() {
    setUserDefined(extraRules);
    setKeyword(isKeyword);
    setStartEndBlock("STag", "ETag");
    setBlock(isBlok);
    }  
     
public text toText(loc asf){
     Document a = parse(#Document, asf);
     setUserRules();
     text r = toText(a);
     writeData(asf, r, ".txt");
     return r;
     }
   
public text toLatex(loc asf){
     Document a = parse(#Document, asf);
     // rawPrintln(a);
     setUserRules();
     text r = toLatex(a);
     writeData(asf, r, ".tex");
     return r;
     }

public text toHtml(loc asf){
     Document a = parse(#Document, asf);
     setUserRules();
     text r = toHtml(a);
     writeData(asf, r, ".html");
     return r;
     }
// Don't change this part 

public Box extraRules(Tree q) {  
   for (UserDefinedFilter userDefinedFilter<-userDefinedFilters) {
           Box b = userDefinedFilter(q);
           if (b!=NULL()) return b;
           }
    return NULL();
    }
    
