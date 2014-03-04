/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Bert Lisser    - Bert.Lisser@cwi.nl
 *******************************************************************************/
module vis::web::examples::DisplayRelations
import vis::web::Chart;
import Prelude;
import util::Math;
import util::HtmlDisplay;
import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::Registry;
import lang::java::m3::AST;

int N = 16;

num r(num v) = round(v,0.04);


public rel[num, num] Sin = {<r(i), r(sin(i))>|num i <-[0,2*PI()/N..2*(PI()+(1.0/N))]};

public rel[num, num, str] towns  = {
  < 4.54, 52.23-51,"amsterdam">,
  < 4.19, 52.05-51, "denhaag">,
  < 4.29, 51.55-51, "rotterdam">,
  < 5.07, 52.06-51, "utrecht">,
  < 6.34, 53.13-51, "groningen">,
  < 5.48, 53.12-51, "leeuwarden">,
  < 5.41, 50.51-51, "maastricht">,
  < 5.55, 51.59-51, "arnhem">,
  < 3.35, 51.27-51, "vlissingen">
};

int nDiv(int d) {
   int r = 0;
   for (int i<-[2..d]) {
      if ((d/i)*i == d) r+=1;
      }
   return r;
   }
   
public map[int, int] divisors = (i:nDiv(i)|i<-[2..50]);



public void main() {
    // chart(Sin);
    // chart(divisors);
    // chart(towns);
    chart(class2method);
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


      
public rel[map[str,int], str, str] initialize(loc project) { 
        model = createM3FromEclipseProject(project);
        set[Declaration] decls = createAstsFromDirectory(project, false, javaVersion = "1.7" );
        // println(size(decls));  
        rel[map[str, int], str, str] r = {}; 
        top-down-break visit(decls) {
            case compilationUnit(_, _ , list[Declaration] d): {
                for (f<-d) {
                    if (class(str name, _, _, list[Declaration] body):=f){
                        for  (g<-body) {
                             if (\method(Type returnType, str procName, list[Declaration] parameters, _, Statement impl)  := g) {                       
                                  if (\block(list[Statement] statements):=impl)
                                       if (!isEmpty(statements)) {
                                          str typ = ((Type::\void():=returnType)?"void":"function");
                                          str method = signature(procName, parameters);
                                          r += <(name:size(body)), method, typ>;                                   
                                       }
                                  }
                             }
                        }
                    }           
                }            
            } 
        return r;                         
        }

public rel[map[str, int], str, str] class2method = initialize(|project://ambidexter|);        
