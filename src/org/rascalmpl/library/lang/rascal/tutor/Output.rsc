module lang::rascal::tutor::Output

extend Message;

data Output 
  = out(str content)
  | err(Message message)
  | details(list[str] order)
  | search(list[str] contents, str fragment)
  | \docTag(str tagName, list[Output] output)
  ;

Output empty() = out("");