module Message

import ParseTree;

public anno Message      Tree@message;
public anno set[Message] Tree@messages;
public anno loc          Message@\loc;

@doc{
Messages can be used to communicate information about source texts.
They can be interpreted by IDE's to display type errors and warnings, etc.
}
data Message = error(str msg)
             | warning(str msg)
             | info(str msg);

public Message error(loc source, str msg) {
  return error(msg)[@\loc=source];
}

public Message warning(loc source, str msg) {
  return warning(msg)[@\loc=source];
}

public Message info(loc source, str msg) {
  return info(msg)[@\loc=source];
}


