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
	protected ArrayList<LatticeGraphNode> nodes;
	protected ArrayList<LatticeGraphEdge> edges;
	private HashMap<String, LatticeGraphNode> registered;
	private final boolean debug = false;
	private LinkedList<LatticeGraphNode> nextLayer = new LinkedList<LatticeGraphNode>();
	IEvaluatorContext ctx;
	ArrayList<LatticeGraphNode>[] layers;
	final int border = 20;

	private final int E = 100;

	private final int E2 = E * E;

	private int cut;

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
			// fitness = LatticeGraph.this.Y() - LatticeGraph.this.C();
			fitness =   -LatticeGraph.this.Z();
		}
		

		Organism(int cut, Organism o1, Organism o2) {
			for (int i = 0; i < o1.x.length; i++) {
				x[i] = new float[o1.x[i].length];
				aux[i] = new float[x[i].length];
			}
			// final int d = 10;
			for (int i = 0; i < cut; i++)
				for (int j = 0; j < o1.x[i].length; j++) {
					// final float delta = rand.nextInt(d)-d/2;
					x[i][j] = o1.x[i][j];
				}
			for (int i = cut + 1; i < o2.x.length; i++)
				for (int j = 0; j < o2.x[i].length; j++) {
					x[i][j] = o2.x[i][j];
					;
				}
			int n = x[cut].length;
			final int m = n / 2;
			// final int m = n>0?rand.nextInt(n):0;
			for (int j = 0; j < m; j++) {
				// final float delta = rand.nextInt(d)-d/2;
				x[cut][j] = o1.x[cut][j];
			}
			for (int j = m; j < n; j++) {
				// final float delta = rand.nextInt(d)-d/2;
				x[cut][j] = o2.x[cut][j];
			}
			set();
			// fitness = LatticeGraph.this.Y() - LatticeGraph.this.C();
			fitness =   -LatticeGraph.this.Z();
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
		cut = layers.length / 2;
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e) {
		System.err.println("mousePressed:" + this.getClass() + " "
				+ nodes.size());
		for (LatticeGraphNode n : nodes) {
			if (n.mousePressed(mousex, mousey, e))
				return true;
		}
		System.err.println("Cut= " + cut);
		elitePopulation = nextPopulation(this.cut, elitePopulation);
		if (layers.length > 2) {
			if (this.cut < layers.length - 2)
				this.cut++;
			else
				this.cut = 2;
		}
		// cut = layers.length/2;
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
				/*
				 * float c = C(), y = Y();
				 * System.err.println("Stress: C="+c+" Y="+y+" Y-8*C="+(y-8*c));
				 */
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
						// if (k<49) System.err.println("Small"+k);
						r += (k < 100 ? -10000000 : 0);
						i++;
					}
		}
		return r / i;
	}
	
/* Number Of Crossings */
	private float Z() {
		int r = 0, s = 0;
		for (LatticeGraphEdge e1:edges)
			for (LatticeGraphEdge e2:edges)
				if (!e1.equals(e2)) {
					   boolean b = new LinSolve(e1, e2).isInside();
					   if (b) r++;
					   else s++;
				}
	return r;
	}

	private Organism[] computeFirstElitePopulation() {
		Organism[] result = new Organism[E2];
		for (int i = 0; i < E; i++) {
			Organism[] organism = new Organism[E2];
			for (int k = 0; k < E2; k++) {
				for (ArrayList<LatticeGraphNode> ns : layers) {
					int len = ns.size();
					for (int j = len - 1; j >= 0; j--) {
						int r = rand.nextInt(j + 1);
						float s = ns.get(j).x;
						ns.get(j).x = ns.get(r).x;
						ns.get(r).x = s;
					}
				}
				organism[k] = new Organism();
			}
			Arrays.sort(organism, this);
			for (int j = 0; j < E; j++) {
				result[i * E + j] = organism[j];
			}
			System.err.println("Start Fitness:" + organism[0].fitness + " i="
					+ i);
		}
		return result;
	}

	private Organism[] nextPopulation(int cut, Organism[] p) {
		Organism[] result = new Organism[E2];
		Arrays.sort(p, this);
		for (int i = 0; i < E; i++)
			for (int j = 0; j < E; j++) {
				// System.err.println("i=" + i + " j=" + j);
				result[E * i + j] = new Organism(cut, p[i], elitePopulation[j]);
			}
		result[0].set();
		System.err.println("NextPopulation Fitness:" + result[0].fitness);
		return result;
	}

	private Organism[] crossOver(int cut) {
		Organism[] organism = new Organism[E2], elite = new Organism[E];
		for (int i = 0; i < E; i++)
			for (int j = 0; j < E; j++) {
				// System.err.println("i=" + i + " j=" + j);
				organism[E * i + j] = new Organism(cut, elitePopulation[i],
						elitePopulation[j]);
			}
		Arrays.sort(organism, this);
		for (int i = 0; i < E; i++) {
			elite[i] = organism[i];
		}
		organism[0].set();
		System.err.println("Fitness:" + organism[0].fitness);
		return elite;
	}

	public int compare(Organism o1, Organism o2) {
		// The biggest first
		return Math.round(o2.fitness - o1.fitness);
	}

	@Override
	public void bbox() {
	}

}
