module lang::rascal::tutor::Output

extend Message;
import ParseTree;
import String;

data Output 
  = line(str content)
  | err(Message message)
  | details(list[str] order)
  | search(list[str] contents, str fragment)
  | \docTag(str tagName, list[Output] output)
  ;


@synopsis{single line comment with a lonely callout}
Output out(/^<pre:.*><t:\/\*\/\s*\<\s*[0-9]\s*\>><post:\s*\*\/\s*>$/) = out("<pre> <callout(t)> <post>");

@synopsis{multi line comment with a lonely callout}
Output out(/^<pre:.*><t:\/\/\s*\<\s*[0-9]\s*\>><post:\s*>$/) = out("<pre> <callout(t)> <post>");

@synopsis{bullets with callouts}
Output out(/^<pre:\s*\*>\s+<t:\<\s*[0-9]\s*\>><post:.*>$/) = out("<pre><callout(trim(t))><post>");

@synopsis{callouts as bullets}
Output out(/^<pre:\s*><t:\<\s*[0-9]\s*\>><post:.*>$/) = out("<pre>*<callout(trim(t))><post>");

default Output out(str output) = line(output);

Output empty() = line("");


@synopsis{replace all characters by space, except the digits by callout digits}
str callout(str input) = visit(input) {
  case /^<ix:[0-9]>/ => callout(toInt(ix))
  case /^./          => " "
};

str callout(0) = "⓿";
str callout(int i) = "<char(0x2775 + i)>" when i >= 1 && i <= 9;
