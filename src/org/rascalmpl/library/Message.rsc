module Message

@doc{
Messages can be used to communicate information about source texts.
They can be interpreted by IDE's to display type errors and warnings, etc.
}
data Message = error(str msg, loc at)
             | warning(str msg, loc at)
             | info(str msg, loc at);

public Message error(loc source, str msg) {
  return error(msg,source);
}

public Message warning(loc source, str msg) {
  return warning(msg,source);
}

public Message info(loc source, str msg) {
  return info(msg,source);
}
