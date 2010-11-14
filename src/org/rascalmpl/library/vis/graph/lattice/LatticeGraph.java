package org.rascalmpl.library.vis.graph.lattice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;

/**

 * Lattice layout.
 * 
 * 
 * @author paulk
 * 
 */
public class LatticeGraph extends Figure {
	private final Double phi = 0.4*Math.PI;
	protected ArrayList<LatticeGraphNode> nodes;
	protected ArrayList<LatticeGraphEdge> edges;
	private HashMap<String, LatticeGraphNode> registered;
	IEvaluatorContext ctx;
	IList props;
	
	// Fields for force layout
	protected float springConstant;
	protected float springConstant2;
	protected int temperature;
	private static boolean debug = false;
	private final boolean lattice;
	private LatticeGraphNode topNode = null, bottomNode = null;
	private HashSet<LatticeGraphNode> visit = new HashSet<LatticeGraphNode>();
	
	public LatticeGraph(FigurePApplet fpa, PropertyManager properties, IList nodes,
			IList edges, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.nodes = new ArrayList<LatticeGraphNode>();
		this.ctx = ctx;
		width = getWidthProperty();
		height = getHeightProperty();

		registered = new HashMap<String,LatticeGraphNode>();
		for(IValue v : nodes){

			IConstructor c = (IConstructor) v;
			Figure ve = FigureFactory.make(fpa, c, properties, ctx);
			String name = ve.getIdProperty();

			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Id property should be defined", v, ctx.getCurrentAST(), ctx.getStackTrace());

			LatticeGraphNode node = new LatticeGraphNode(this, name, ve);
			this.nodes.add(node);
			register(name, node);
		}

		this.edges = new ArrayList<LatticeGraphEdge>();
		for (IValue v : edges) {
			IConstructor c = (IConstructor) v;
			LatticeGraphEdge e = FigureFactory.makeLatticeGraphEdge(this, fpa, c, properties,
					ctx);
			this.edges.add(e);
			e.getFrom().addOut(e.getTo());
			e.getTo().addIn(e.getFrom());
		}
		
		lattice = isLattice();
		if (lattice)
			assignRank();
	}
	
	public void register(String name, LatticeGraphNode nd){
		registered.put(name, nd);
	}

	public LatticeGraphNode getRegistered(String name) {
		return registered.get(name);
	}
	
	private void initialPlacement(){
		
/*		
		LatticeGraphNode root = null;
		// for(LatticeGraphNode n : nodes){
		// if(n.in.isEmpty()){
		// root = n;
		// break;
		// }
		// }
		// if(root != null){
		// root.x = width/2;
		// root.y = height/2;
		// }
		for (LatticeGraphNode n : nodes) {
			if (n != root) {
				n.x = fpa.random(width);
				n.y = fpa.random(height);
			}
		}
*/
	}

	@Override
	public
	void bbox() {

		initialPlacement();


		// Now scale (back or up) to the desired width x height frame
		float minx = Float.MAX_VALUE;
		float maxx = Float.MIN_VALUE;
		float miny = Float.MAX_VALUE;
		float maxy = Float.MIN_VALUE;

		for(LatticeGraphNode n : nodes){
			float w2 = n.width()/2;
			float h2 = n.height()/2;
			if(n.x - w2 < minx)

				minx = n.x - w2;
			if (n.x + w2 > maxx)
				maxx = n.x + w2;

			if (n.y - h2 < miny)
				miny = n.y - h2;
			if (n.y + h2 > maxy)
				maxy = n.y + h2;
		}

		float scalex = width / (maxx - minx);
		float scaley = height / (maxy - miny);

		for (LatticeGraphNode n : nodes) {
			n.x = n.x - minx;
			n.x *= scalex;
			n.y = n.y - miny;
			n.y *= scaley;
		}
		if (lattice) {
			// To y coordinate is assigned rank 
			for (LatticeGraphNode n : nodes) {
				n.x = (float) (n.x*Math.cos(phi)+n.y*Math.sin(phi));		
				n.y = (n.rank*height)/bottomNode.rankTop;
			}

		}
	}

	@Override
	public
	void draw(float left, float top) {
		this.left = left;
		this.top = top;

		applyProperties();
		for (LatticeGraphEdge e : edges)
			e.draw(left, top);
		for (LatticeGraphNode n : nodes) {
			n.draw(left, top);
		}
	}

	@Override
	public boolean mouseOver(int mousex, int mousey) {
		for (LatticeGraphNode n : nodes) {
			if (n.mouseOver(mousex, mousey))
				return true;
		}
		return super.mouseOver(mousex, mousey);
	}

	@Override
	public boolean mousePressed(int mousex, int mousey) {
		for (LatticeGraphNode n : nodes) {
			if (n.mousePressed(mousex, mousey))
				return true;
		}
		return super.mouseOver(mousex, mousey);
	}

	public boolean isLattice() {
		boolean r1 = false, r2 = false;
		topNode = bottomNode = null;
		for (LatticeGraphNode n : nodes) {
			if (n.out.isEmpty()) {
				if (!r1)
					bottomNode = n;
				else
					bottomNode = null;
				r1 = true;
			}
			if (n.in.isEmpty()) {
				if (!r2)
					topNode = n;
				else
					topNode = null;
				r2 = true;
			}
		}
		return topNode != null && bottomNode != null;
	}

	private int assignRankTop(LatticeGraphNode node) {
		visit.add(node);
		int rank = node.rankTop;
		for (LatticeGraphNode n : node.out)
			if (!visit.contains(n)) {
				n.rankTop = node.rankTop + 1;
				int r = assignRankTop(n);
				if (r > rank)
					rank = r;
			}
		return rank;
	}

	private int assignRankBottom(LatticeGraphNode node) {
		visit.add(node);
		int rank = node.rankBottom;
		for (LatticeGraphNode n : node.in) {
			if (!visit.contains(n)) {
				n.rankBottom = node.rankBottom + 1;
				int r = assignRankBottom(n);
				if (r > rank)
					rank = r;
			}
		}
		return rank;
	}

	private void assignRank() {
		visit.clear();
		int maxTop = assignRankTop(topNode);
		visit.clear();
		assignRankBottom(bottomNode);
		for (LatticeGraphNode n : nodes) {
			// n.rank = n.rankTop - n.rankBottom + maxBottom;
			n.rank = n.rankBottom - n.rankTop + maxTop;
		}
	}

}
