module experiments::RascalTutor::ValueGenerator

import Boolean;
import Integer;
import String;
import Real;
import List;
import Set;
import Map;
import Relation;
import IO;
import DateTime;
import experiments::RascalTutor::CourseModel;
     
public str generateValue(str t){
   return generateValue(parseType(t));
}

public str generateValue(RascalType t){
     switch(t){
       case \bool(): 		return toString(arbBool());
       case \int(): 		return toString(arbInt(20));
       case \real(): 		return toString(20 * arbReal());
       case \num(): 		return generateNumber();
       case \str(): 		return generateString();
       case \loc():			return generateLoc();
       case \dateTime():	return generateDateTime();
       case \list(et): 		return generateList(et);
       case \set(et):		return generateSet(et);
       case \map(kt, vt):	return generateMap(kt, vt);
       case \tuple(list[RascalType] ets):	
       						return generateTuple(ets);
       case \rel(list[RascalType] ets):	
       						return generateSet(\tuple(ets));
     }
     throw "Unknown type: <t>";
}

private list[RascalType] baseTypes = [\bool(), \int(), \real(), \num(), \str(), \loc(), \dateTime()];
private list[RascalType] reducedBaseTypes = [\bool(), \int(), \real(), \str()];


public RascalType parseType(str txt){
   switch(txt){
     case /^bool$/:		return \bool();
     case /^int$/: 		return \int();
     case /^real$/: 	return \real();
     case /^num$/: 		return \num();
     case /^str$/: 		return \str();
     case /^loc$/: 		return \loc();
     case /^dateTime$/: return \dateTime();
     case /^list\[<et:.+>\]/:
     					return \list(parseType(et));
     case /^set\[<et:.+>\]$/:
     					return \set(parseType(et));
     					
     case /^map\[<ets:.+>\]$/: {
     						types = parseTypeList(ets);
     						if(size(types) != 2)
     							throw "map type should have to arguments: <txt>";
     					    return \map(types[0], types[1]);
     					}
     					
     case /^tuple\[<ets:.+>\]$/:
     					return \tuple(parseTypeList(ets));
     					
     case /^rel\[<ets:.+>\]$/:
     					return \set(\tuple(parseTypeList(ets)));
     					
     case /^arb\[<depth:[0-9]>\]$/:{
     						return \arb(toInt(depth), baseTypes);
     					}					
     case /^arb\[<depth:[0-9]>,<tps:.+>\]$/:{
     						types = parseTypeList(tps);
     						return \arb(toInt(depth), types);
     					}
     					
     case /^arb\[<tps:.+>\]$/:{
     						types = parseTypeList(tps);
     						return \arb(0, types);
     					}
     					
     case /^arb$/:		return \arb(0, baseTypes);
     
     case /^prev\[<depth:[0-9]>\]$/:
     					return \prev(toInt(depth));
   }
   throw "Parse error in type: <txt>";
}

public list[RascalType] parseTypeList(str txt){
  str prefix = "";
  return
    for(/<et:[^,]+>($|,)/ := txt){
      try {
         if(prefix != "")
         	prefix += ",";
         pt = parseType(prefix + et);
         append pt;
         prefix = "";
      } catch: {
        prefix += et;
      }
    }
}

test parseType("bool") == \bool();
test parseType("int") == \int();
test parseType("real") == \real();
test parseType("num") == \num();
test parseType("str") == \str();
test parseType("loc") == \loc();
test parseType("dateTime") == \dateTime();
test parseType("list[int]") == \list(\int());
test parseType("list[list[int]]") == \list(\list(\int()));
test parseType("set[int]") == \set(\int());
test parseType("set[list[int]]") == \set(\list(\int()));
test parseType("map[str,int]") == \map(\str(),\int());

test parseType("tuple[int,str]") == \tuple([\int(), \str()]);
test parseType("tuple[int,tuple[real,str]]") == \tuple([\int(), \tuple([\real(),\str()])]);
test parseType("rel[int,str]") == \set(\tuple([\int(), \str()]));
test parseType("rel[int,tuple[real,str]]") == \set(\tuple([\int(), \tuple([\real(),\str()])]));
test parseType("arb[0]") == \arb(0, [\bool(),\int(),\real(),\num(),\str(),\loc(),\dateTime()]);
test parseType("set[arb]") == \set(\arb(0,[\bool(),\int(),\real(),\num(),\str(),\loc(),\dateTime()]));

public RascalType generateType(str t){
  return generateType(parseType(t), []);
}

