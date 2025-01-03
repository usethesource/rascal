module lang::rascal::tests::library::lang::json::JSONIOTests

import String;
import lang::json::IO;
import util::UUID;
import util::Maybe;
import IO;
import util::Math;
import Type;
import DateTime;
import List;
import Node;

loc targetFile = |memory://test-tmp/test-<"<uuidi()>">.json|;
public int maxLong = floor(pow(2,63));

bool writeRead(type[&T] returnType, &T dt, value (value x) normalizer = value(value x) { return x; }, bool dateTimeAsInt=false, bool unpackedLocations=false, bool explicitConstructorNames=false, bool explicitDataTypes=false) {
    json = asJSON(dt, dateTimeAsInt=dateTimeAsInt, unpackedLocations=unpackedLocations, explicitConstructorNames=explicitConstructorNames, explicitDataTypes=explicitDataTypes);
    readBack = normalizer(parseJSON(returnType, json, explicitConstructorNames=explicitConstructorNames, explicitDataTypes=explicitDataTypes));
    if (readBack !:= normalizer(dt) /* ignores additional src fields */) {
        println("What is read back, a <type(typeOf(readBack),())>:");
        iprintln(readBack);
        println("Is different from the original, a <type(typeOf(normalizer(dt)),())>:");
        iprintln(normalizer(dt));
        return false;
    }

    return true;
}
	
// only single constructors supported for now
data DATA1 = data1(int n);
data DATA2 = data2(str n);
data DATA3 = data3(int n, str kw = "abc");
data Enum = x() | y() | z();
data DATA4 = data4(Enum e = x());

