package org.rascalmpl.test.parser.benchmark;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.NodeToUPTR;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= SSS | SS | a | epsilon
*/
public class WorstCase extends SGTDBF{
	private final static IConstructor SYMBOL_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_epsilon = VF.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_SS = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_S, SYMBOL_S), SYMBOL_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_SSS = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_S, SYMBOL_S, SYMBOL_S), SYMBOL_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_epsilon = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_epsilon), SYMBOL_S, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_S0 = new NonTerminalStackNode(0, 0, "S");
	private final static AbstractStackNode NONTERMINAL_S1 = new NonTerminalStackNode(1, 1, "S");
	private final static AbstractStackNode NONTERMINAL_S2 = new NonTerminalStackNode(2, 2, "S");
	private final static AbstractStackNode TERMINAL_a5 = new LiteralStackNode(5, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode EP6 = new EpsilonStackNode(6, 0);
	
	private final static AbstractStackNode[] SS = new AbstractStackNode[]{NONTERMINAL_S0, NONTERMINAL_S1};
	private final static AbstractStackNode[] SSS = new AbstractStackNode[]{NONTERMINAL_S0, NONTERMINAL_S1, NONTERMINAL_S2};
	private final static AbstractStackNode[] a = new AbstractStackNode[]{TERMINAL_a5};
	private final static AbstractStackNode[] ep = new AbstractStackNode[]{EP6};
	
	private final static VoidActionExecutor actionExecutor = new VoidActionExecutor();
	
	public WorstCase(){
		super();
	}
	
	public void S(){
		expect(PROD_S_SS, SS);
		
		expect(PROD_S_SSS, SSS);
		
		expect(PROD_S_a, a);
		
		expect(PROD_S_epsilon, ep);
	}
	
	private final static int ITERATIONS = 5;
	
	private static char[] createInput(int size){
		char[] input = new char[size];
		for(int i = size - 1; i >= 0; --i){
			input[i] = 'a';
		}
		return input;
	}
	
	private static void cleanup() throws Exception{
		System.gc();
		System.gc();
		Thread.sleep(1000);
	}
	
	private static void runTest(char[] input) throws Exception{
		ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
		
		long total = 0;
		long lowest = Long.MAX_VALUE;
		for(int i = ITERATIONS - 1; i >= 0; --i){
			cleanup();
			
			long start = tmxb.getCurrentThreadCpuTime();
			WorstCase wc = new WorstCase();
			wc.parse(NONTERMINAL_START_S, null, input, actionExecutor, new NodeToUPTR());
			long end = tmxb.getCurrentThreadCpuTime();
			
			long time = (end - start) / 1000000;
			total += time;
			lowest = (time < lowest) ? time : lowest;
			
			//System.out.println(input.length+": intermediate time: "+time+"ms"); // Temp
		}
		System.out.println(input.length+": avg="+(total / ITERATIONS)+"ms, lowest="+lowest+"ms");
	}
	
	public static void main(String[] args) throws Exception{
		// Warmup.
		char[] input = createInput(5);
		for(int i = 9999; i >= 0; --i){
			WorstCase wc = new WorstCase();
			wc.parse(NONTERMINAL_START_S, null, input, actionExecutor, new NodeToUPTR());
		}
		
		// The benchmarks.
		for(int i = 50; i <= 300; i += 50){
			input = createInput(i);
			runTest(input);
		}
	}
}
