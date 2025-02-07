module lang::rascalcore::compile::CompileTimeError

import Message;

data Exception
    = CompileTimeError(Message msg)
    | InternalCompilerError(Message msg)
    | rascalTplVersionError(str txt)
    | rascalBinaryNeedsRecompilation(str moduleName, Message msg)
    ;