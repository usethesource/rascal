module lang::rascal::tutor::ValueGenerator

import Boolean;
import String;
import util::Math;
import List;
import Set;
import String;
import DateTime;
import lang::rascal::tutor::Questions;
import Type;

lexical IntCon = [0-9]+ !>> [0-9];
syntax Type = "arb" "[" IntCon depth "," {Type ","}+ elemTypes "]";

private list[Type] baseTypes = [(Type)`bool`, (Type)`int`, (Type)`real`, (Type)`num`, (Type)`str`, (Type)`loc`, (Type)`datetime`];

// Type generation

Type generateType(Type tp){
    return
        visit(tp){
           case (Type) `arb[<IntCon depth>,<{Type ","}+ elemTypes>]` =>
            generateArbType(toInt("<depth>"), elemTypes)
        };
}

Type generateArbType(int n, {Type ","}+ prefs) = generateArbType(n, [p | p <- prefs]);

Type generateArbType(int n, list[Type] prefs){
   //println("generateArbType: <n>, <prefs>");
   if(n <= 0){
      return getOneFrom(prefs);
   }
     
   switch(arbInt(6)){
     case 0: { elemType = generateArbType(n-1, prefs); return (Type) `list[<Type elemType>]`; }
     case 1: { elemType = generateArbType(n-1, prefs); return (Type) `set[<Type elemType>]`; }
     case 2: { keyType = generateArbType(n-1, prefs);
               valType = generateArbType(n-1, prefs); 
               return (Type) `map[<Type keyType>,<Type valType>]`;
               }
     case 3: return generateArbTupleType(n-1, prefs);
     case 4: return generateArbRelType(n-1, prefs);
     case 5: return generateArbLRelType(n-1, prefs);
   }
   return getOneFrom(prefs);
} 

int size({Type ","}+ ets) = size([et | et <- ets]);

Type makeTupleType({Type ","}+ ets) =  [Type] "tuple[<intercalate(",", [et | et <- ets])>]";

Type generateArbTupleType(int n, list[Type] prefs){
   arity = 1 + arbInt(5);
   return [Type] "tuple[<intercalate(",", [generateArbType(n-1, prefs) | int _ <- [0 .. 1+arbInt(5)] ])>]";
}

Type generateArbRelType(int n, list[Type] prefs){
   return [Type] "rel[<intercalate(",", [generateArbType(n - 1, prefs) | int _ <- [0 .. 1+arbInt(5)] ])>]";
}

Type generateArbLRelType(int n, list[Type] prefs){
   return [Type] "lrel[<intercalate(",", [generateArbType(n - 1, prefs) | int _ <- [0 .. 1+arbInt(5)] ])>]";
}

// Value generation

public str generateValue(Type tp){
     //println("generateValue(<tp>, <env>)");
     switch(tp){
        case (Type) `bool`:          
            return generateBool();
            
        case (Type) `int`:      
            return generateInt(-100,100);
            
        case (Type) `int[<IntCon f>,<IntCon t>]`:      
            return generateInt(toInt("<f>"), toInt("<t>"));
            
        case (Type) `real`:      
            return generateReal(-100,100);
            
        case (Type) `real[<IntCon f>,<IntCon t>]`:      
            return generateReal(toInt("<f>"), toInt("<t>"));
            
        case (Type) `num`:      
            return generateNumber(-100,100);
            
        case (Type) `num[<IntCon f>,<IntCon t>]`:      
            return generateNumber(toInt("<f>"), toInt("<t>"));
       
       case (Type) `str`:
            return generateString();
            
       case (Type) `loc`:
            return generateLoc();
            
       case (Type) `datetime`:            
            return generateDateTime();
            
       case (Type) `list[<Type et>]`: 
            return generateList(et);
            
       case (Type) `list[<Type et>][<IntCon f>,<IntCon t>]`: 
            return generateList(et, from=toInt("<f>"), to=toInt("<t>"));
            
       case (Type) `set[<Type et>]`: 
            return generateSet(et);
            
       case (Type) `set[<Type et>] [<IntCon f>,<IntCon t>]`: 
            return generateSet(et, from=toInt("<f>"), to=toInt("<t>"));
            
       case (Type) `map[<Type kt>,<Type vt>]`:           
            return generateMap(kt, vt);
       
       case (Type) `map[<Type kt>,<Type vt>][<IntCon f>,<IntCon t>]`:           
            return generateMap(kt, vt, from=toInt("<f>"), to=toInt("<t>"));
       
       case (Type) `tuple[<{Type ","}+ ets>]`:   
            return generateTuple(ets);
            
       case (Type) `rel[<{Type ","}+ ets>]`: 
            return generateSet(makeTupleType(ets));
            
       case (Type) `lrel[<{Type ","}+ ets>]`: 
            return generateList(makeTupleType(ets));
            
       case (Type) `value`:               
            return generateArb(0, baseTypes);
     }
     throw "Unknown type: <tp>";
}

