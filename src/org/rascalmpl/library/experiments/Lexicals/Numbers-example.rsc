@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module \Numbers-example

rule n1 number("0" <Digit+ Ds>) => number(<Ds>);


rule n2 number("0" <[0-9]+ Ds>) => number(<Ds>);


Real truncate(Real R){
    switch (R) {
       case real(<Number Num> "." <Digit+ Ds>) => real(<Num> "." "0");
    }
}