test bool writeReadIsTheSameAsAsJSONparseJSON(value example) {
    jsonFile = |memory://jsontests/example.json|;
    writeJSON(jsonFile, example);
    written = readFile(jsonFile);

    // asJON == writeJSON 
    assert asJSON(example) == written;

    // parseJSON == readJSON
    assert toDefaultRec(parseJSON(#value, written)) 
        == toDefaultRec(readJSON(#value, jsonFile));

    return true;
}

test bool jsonWithBool1(bool dt) = writeRead(#bool, dt);
test bool jsonWithInt1(int dt) = writeRead(#int, dt);
test bool jsonWithReal1(real dt) = writeRead(#real, dt);
test bool jsonWithRat1(rat dt) = writeRead(#rat, dt);
test bool jsonWithNum1(num dt) = writeRead(#num, dt, normalizer=numNormalizer);

test bool jsonWithLoc1(loc dt) = writeRead(#loc, dt);
test bool jsonWithLoc2(loc dt) = writeRead(#loc, dt, unpackedLocations=true);
test bool jsonWithStr1(str dt) = writeRead(#str, dt);
test bool jsonWithDatetime1(datetime dt) = writeRead(#datetime, dt);
test bool jsonWithDatetime2(datetime dt) = writeRead(#datetime, dt, dateTimeAsInt=true);
test bool jsonWithList1(list[int] dt) = writeRead(#list[int], dt);
test bool jsonWithSet1(set[int] dt) = writeRead(#set[int], dt);
test bool jsonWithMap1(map[int, int] dt) = writeRead(#map[int,int], dt);
test bool jsonWithNode1(node  dt) = writeRead(#node, dt, normalizer = toDefaultRec);

test bool jsonWithDATA11(DATA1 dt) = writeRead(#DATA1, dt);
test bool jsonWithDATA21(DATA2 dt) = writeRead(#DATA2, dt);
test bool jsonWithDATA12(DATA1 dt) = writeRead(#DATA1, dt, explicitDataTypes=true);
test bool jsonWithDATA22(DATA2 dt) = writeRead(#DATA2, dt, explicitDataTypes=true);
test bool jsonWithDATA13(DATA1 dt) = writeRead(#DATA1, dt, explicitConstructorNames=true);
test bool jsonWithDATA23(DATA2 dt) = writeRead(#DATA2, dt, explicitConstructorNames=true);

@synopsis{all values can be written and read again}
@description{
However sets are always read back in as lists if we don't have 
a specific abstract data-type that can enforce sets.
}
test bool jsonRandom1(value dt) = writeRead(#value, dt, normalizer=toDefaultRec);

test bool json1() = writeRead(#DATA1, data1(123));
test bool json2() = writeRead(#DATA2, data2("123"));
test bool json3() = writeRead(#DATA3, data3(123,kw="123"));
test bool json4(Enum e) = writeRead(#DATA4, data4(e=e));

test bool originTracking() {
   ex2 = readJSON(#node, |std:///lang/rascal/tests/library/lang/json/glossary.json|, trackOrigins=true);   
   content = readFile(|std:///lang/rascal/tests/library/lang/json/glossary.json|);

   poss = [<x.src, x.line> | /node x := ex2, x.line?]; // every node has a .src field, otherwise this fails with an exception

   for (<loc p, int line> <- poss) {
      assert content[p.offset] == "{";                // all nodes start with a {
      assert content[p.offset + p.length - 1] == "}"; // all nodes end with a }
      assert p.begin.line == line;
   }

   return true;
}

value numNormalizer(int i) = i % maxLong when abs(i) > maxLong;
value numNormalizer(real r) = r - round(r) == 0
    ? round(r)
    : fitDouble(r);
default value numNormalizer(value x) = x;


@synopsis{Normalizer used to replace unrecoverable types with their default representatives}
value toDefaultRec(value readBack) {
    value mapToSet(value _:map[value, value] m) = {{k,m[k]} | value k <- m};
    default value mapToSet(value x) = x;

    // first we remove maps cleanly to avoid key collisions caused by normalization
    noMaps = visit(readBack) {
        case value v => mapToSet(v)
    };

    // then we normalize the rest, which could cause key collisions if maps were still there
    return visit(noMaps) {
        case value x => toDefaultValue(x)
    };
}

// The list order depends on the hashcodes of the children
// as the writer is top-down and this rewrite is bottom-up 
// we end up with different lists sometimes (if the elements have been rewritten).
// therefore we normalize all lists to sets, as they can be tested for equality
// regardless of order and hashcode collisions

value toDefaultValue(set[value] x) = x; 
value toDefaultValue(list[value] x) = {*x};
value toDefaultValue(map[void,void] _) =   {};
value toDefaultValue(node x) = { {k, m[k]} | m := getKeywordParameters(x), k <- m}
                             + {*[{"arg<i>", c[i]}  | c := getChildren(x), i <- index(c)]};
value toDefaultValue(map[value,value] m) = {{k,m[k]} | k <- m};
value toDefaultValue(<>) =   {};
value toDefaultValue(<value x>) =   {x};
value toDefaultValue(<value x,value y>) =  toDefaultValue([x,y]);
value toDefaultValue(<value x,value y,value z>) = toDefaultValue([x,y,z]);
value toDefaultValue(<value x,value y,value z,value a>) = toDefaultValue([x,y,z,a]);
value toDefaultValue(<value x,value y,value z,value a,value b>) = toDefaultValue([x,y,z,a,b]);
value toDefaultValue(<value x,value y,value z,value a,value b,value c>) = toDefaultValue([x,y,z,a,b,c]);
value toDefaultValue(<value x,value y,value z,value a,value b,value c,value d>) = toDefaultValue([x,y,z,a,b,c,d]);
value toDefaultValue(<value x,value y,value z,value a,value b,value c,value d,value e>) = toDefaultValue([x,y,z,a,b,c,d,e]);
value toDefaultValue(<value x,value y,value z,value a,value b,value c,value d,value e,value f>) = toDefaultValue([x,y,z,a,b,c,d,e,f]);
value toDefaultValue(loc l) {
    // this simulates the simplications the writer applies
    if (!(l.offset?)) {
        if (l.scheme == "file") {
            return l.path;
        }
        else {
            return "<l>"[1..-1];
        }
    }
    else {
        return "<l>";
    }
}

value toDefaultValue(int i) = i % maxLong when abs(i) > maxLong;
value toDefaultValue(rat r) = {numerator(r), denominator(r)};
value toDefaultValue(datetime t) = printDateTime(t, "yyyy-MM-dd\'T\'HH:mm:ssZ");
value toDefaultValue(real r) = r - round(r) == 0
    ? round(r) 
    : fitDouble(r);
default value toDefaultValue(value x) = x;

test bool accurateParseErrors() {
   ex = readFile(|std:///lang/rascal/tests/library/lang/json/glossary.json|);
   broken = ex[..size(ex)/2] + ex[size(ex)/2+10..];

   try {
       ex2 = parseJSON(#node, broken, trackOrigins=true);   
   }
   catch ParseError(loc l): 
        return l.begin.line == 14;

   try {
        // accurate locations have to be provided also when trackOrigins=false
       ex2 = parseJSON(#node, broken, trackOrigins=false);   
   }
   catch ParseError(loc l): 
        return l.begin.line == 14;

   return true;
}

test bool regression1() = jsonRandom1(("a":12,[]:{}));

data Cons = cons(str bla = "null");

test bool dealWithNull() {
    // use the default nulls map
    assert parseJSON(#map[str,value], "{\"bla\": null}") == ("bla":"null"());

    // using our own nulls map
    assert parseJSON(#map[str,value], "{\"bla\": null}", nulls=(#value:-1)) == ("bla":-1);

    // conflicting entries in the nulls maps: more specific goes first:
    assert parseJSON(#map[str,node], "{\"bla\": null}", nulls=(#value:-1, #node:"null"())) == ("bla":"null"());

    // the builtin Maybe interpreter with null
    assert parseJSON(#map[str,Maybe[str]], "{\"bla\": null}") == ("bla":nothing());

    // the builtin Maybe interpreter with non-null
    assert parseJSON(#map[str,Maybe[str]], "{\"bla\": \"foo\"}") == ("bla":just("foo"));

    // test different specific nulls for different expected types:
    for (t <- [#Maybe[value], #node, #int, #real, #rat, #value, #str,
               #list[value], #set[value], #map[value,value], #loc, #bool]) {
        assert parseJSON(t, "null") == (defaultJSONNULLValues[t]?"default-not-found");
    }

    // test undefined top-level null
    try {
        parseJSON(#int, "null", nulls=());
        assert false;
    }
    catch ParseError(_): assert true;

    // keyword parameters and null
    assert cons(bla="foo") := parseJSON(#Cons, "{\"bla\": \"foo\"}");
    assert cons() := parseJSON(#Cons, "{\"bla\": null}");

    return true;
}

data Example = example(Prop ex = F());

data Prop = T() | F() | and(Prop lhs, Prop rhs) | or(Prop lhs, Prop rhs);

str format(T()) = "true";
str format(F()) = "false";
str format(and(Prop p1, Prop p2)) = "<format(p1)> && <format(p2)>";
str format(or(Prop p1, Prop p2)) = "<format(p1)> || <format(p2)>";

Prop parse(type[Prop] _, "true") = T();
Prop parse(type[Prop] _, "false") = F();

test bool formattingToStringsTest() {
    ex1 = and(and(\T(), \F()),or(\T(), \F()));

    writeJSON(|memory://test-json/formatted.json|, example(ex=ex1), formatter=format);
    source = readFile(|memory://test-json/formatted.json|);

    assert source == "{\"ex\":\"true && false && true || false\"}";

    writeFile(|memory://test-json/printed.json|, "{\"ex\":\"true\"}");

    Example result = readJSON(#Example, |memory://test-json/printed.json|, parser=parse);

    assert result.ex == T();

    return true;
}

test bool explicitConstructorNames() {
    tmp = data4(e=z());
    json = asJSON(tmp, explicitConstructorNames=true);
    
    assert json == "{\"_constructor\":\"data4\",\"e\":{\"_constructor\":\"z\"}}";

    assert tmp2 := parseJSON(#DATA4, json, explicitConstructorNames=true) && tmp2 := tmp;

    // here we can't be sure to get z() back, but we will get some Enum
    assert data4(e=Enum _) := parseJSON(#DATA4, json, explicitConstructorNames=false);

    return true;
}

test bool explicitDataTypes() {
    tmp = data4(e=z());
    json = asJSON(tmp, explicitDataTypes=true);
    
    assert json == "{\"_constructor\":\"data4\",\"_type\":\"DATA4\",\"e\":{\"_constructor\":\"z\",\"_type\":\"Enum\"}}";

    // _constructor and _type must be the first fields
    assert tmp2 := parseJSON(#DATA4, json, explicitDataTypes=true) && tmp := tmp2 ;

    // _type and _constructor may appear in a different order
    flippedJson = "{\"_type\":\"DATA4\",\"_constructor\":\"data4\",\"e\":{\"_constructor\":\"z\",\"_type\":\"Enum\"}}";
    assert tmp2 := parseJSON(#DATA4, flippedJson, explicitDataTypes=true) && tmp := tmp2;

    // here we can't be sure to get z() back, but we will get some Enum
    assert data4(e=Enum _) := parseJSON(#DATA4, json, explicitDataTypes=false);

    return true;
}