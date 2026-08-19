package rascal.lang.rascal.grammar;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $ParserGenerator_$I  {
    IValue ciliterals2ints(IValue $0);
    IValue computeDontNests(IValue $0, IValue $1, IValue $2);
    IValue esc(IValue $0);
    IValue escId(IValue $0);
    IValue generateAltExpects(IValue $0, IValue $1);
    IValue generateCharClassArrays(IValue $0);
    IValue generateClassConditional(IValue $0);
    IValue generateNewItems(IValue $0);
    IValue generateParseMethod(IValue $0, IValue $1);
    IValue generateRangeConditional(IValue $0);
    IValue generateSeparatorExpects(IValue $0, IValue $1);
    IValue generateSequenceExpects(IValue $0, IValue $1);
    IValue getItemId(IValue $0, IValue $1, IValue $2);
    IValue getParserMethodName(IValue $0);
    IValue getType(IValue $0);
    IValue isNonterminal(IValue $0);
    IValue literals2ints(IValue $0);
    IValue makeUnique(IValue $0);
    IValue newGenerate(IValue $0, IValue $1, IValue $2);
    IValue split(IValue $0);
    IValue sym2name(IValue $0);
    IValue sym2newitem(IValue $0, IValue $1, IValue $2);
    IValue uu(IValue $0);
    IValue v2i(IValue $0);
    IValue value2id(IValue $0);
}