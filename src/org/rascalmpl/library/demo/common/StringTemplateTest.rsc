@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::common::StringTemplateTest

import demo::common::StringTemplate;

public  map[str, str] fields = (
     "name" : "String",
     "age" : "Integer",
     "address" : "String"
  );
  
public str cperson = 
  // Do not change a single space in the string below! (for testing purposes)
  "public class Person {
    '  
    '  private String address;
    '  public void setAddress(String address) {
    '    this.address = address;
    '  }
    '  public String getAddress() {
    '    return address;
    '  }
    '  private Integer age;
    '  public void setAge(Integer age) {
    '    this.age = age;
    '  }
    '  public Integer getAge() {
    '    return age;
    '  }
    '  private String name;
    '  public void setName(String name) {
    '    this.name = name;
    '  }
    '  public String getName() {
    '    return name;
    '  }
    '}";

public test bool t1() =
    genClass("Person", fields) == cperson;
