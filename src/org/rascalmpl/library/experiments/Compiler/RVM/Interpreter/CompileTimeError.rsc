module experiments::Compiler::RVM::Interpreter::CompileTimeError

import Message;

data Exception
    = CompileTimeError(Message msg)
    ;