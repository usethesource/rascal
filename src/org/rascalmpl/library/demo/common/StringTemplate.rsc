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
//START
module demo::common::StringTemplate

import String;
import IO;
import Set;
import List;

// Capitalize the first character of a string

public str capitalize(str s) {  /*1*/
  return toUpperCase(substring(s, 0, 1)) + substring(s, 1);
}

// Helper function to generate a setter
private str genSetter(map[str,str] fields, str x) {
  return "public void set<capitalize(x)>(<fields[x]> <x>) {
         '  this.<x> = <x>;
         '}";
}

// Helper function to generate a getter
private str genGetter(map[str,str] fields, str x) {
  return "public <fields[x]> get<capitalize(x)>() {
         '  return <x>;
         '}";
}

// Generate a class with given name and fields.
// The field names are processed in sorted order.
public str genClass(str name, map[str,str] fields) { /*2*/
  return 
    "public class <name> {
    '  <for (x <- sort([f | f <- fields])) {>
    '  private <fields[x]> <x>;
    '  <genSetter(fields, x)>
    '  <genGetter(fields, x)><}>
    '}";
}

public  map[str, str] fields = (
     "name" : "String",
     "age" : "Integer",
     "address" : "String"
  );
  
public str cperson = 
  // Do not change a single space in the string below!
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

public test bool tstGenClass() =
    genClass("Person", fields) == cperson;
