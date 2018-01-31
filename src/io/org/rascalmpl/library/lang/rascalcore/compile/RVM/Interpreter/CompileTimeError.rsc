module lang::rascalcore::compile::RVM::Interpreter::CompileTimeError

import Message;

data Exception
    = CompileTimeError(Message msg)
    | InternalCompilerError(Message msg)
    ;