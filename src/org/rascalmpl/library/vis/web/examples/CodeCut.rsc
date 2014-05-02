module vis::web::examples::CodeCut

import lang::java::jdt::m3::Core;
import analysis::m3::Core;

import lang::java::m3::Registry;

import lang::java::m3::AST;
//import Set;
// import List;
import Prelude;

import vis::web::BarChart;
import util::HtmlDisplay;
import vis::web::markup::Dimple;
import IO;


list[tuple[str, int]] classes = [];

public rel[str, str] methodSrc={},  methodReturn={};

rel [str, int] methodSize={};

str toString(Type typ) {
    // println(typ);
    switch (typ) {
               case \int() : return "int";
               case  short() : return "short";
               case  long()  : return "long";
               case float() : return "int";
               case  double(): return "double";
               case char() :  return "char";
               case string() : return "string";
               case  byte() : return "byte";
               case \void() : return "void";
               case \boolean() : return "boolean";
               case simpleType(Expression name): {
                   if (simpleName(str stringValue):=name) return stringValue;
                   }
        }
        return "??";
    }

str signature(str name, list[Declaration] parameters) {
   str pars = "<name>(";
   if (isEmpty(parameters)) return pars+")";
   if (parameter(Type typ, _, _):=head(parameters)) {
       pars += toString(typ);
       }
   for (Declaration d <- tail(parameters)) {
        if (parameter(Type typ, _, _):=d) {
              pars+=",<toString(typ)>";
              }
        }
   pars+=")";
   return pars;
   }



public void initialize(loc project) { 
        M3 model = createM3FromEclipseProject(project);
        set[Declaration] decls = createAstsFromDirectory(project, false, javaVersion = "1.7" );
        println(size(decls));
        // compilationUnit(package("dotplugin"),[import("org.eclipse.swt.events.ModifyEvent")][]
        top-down-break visit(decls) {
            case compilationUnit(_, _ , list[Declaration] d): {
                for (f<-d) {
 // \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body)
                    if (class(str name, _, _, list[Declaration] body):=f){
                        classes += < name, size(body)>;
                        // \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
                        // \block(list[Statement] statements)
                        for  (g<-body) {
                             if (\method(Type returnType, str procName, list[Declaration] parameters, _, Statement impl)  := g) {                       
                                  if (\block(list[Statement] statements):=impl)
                                       if (!isEmpty(statements)) {
                                          str typ = ((Type::\void():=returnType)?"void":"function");
                                          str method = signature(procName, parameters);
                                          methodSrc += <method, name>;
                                          methodSize+=<method, size(statements)>;
                                          methodReturn += <method, typ>;
                                          // println("<typ> <method>  <size(statements)>");
                                       }
                                  }
                             }
                        }
                    }
                    classes = sort(classes, bool(tuple[str, int] a, tuple[str, int] b) { return a[1]<b[1];});            
                }            
            }                          
        }

public void main() {
        initialize(|project://dotplugin|);
    str body = barChart(
    title="CodeCut" 
    ,x_axis = "class"
    ,y_axis =getYAxis(varName="methodSize", series=["method"],aggregateMethod="max", showPercent=true)  
    ,orderRule= [e[0]|e<-classes]
    );
    
    htmlDisplay(publish(
     |tmp:///codecut2|
     // |project://chart/src/m3|
     ,barChartHeader("barChart"), body,
    "method"
    ,<"class", methodSrc>
    ,<"proc", methodReturn>
    ,<"methodSize", methodSize>));
    }
        
 