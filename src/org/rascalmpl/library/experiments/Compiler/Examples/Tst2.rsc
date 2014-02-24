module experiments::Compiler::Examples::Tst2

import Message;
import analysis::m3::TypeSymbol;

data Declaration;
anno loc             Declaration@src;
anno loc             Declaration@decl;
anno TypeSymbol      Declaration@typ;
anno list[Modifier]  Declaration@modifiers;
anno list[Message]   Declaration@messages;

data Statement;
anno loc Statement@src;

data Expression;
anno loc Expression@src;
anno loc Expression@decl;
anno TypeSymbol Expression@typ;

data Type;
anno loc Type@name;              
anno TypeSymbol Type@typ;

data Modifier;
