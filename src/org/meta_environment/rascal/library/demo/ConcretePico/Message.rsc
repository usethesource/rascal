module demo::ConcretePico::Message

/* A common format for messages that are generated during analysis */

public data Message = message(loc l, str msg);
