package org.rascalmpl.library.vis.graph.lattice;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * 
 * Lattice layout.
 * 
 * This layout is activated by by the property: hint("lattice").
 * 
 * 
 * @author bertl used A Genetic Algorithm for drawing ordered sets, Brian Loft
 *         and John Snow
 */
public class LatticeGraph extends Figure implements
		Comparator<LatticeGraph.Organism> {
	private final int E = 10;
	private final int G = 30;
	protected ArrayList<LatticeGraphNode> nodes;
	protected ArrayList<LatticeGraphEdge> edges;
	private HashMap<String, LatticeGraphNode> registered;
	private final boolean debug = false;
	private LinkedList<LatticeGraphNode> nextLayer = new LinkedList<LatticeGraphNode>();
	IEvaluatorContext ctx;
	ArrayList<LatticeGraphNode>[] layers;
	final int border = 20;

	private final int E2 = E * E;

	final private Random rand = new Random();

	// private static boolean debug = false;
	private LatticeGraphNode topNode = null, bottomNode = null;
	private HashMap<LatticeGraphNode, LatticeGraphNode> visit = new HashMap<LatticeGraphNode, LatticeGraphNode>();

	class Organism {
		float[][] x = new float[layers.length][],
				aux = new float[layers.length][];

		final float fitness;

		Organism() {
			int i = 0;
			for (ArrayList<LatticeGraphNode> ns : layers) {
				x[i] = new float[ns.size()];
				aux[i] = new float[ns.size()];
				i++;
			}
			init();
			mutate(20);
			set();
			fitness = -LatticeGraph.this.Z();
		}

		private void mutate(int p) {
			final int r = rand.nextInt(x.length);
			final int n = x[r].length;
			for (int i = 0; i < p; i++)
				if (n > 0) {
					int k = rand.nextInt(n);
					float s = x[r][k];
					final int l = rand.nextInt(n);
					x[r][k] = x[r][l];
					x[r][l] = s;
				}
		}

		Organism(Organism o) {
			for (int i = 0; i < o.x.length; i++) {
				x[i] = new float[o.x[i].length];
				aux[i] = new float[x[i].length];
			}
			for (int i = 0; i < o.x.length; i++)
				for (int j = 0; j < o.x[i].length; j++) {
					x[i][j] = o.x[i][j];
				}
			mutate(1);
			set();
			fitness = -LatticeGraph.this.Z();
		}

		Organism(Organism o1, Organism o2) {
			for (int i = 0; i < o1.x.length; i++) {
				x[i] = new float[o1.x[i].length];
				aux[i] = new float[x[i].length];
			}
			final int cut = o1.x.length > 3 ? 1 + rand.nextInt(o1.x.length - 2)
					: 0;
			for (int i = 0; i < cut; i++)
				for (int j = 0; j < o1.x[i].length; j++) {
					x[i][j] = o1.x[i][j];
				}
			for (int i = cut; i < o2.x.length; i++)
				for (int j = 0; j < o2.x[i].length; j++) {
					x[i][j] = o2.x[i][j];
					;
				}
			set();
			// fitness = LatticeGraph.this.Y() - LatticeGraph.this.C();
			fitness = -LatticeGraph.this.Z();
			// System.err.println("New organism fitness:" + fitness+
			// " "+o1.fitness+" "+o2.fitness);
		}

		private void init() {
			int i = 0;
			for (ArrayList<LatticeGraphNode> ns : layers) {
				for (int j = 0; j < ns.size(); j++)
					x[i][j] = ns.get(j).x;
				i++;
			}
		}

		void set() {
			int i = 0;
			for (ArrayList<LatticeGraphNode> ns : layers) {
				for (int j = 0; j < ns.size(); j++) {
					aux[i][j] = ns.get(j).x;
					ns.get(j).x = x[i][j];
				}
				i++;
			}
		}

		void reset() {
			int i = 0;
			for (ArrayList<LatticeGraphNode> ns : layers) {
				for (int j = 0; j < ns.size(); j++)
					ns.get(j).x = aux[i][j];
				i++;
			}
		}

		/*
		 * float C() { set(); float r = LatticeGraph.this.C(); reset(); return
		 * r; }
		 */

	}

	Organism[] elitePopulation;

	// Organism[][] islands;

	public LatticeGraph(FigurePApplet fpa, IPropertyManager properties,
			IList nodes, IList edges, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.nodes = new ArrayList<LatticeGraphNode>();
		this.ctx = ctx;
		width = getWidthProperty();
		height = getHeightProperty();
		if (debug)
			System.err.println("LatticaGraph");
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
		computeReached();
		initialPlacement();
		elitePopulation = computeFirstElitePopulation();
		evolution();
	}

	public void register(String name, LatticeGraphNode nd) {
		registered.put(name, nd);
	}

	public LatticeGraphNode getRegistered(String name) {
		return registered.get(name);
	}

	public void initialPlacement() {
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
					n.y = border + (n.rank * (height - 2 * border))
							/ layers.length;
					// System.err.println("y:"+n.y);
				}
				i++;
			}
		}
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties();
		for (LatticeGraphEdge e : edges)
			e.draw(left, top);
		for (LatticeGraphNode n : nodes) {
			n.draw(left, top);
		}
	}

	/**
	 * Draw focus around this figure
	 */
	public void drawFocus() {
		// System.err.println("drawFocus: " + this.left);
		if (isVisible()) {
			fpa.stroke(255, 0, 0);
			fpa.strokeWeight(1);
			fpa.noFill();
			fpa.rect(getLeft(), getTop(), width, height);
		}
	}

	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX,
			float centerY, boolean mouseInParent) {
		for (LatticeGraphNode n : nodes) {
			if (n.mouseOver(mousex, mousey, mouseInParent))
				return true;
		}
		return super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}

	private void evolution() {
		for (int i = 0; i < G; i++) {
			Arrays.sort(elitePopulation, this);
			elitePopulation = nextPopulation(elitePopulation);
		}
		Arrays.sort(elitePopulation, this);
		System.err.println("NextPopulation Fitness:"
				+ +elitePopulation[0].fitness);
		elitePopulation[0].set();
	}

	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e) {
		System.err.println("mousePressed:" + this.getClass() + " "
				+ nodes.size());
		for (LatticeGraphNode n : nodes) {
			if (n.mousePressed(mousex, mousey, e))
				return true;
		}
		evolution();
		return false;
		// return super.mousePressed(mousex, mousey, e);
	}

	@Override
	public boolean mouseDragged(int mousex, int mousey) {
		// System.err.println("mouseDragged:"+this.getClass()+" "+nodes.size());
		for (LatticeGraphNode n : nodes) {
			if (n.mousePressed) {
				n.x = mousex;
				n.y = mousey;
				return true;
			}
		}
		return false;
		// return super.mousePressed(mousex, mousey, e);
	}

	@Override
	public boolean mouseReleased() {
		if (debug)
			System.err.println("mouseReleased");
		for (LatticeGraphNode n : nodes) {
			if (n.mouseReleased()) {
				return true;
			}
		}
		return false;
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

	private void computeReached(LatticeGraphNode node) {
		for (LatticeGraphNode n : node.in) {
			LatticeGraphNode oldNode = visit.put(n, n);
			if (oldNode == null)
				computeReached(n);
			node.reached.add(n);
		}
		for (LatticeGraphNode n : node.in)
			for (LatticeGraphNode m : n.reached)
				node.reached.add(m);
	}

	private void computeReached() {
		visit.clear();
		computeReached(bottomNode);
	}

	private boolean isLessThen(LatticeGraphNode a, LatticeGraphNode b) {
		return b.reached.contains(a);
	}

	private float C() {
		float r = 0;
		int i = 0;
		for (LatticeGraphNode n : nodes)
			for (LatticeGraphNode m : n.reached) {
				float t = (n.x - m.x) * (n.x - m.x) + (n.y - m.y) * (n.y - m.y);
				r += t;
				i++;
			}
		return r / i;
	};

	private float Y() {
		float r = 0;
		int i = 0;
		for (ArrayList<LatticeGraphNode> ns : layers) {
			for (LatticeGraphNode n : ns)
				for (LatticeGraphNode m : ns)
					if (!m.equals(n)) {
						final float k = (n.x - m.x) * (n.x - m.x);
						r += (k < 10 ? -10000000 : 0);
						i++;
					}
		}
		return r;
	}

	/* Number Of Crossings */
	private float Z() {
		int r = 0, s = 0;
		for (LatticeGraphEdge e1 : edges)
			for (LatticeGraphEdge e2 : edges)
				if (!e1.equals(e2)) {
					boolean b = new LinSolve(e1, e2).isInside();
					if (b)
						r++;
					else
						s++;
				}
		// System.err.println("r="+r+" s="+s+" t="+(r+s));
		return r;
	}

	private Organism[] computeFirstElitePopulation() {
		Organism[] result = new Organism[E2];
		for (int i = 0; i < E; i++) {
			Organism[] organism = new Organism[E2];
			for (int k = 0; k < E2; k++) {
				final Organism o = new Organism();
				organism[k] = o;
			}
			Arrays.sort(organism, this);
			for (int j = 0; j < E; j++) {
				result[i * E + j] = organism[j];
			}
//			System.err.println("Start Fitness:" + organism[0].fitness + " i="
//					+ i);
		}
		Arrays.sort(result, this);
		result[0].set();
		System.err.println("Init Fitness:" + result[0].fitness);
		return result;
	}

	private Organism[] nextPopulation(Organism[] p) {
		Organism[] result = new Organism[E2];
		for (int i = 0; i < E; i++)
			for (int j = 0; j < E; j++) {
				// System.err.println("i=" + i + " j=" + j);
				Organism o1 = new Organism(p[i], p[j]);
				Organism o2 = new Organism(o1);
				result[E * i + j] = (o1.fitness < o2.fitness) ? o2 : o1;
			}
		return result;
	}

	public int compare(Organism o1, Organism o2) {
		// The biggest first
		return Math.round(o2.fitness - o1.fitness);
	}

	@Override
	public void bbox() {
	}

}