set[set[str]] vocabularies =
{
// Letters:
{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"},

// Sesame Street:
{"Bert", "Ernie", "Big Bird", "Cookie Monster", "Grover", "Elmo",
"Slimey the Worm", "Telly Monster", "Count Von Count", "Countess Darling Von Darling",
"Countess Von Backwards", "Guy Smiley", "Barkley the Dog", "Little Bird",
"Forgetful Jones", "Bruno", "Hoots The Owl", "Prince Charming",
"Mumford the Magician", "Two-Headed Monster"},

// Star trek

// Dr Who

// Star Wars:
{"Jar Jar Binks", "C-3PO", "E-3PO", "Boba Fett", "Isolder", "Jabba the Hut",
"Luke Skywalker", "Obi-Wan Kenobi", "Darth Sidious", "Pincess Leia",
"Emperor Palpatine", "R2-D2", "Admiral Sarn", "Boba Fett", 
"Anakin Skywalker", "Sy Snootles", "Han Solo", "Tibor", "Darth Vader",
"Ailyn Vel", "Yoda", "Zekk", "Joh Yowza"},

// Game of Thrones:
{"Tyrion", "Cersei", "Daenerys", "Petyr", "Jorah", "Sansa", "Arya",
 "Theon", "Bran", "Sandor", "Joffrey", "Catelyn", "Robb", "Ned",
 "Viserys", "Khal", "Varys", "Samwell", "Bronn", "Tywin", "Shae",
 "Jeor", "Gendry", "Tommen", "Jaqen", "Davos", "Melisandre",
 "Margaery", "Stannis", "Ygritte", "Talisa", "Brienne", "Gilly",
 "Roose", "Tormund", "Ramsay", "Daario", "Missandei", "Eilaria",
 "The High Sparrow"},
 
 // World of Warcraft:

 {"Archimonde", "Kil\'jaeden", "Mannoroth", "Ner\'zhul", "Sargeras",
 "Balnazzar", "Magtheridon", "Mal\'Ganis", "Tichondrius", "Varimathras",
 "Azgalor", "Hakkar", "Kazzak", "Detheroc", "Akama", "Velen", 
 "Alexstrasza", "Malygos", "Neltharion", "Nozdormu", "Ysera",
 "Korialstrasz", "Kalecgos", "Brann", "Muradin"},
 
// Fruits:
{"Blackcurrant", "Redcurrant", "Gooseberry", "Eggplant", "Guava",
"Pomegranate", "Kiwifruit", "Grape", "Cranberry", "Blueberry",  "Pumpkin",
"Melon", "Orange", "Lemon", "Lime", "Grapefruit", "Blackberry",
 "Raspberry", "Pineapple", "Fig", "Apple", "Strawberry",
 "Mandarin", "Coconut", "Date", "Prune", "Lychee", "Mango", "Papaya",
 "Watermelon"},

// Herbs and spices:
{"Anise", "Basil", "Musterd", "Nutmeg", "Cardamon", "Cayenne", 
"Celery", "Pepper", "Cinnamon", "Coriander", "Cumin",
"Curry", "Dill", "Garlic", "Ginger", "Jasmine",
"Lavender", "Mint", "Oregano", "Parsley", "Peppermint",
"Rosemary", "Saffron", "Sage", "Sesame", "Thyme",
"Vanilla", "Wasabi"}

};      
       
public str generateBool(){
   return toString(arbBool());
}
 
 public str generateInt(int from, int to){
   return toString(from + arbInt(to - from));
}
 
 public str generateReal(int from, int to){
   return toString(from + arbReal() * (to - from));
}

public str generateNumber(int from, int to){
   return (arbBool()) ? generateInt(from, to) : generateReal(from, to);
}

public str generateLoc(){
  return "|file:///home/paulk/pico.trm|(0,1,\<2,3\>,\<4,5\>)";
   /*
   scheme = getOneFrom(["http", "file", "stdlib", "cwd"]);
   authority = "auth";
   host = "www.cwi.nl";
   port = "80";
   path = "/a/b";

uri: the URI of the location. Also subfields of the URI can be accessed:

scheme: the scheme (or protocol) like http or file. Also supported is cwd: for current working directory (the directory from which Rascal was started).

authority: the domain where the data are located.

host: the host where the URI is hosted (part of authority).

port: port on host (part ofauthority).

path: path name of file on host.

extension: file name extension.

query: query data

fragment: the fragment name following the path name and query data.

user: user info (only present in schemes like mailto).

offset: start of text area.

length: length of text area

begin.line, begin.column: begin line and column of text area.

end.line, end.column end line and column of text area.
*/
}

public str generateDateTime(){
   year = 1990 + arbInt(150);
   month = 1 + arbInt(11);
   day = 1 + arbInt(6);
   dt1 = createDate(year, month, day);
   if(arbBool())
      return "<dt1>";
   hour = arbInt(24);
   minute = arbInt(60);
   second = arbInt(60);
   milli = arbInt(1000);
   dt2 = createTime(hour, minute, second, milli);
  return "<joinDateAndTime(dt1, dt2)>";
}

public str generateString(){
  vocabulary = getOneFrom(vocabularies);
  return "\"<getOneFrom(vocabulary)>\"";
}

public str generateList(Type et, int from=0, int to=5){
   n = from;
   if(from < to)
      n = from + arbInt(to - from + 1);
   elms = [];
   while(size(elms) < n){
        elms += generateValue(et);
   }
   return "[<intercalate(", ", elms)>]";
}

public str generateSet(Type et, int from=0, int to=5){
   n = from;
   if(from < to)
      n = from + arbInt(to - from + 1);
   elms = [];
   attempt = 0;
   while(size(elms) < n && attempt < 100){
     attempt += 1;
     elm = generateValue(et);
     if(elm notin elms)
        elms += elm;
   }
   return "{<intercalate(", ", elms)>}";
}

public str generateMap(Type kt, Type vt,  int from=0, int to=5){
   keys = { generateValue(kt) | int _ <- [from .. arbInt(to)] }; // ensures unique keys
   keyList = toList(keys);
   return "(<for(i <- index(keyList)){><(i==0)?"":", "><keyList[i]>: <generateValue(vt)><}>)";
}

public str generateTuple({Type ","}+ ets){
   return intercalate(", ", [generateValue(et) | et <-ets]);
}

public str generateTuple(list[Type] ets){
   return intercalate(", ", [generateValue(et) | et <-ets]);
}

public str generateRel({Type ","}+ ets){
   return "\<<for(int i <- [0 .. size(ets)]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}
public str generateRel(list[Type] ets){
   return "\<<for(int i <- [0 .. size(ets)]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateLRel({Type ","}+ ets){
   return "\<<for(int i <- [0 .. size(ets)]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateArb(int n, list[Type] prefs){
   if(n <= 0)
      return generateValue(getOneFrom(prefs));
      
   switch(arbInt(5)){
     case 0: return generateList(generateArbType(n-1, prefs));
     case 1: return generateSet(generateArbType(n-1, prefs));
     case 2: return generateMap(generateArbType(n-1, prefs), generateArbType(n-1, prefs));
     case 3: return generateTuple(prefs);
     case 4: return generateRel(prefs);
   }
   
   return generateValue(getOneFrom(prefs));
} 