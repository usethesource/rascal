package org.rascalmpl.test.parser;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.CharStackNode;
import org.rascalmpl.parser.sgll.stack.EpsilonStackNode;
import org.rascalmpl.parser.sgll.stack.ListStackNode;
import org.rascalmpl.parser.sgll.stack.LiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.uptr.Factory;

public class Reject3 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_AB = vf.constructor(Factory.Symbol_Sort, vf.string("AB"));
	private final static IConstructor SYMBOL_BC = vf.constructor(Factory.Symbol_Sort, vf.string("BC"));
	private final static IConstructor SYMBOL_ABp = vf.constructor(Factory.Symbol_Sort, vf.string("ABp"));
	private final static IConstructor SYMBOL_BCp = vf.constructor(Factory.Symbol_Sort, vf.string("BCp"));
	private final static IConstructor SYMBOL_X = vf.constructor(Factory.Symbol_Sort, vf.string("X"));
	private final static IConstructor SYMBOL_Y = vf.constructor(Factory.Symbol_Sort, vf.string("Y"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_b = vf.constructor(Factory.Symbol_Lit, vf.string("b"));
	private final static IConstructor SYMBOL_c = vf.constructor(Factory.Symbol_Lit, vf.string("c"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_char_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	private final static IConstructor SYMBOL_char_c = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(99))));
	private final static IConstructor SYMBOL_PLUS_LIST_a = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_char_a);
	private final static IConstructor SYMBOL_PLUS_LIST_b = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_char_b);
	private final static IConstructor SYMBOL_PLUS_LIST_c = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_char_c);
	private final static IConstructor SYMBOL_epsilon = vf.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_START_S_ABp = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_ABp), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_START_S_X = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_X), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_START_S_Y = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_Y), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_ABp_ABPLUSLISTc = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_AB, SYMBOL_PLUS_LIST_a), SYMBOL_ABp, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_BCp_PLUSLISTaBC = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_PLUS_LIST_b, SYMBOL_BC), SYMBOL_BCp, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_AB_PLUSLISTaABb = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a, SYMBOL_AB, SYMBOL_b), SYMBOL_AB, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_AB_empty = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_epsilon), SYMBOL_AB, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_BC_PLUSLISTbBCc = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_b, SYMBOL_BC, SYMBOL_c), SYMBOL_BC, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_BC_empty = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_epsilon), SYMBOL_BC, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_PLUSLISTa = vf.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_a, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_PLUSLISTc = vf.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_c, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_X_ABp = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_ABp), SYMBOL_X, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_X_BCp = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_BCp), SYMBOL_X, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_Y_ABp = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_ABp), SYMBOL_Y, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_Y_BCp = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_BCp), SYMBOL_Y, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_b_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b), SYMBOL_b, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_c_c = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_c), SYMBOL_c, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_AB0 = new NonTerminalStackNode(0, "AB");
	private final static AbstractStackNode NONTERMINAL_AB1 = new NonTerminalStackNode(1, "AB");
	private final static AbstractStackNode NONTERMINAL_BC2 = new NonTerminalStackNode(2, "BC");
	private final static AbstractStackNode NONTERMINAL_BC3 = new NonTerminalStackNode(3, "BC");
	private final static AbstractStackNode NONTERMINAL_ABp4 = new NonTerminalStackNode(4, "ABp");
	private final static AbstractStackNode NONTERMINAL_ABp5 = new NonTerminalStackNode(5, "ABp");
	private final static AbstractStackNode NONTERMINAL_ABp6 = new NonTerminalStackNode(6, "ABp");
	private final static AbstractStackNode NONTERMINAL_BCp7 = new NonTerminalStackNode(7, "BCp");
	private final static AbstractStackNode NONTERMINAL_BCp8 = new NonTerminalStackNode(8, "BCp");
	private final static AbstractStackNode NONTERMINAL_X9 = new NonTerminalStackNode(9, "X");
	private final static AbstractStackNode NONTERMINAL_Y10 = new NonTerminalStackNode(10, "Y");
	private final static AbstractStackNode LITERAL_a11 = new LiteralStackNode(11, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_b12 = new LiteralStackNode(12, PROD_b_b, new char[]{'b'});
	private final static AbstractStackNode LITERAL_b13 = new LiteralStackNode(13, PROD_b_b, new char[]{'b'});
	private final static AbstractStackNode LITERAL_c14 = new LiteralStackNode(14, PROD_c_c, new char[]{'c'});
	private final static AbstractStackNode EPSILON_15 = new EpsilonStackNode(15);
	private final static AbstractStackNode EPSILON_16 = new EpsilonStackNode(16);
	private final static AbstractStackNode CHAR16 = new CharStackNode(16, new char[][]{{'a', 'a'}});
	private final static AbstractStackNode CHAR17 = new CharStackNode(17, new char[][]{{'c', 'c'}});
	private final static AbstractStackNode LIST18 = new ListStackNode(18, PROD_PLUSLISTa, CHAR16, true);
	private final static AbstractStackNode LIST19 = new ListStackNode(19, PROD_PLUSLISTc, CHAR17, true);
	
	public Reject3(){
		super();
	}
	
	public void S(){
		expect(PROD_START_S_ABp, NONTERMINAL_ABp4);
		
		expectReject(PROD_START_S_X, NONTERMINAL_X9);
		expectReject(PROD_START_S_Y, NONTERMINAL_Y10);
	}
	
	public void AB(){
		expect(PROD_AB_PLUSLISTaABb, LITERAL_a11, NONTERMINAL_AB0, LITERAL_b12);
		
		expect(PROD_AB_empty, EPSILON_15);
	}
	
	public void BC(){
		expect(PROD_BC_PLUSLISTbBCc, LITERAL_b13, NONTERMINAL_BC2, LITERAL_c14);

		expect(PROD_BC_empty, EPSILON_16);
	}
	
	public void ABp(){
		expect(PROD_ABp_ABPLUSLISTc, NONTERMINAL_AB1, LIST19);
	}
	
	public void BCp(){
		expect(PROD_BCp_PLUSLISTaBC, LIST18, NONTERMINAL_BC3);
	}
	
	public void X(){
		expect(PROD_X_ABp, NONTERMINAL_ABp5);
		
		expectReject(PROD_X_BCp, NONTERMINAL_BCp7);
	}
	
	public void Y(){
		expect(PROD_Y_BCp, NONTERMINAL_BCp8);
		
		expectReject(PROD_Y_ABp, NONTERMINAL_ABp6);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "abbcc".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		return vf.string(":-1,-1: Syntax error in Parse Error: all trees were filtered.");
	}
	
	public static void main(String[] args){
		Reject3 r3 = new Reject3();
		IConstructor result = r3.parse(NONTERMINAL_START_S, null, "abbcc".toCharArray());
		System.out.println(result);
		
		System.out.println("S() <- good");
	}
}
