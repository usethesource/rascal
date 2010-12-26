package org.rascalmpl.library.vis.graph.lattice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * 
 * Lattice layout.
 * 
 * This layout is activated by by the property: hint("lattice").
 * 
 * 
 * @author bertl
 * 
 */
public class LatticeGraph extends Figure {
	// private final Double phi = 0.4 * Math.PI;
	protected ArrayList<LatticeGraphNode> nodes;
	protected ArrayList<LatticeGraphEdge> edges;
	private HashMap<String, LatticeGraphNode> registered;
	private LinkedList<LatticeGraphNode> nextLayer = new LinkedList<LatticeGraphNode>();
	IEvaluatorContext ctx;
	ArrayList<LatticeGraphNode>[] layers;

	// private static boolean debug = false;
	private LatticeGraphNode topNode = null, bottomNode = null;
	private HashMap<LatticeGraphNode, LatticeGraphNode> visit = new HashMap<LatticeGraphNode, LatticeGraphNode>();

	public LatticeGraph(FigurePApplet fpa, IPropertyManager properties,
			IList nodes, IList edges, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.nodes = new ArrayList<LatticeGraphNode>();
		this.ctx = ctx;
		width = getWidthProperty();
		height = getHeightProperty();

		registered = new HashMap<String, LatticeGraphNode>();
		for (IValue v : nodes) {

			IConstructor c = (IConstructor) v;
			Figure ve = FigureFactory.make(fpa, c, properties, ctx);
			String name = ve.getIdProperty();

			if (name.length() == 0)
				throw RuntimeExceptionFactory.figureException(
						"Id property should be defined", v,
						ctx.getCurrentAST(), ctx.getStackTrace());

			LatticeGraphNode node = new LatticeGraphNode(name, ve);
			this.nodes.add(node);
			register(name, node);
		}

		this.edges = new ArrayList<LatticeGraphEdge>();
		for (IValue v : edges) {
			IConstructor c = (IConstructor) v;
			LatticeGraphEdge e = FigureFactory.makeLatticeGraphEdge(this, fpa,
					c, properties, ctx);
			this.edges.add(e);
			e.getFrom().addOut(e.getTo());
			e.getTo().addIn(e.getFrom());
		}

		if (!isLattice())
			throw RuntimeExceptionFactory.figureException("Not a lattice",
					null, ctx.getCurrentAST(), ctx.getStackTrace());

		assignRank();
	}

	public void register(String name, LatticeGraphNode nd) {
		registered.put(name, nd);
	}

	public LatticeGraphNode getRegistered(String name) {
		return registered.get(name);
	}


	@Override
	public void bbox() {
		int i = 0;
		for (ArrayList<LatticeGraphNode> layer : layers) {
			int s = layer.size();
			// System.err.println("layer.size:"+s);
			if (s > 0) {
				float step = width / (s + 1);
				// System.err.println("width:"+width);
				// System.err.println("step:"+step);
				float x = i % 2 == 0 ? step / 2 : (width - step / 2);
				for (LatticeGraphNode n : layer) {
					// n.x = (float) (n.x*Math.cos(phi)+n.y*Math.sin(phi));
					n.x = x;
					x += (i % 2 == 0 ? step : -step);
					n.y = (n.rank * height) / layers.length;
					// System.err.println("y:"+n.y);
				}
				i++;
			}
		}

	}

	@Override
	public void draw(float left, float top) {
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

	

	private int assignRank(final boolean top) {
		LatticeGraphNode first = top ? topNode : bottomNode;
		nextLayer.add(first);
		visit.put(first, first);
		do {
			LinkedList<LatticeGraphNode> newLayer = new LinkedList<LatticeGraphNode>();
			while (!nextLayer.isEmpty()) {
				LatticeGraphNode node = nextLayer.remove();
				for (LatticeGraphNode n : (top ? node.out : node.in)) {
					LatticeGraphNode oldNode = visit.put(n, n);
					if (oldNode == null) {
						newLayer.add(n);
						if (top)
							n.rankTop = node.rankTop + 1;
						else
							n.rankBottom = node.rankBottom + 1;
					} else {
						if (top)
							oldNode.rankTop = node.rankTop + 1;
						else
							oldNode.rankBottom = node.rankBottom + 1;
					}
				}
			}
			nextLayer = newLayer;
		} while (!nextLayer.isEmpty());
		return top ? bottomNode.rankTop : topNode.rankBottom;
	}

	@SuppressWarnings("unchecked")
	private void assignRank() {
		visit.clear();
		int maxTop = assignRank(true);
		// System.err.println("maxTop:"+maxTop);
		visit.clear();
		int maxBottom = assignRank(false);
		// System.err.println("maxBottom:"+maxBottom);
		int len = maxBottom + maxTop + 1;
		layers = new ArrayList[len];
		for (int i = 0; i < len; i++)
			layers[i] = new ArrayList<LatticeGraphNode>();
		for (LatticeGraphNode n : nodes) {
			// n.rank = n.rankTop - n.rankBottom + maxBottom;
			n.rank = n.rankBottom - n.rankTop + maxTop;
			layers[n.rank].add(n);
		}
	}

}