public RascalType generateType(RascalType t, list[RascalType] previous){
     println("generateType(<t>, <previous>)");
     switch(t){
       case \list(et): 		return \list(generateType(et, previous));
       case \set(et):		return \set(generateType(et, previous));
       case \map(kt, vt):  	return \map(generateType(kt, previous), generateType(vt, previous));
       case \tuple(list[RascalType] ets):	
       						return generateTupleType(ets, previous);
       case \rel(list[RascalType] ets):	
       						return generateRelType(ets, previous);
       case \arb(int d, list[RascalType] tps):
       						return generateArbType(d, tps);
       case \prev(int p):	return previous[p];
       default:				return t;
     }
     throw "Unknown type: <t>";
}

public RascalType generateTupleType(list[RascalType] ets, list[RascalType] previous){
   return \tuple([generateType(et, previous) | et <- ets ]);
}

public RascalType generateRelType(list[RascalType] ets, list[RascalType] previous){
   return \rel([generateType(et, previous) | et <- ets ]);
}

test generateType(\bool(), []) == \bool();
test generateType(\int(), []) == \int();
test generateType(\real(), []) == \real();
test generateType(\num(), []) == \num();
test generateType(\loc(), []) == \loc();
test generateType(\dateTime(), []) == \dateTime();
test generateType(\list(\int()), []) == \list(\int());
test generateType(\set(\int()), []) == \set(\int());
test generateType(\map(\str(), \int()), []) == \map(\str(), \int());
test generateType(\tuple([\int(), \str()]), []) == \tuple([\int(), \str()]);
test generateType(\rel([\int(), \str()]), []) == \rel([\int(), \str()]);
test generateType(\prev(0), [\int(), \str()]) == \int();
test generateType(\prev(1), [\int(), \str()]) == \str();
test generateType(\list(\prev(1)), [\int(), \str()]) == \list(\str());


public RascalType generateArbType(int n, list[RascalType] prefs){
   if(n <= 0)
   	  return getOneFrom(prefs);
   	  
   switch(arbInt(5)){
     case 0: return \list(generateArbType(n-1, prefs));
     case 1: return \set(generateArbType(n-1, prefs));
     case 2: return \map(generateArbType(n-1, prefs), generateArbType(n-1, prefs));
     case 3: return generateArbTupleType(n-1, prefs);
     case 4: return generateArbRelType(n-1, prefs);
   }
} 

public RascalType generateArbTupleType(int n, list[RascalType] prefs){
   return \tuple([generateArbType(n - 1, prefs) | int i <- [0 .. arbInt(5)] ]);
}

public RascalType generateArbRelType(int n, list[RascalType] prefs){
   return \rel([generateArbType(n - 1, prefs) | int i <- [0 .. arbInt(5)] ]);
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

// Star Wars:
{"Jar Jar Binks", "C-3PO", "E-3PO", "Boba Fett", "Isolder", "Jabba the Hut",
"Luke Skywalker", "Obi-Wan Kenobi", "Darth Sidious", "Pincess Leia",
"Emperor Palpatine", "R2-D2", "Admiral Sarn", "Boba Fett", 
"Anakin Skywalker", "Sy Snootles", "Han Solo", "Tibor", "Darth Vader",
"Ailyn Vel", "Yoda", "Zekk", "Joh Yowza"},

// Fruits:
{"Blackcurrant", "Redcurrant", "Gooseberry", "Eggplant", "Guava",
"Pomegranate", "Kiwifruit", "Grape", "Cranberry", "Blueberry",	"Pumpkin",
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
 
 public str generateInt(){
   return toString(arbInt(20));
}
 
 public str generateReal(){
   return toString(20 * arbReal());
}

public str generateNumber(){
   return (arbBool()) ? generateInt() : generateReal();
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

public str generateList(RascalType et){
   return "[<for(int i <- [0 .. arbInt(5)]){><(i==0)?"":", "><generateValue(et)><}>]";
}

public str generateSet(RascalType et){
   return "{<for(int i <- [0 .. arbInt(5)]){><(i==0)?"":", "><generateValue(et)><}>}";
}

public str generateMap(RascalType kt, RascalType vt){
   return "(<for(int i <- [0 .. arbInt(5)]){><(i==0)?"":", "><generateValue(kt)> : <generateValue(vt)><}>)";
}

public str generateTuple(list[RascalType] ets){
   return "\<<for(int i <- [0 .. size(ets)-1]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateRel(list[RascalType] ets){
   return "\<<for(int i <- [0 .. size(elts)-1]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateExpr(str expr){
  previous = [];
  return
    visit(expr){
      case /^\<<tp:[a-z0-9,\[\]]+>\>/: {
        println("tp = <tp>, previous = <previous>");
        atp = generateType(parseType(tp), previous);
        println("atp = <atp>");
        previous += atp;
        insert generateValue(atp);
      }
    };
}
     