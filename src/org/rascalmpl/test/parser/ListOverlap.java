package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.CharStackNode;
import org.rascalmpl.parser.sgll.stack.ListStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class ListOverlap extends SGLL implements IParserTest{
	private static IConstructor read(java.lang.String s, Type type){
		try{
			return (IConstructor) new StandardTextReader().read(vf, org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
		}catch(FactTypeUseException e){
			throw new RuntimeException("unexpected exception in generated parser", e);
		}catch(IOException e){
			throw new RuntimeException("unexpected exception in generated parser", e);
		}
	}
	
	private static final IConstructor iter_star_Id = read("regular(\\iter-star(sort(\"Id\")),\\no-attrs())", Factory.Production);
	private static final IConstructor iter_star_layout_elems_iter_star_seps_Id_iter_star_layout_iter_star_layout_Decl = read("prod([\\iter-star(layout()),label(\"elems\",\\iter-star-seps(sort(\"Id\"),[\\iter-star(layout())])),\\iter-star(layout())],sort(\"Decl\"),\\no-attrs())", Factory.Production);
	private static final IConstructor iter_star_layout = read("regular(\\iter-star(layout()),\\no-attrs())", Factory.Production);
	private static final IConstructor char32_Whitespace = read("prod([\\char-class([range(32,32)])],sort(\"Whitespace\"),\\no-attrs())", Factory.Production);
	private static final IConstructor char97_Id = read("prod([\\char-class([range(97,97)])],sort(\"Id\"),\\no-attrs())", Factory.Production);
	
	public ListOverlap(){
		super();
	}
	
	public void Whitespace(){
		expect(char32_Whitespace, new CharStackNode(1, new char[][]{{32, 32}}));
	}
	
	public void Decl(){
		expect(iter_star_layout_elems_iter_star_seps_Id_iter_star_layout_iter_star_layout_Decl,
				new ListStackNode(3, iter_star_layout, new NonTerminalStackNode(4, "Whitespace"), false),
				new ListStackNode(5, iter_star_Id, new NonTerminalStackNode(6, "Id"), false),
				new ListStackNode(7, iter_star_layout, new NonTerminalStackNode(8, "Whitespace"), false));
	}
	
	public void Id(){
		expect(char97_Id, new CharStackNode(10, new char[][]{{97, 97}}));
	}
	
	public IConstructor executeParser(){
		return parse("Decl", null, "  a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([\\iter-star(layout()),label(\"elems\",\\iter-star-seps(sort(\"Id\"),[\\iter-star(layout())])),\\iter-star(layout())],sort(\"Decl\"),\\no-attrs()),[appl(regular(\\iter-star(layout()),\\no-attrs()),[appl(prod([\\char-class([range(32,32)])],sort(\"Whitespace\"),\\no-attrs()),[char(32)]),appl(prod([\\char-class([range(32,32)])],sort(\"Whitespace\"),\\no-attrs()),[char(32)])]),appl(regular(\\iter-star(sort(\"Id\")),\\no-attrs()),[appl(prod([\\char-class([range(97,97)])],sort(\"Id\"),\\no-attrs()),[char(97)])]),appl(regular(\\iter-star(layout()),\\no-attrs()),[])]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		ListOverlap lo = new ListOverlap();
		IConstructor result = lo.parse("Decl", null, "  a".toCharArray());
		System.out.println(result);
		
		System.out.println("? <- good");
	}
}
