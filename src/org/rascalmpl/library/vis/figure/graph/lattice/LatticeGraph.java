/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.lattice;




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
//public class LatticeGraph extends Figure implements
//		Comparator<LatticeGraph.Organism> {
//	private final int E = 20;
//	private final int G = 20;
//	protected ArrayList<LatticeGraphNode> nodes;
//	protected ArrayList<LatticeGraphEdge> edges;
//	private HashMap<String, LatticeGraphNode> registered;
//	private final boolean debug = false;
//	private LinkedList<LatticeGraphNode> nextLayer = new LinkedList<LatticeGraphNode>();
//	Layer[] layers;
//	final int border = 20;
//	final int lmargin = 0;
//	final TypeStore ts = new TypeStore();
//	final TypeFactory tf = TypeFactory.getInstance();
//	final Type propt, shapeCurved;
//
//	private final int E2 = E * E;
//
//	final private Random rand = new Random();
//
//	final LinSolve linSolve = new LinSolve();
//
//	// private static boolean debug = false;
//	private LatticeGraphNode topNode = null, bottomNode = null;
//	private HashMap<LatticeGraphNode, LatticeGraphNode> visit = new HashMap<LatticeGraphNode, LatticeGraphNode>();
//
//	class Organism {
//		double[][] x = new double[layers.length][],
//				aux = new double[layers.length][];
//
//		final double fitness;
//
//		final int cut;
//
//		private double fitnessDefinition() {
//			set();
//			return -LatticeGraph.this.Z();
//		}
//
//		Organism() {
//			int i = 0;
//			for (Layer ns : layers) {
//				x[i] = new double[ns.data.size()];
//				aux[i] = new double[ns.data.size()];
//				i++;
//			}
//			init();
//			mutate(20);
//			cut = -1;
//			fitness = fitnessDefinition();
//		}
//
//		private void mutate(int p) {
//			final int r = rand.nextInt(x.length);
//			mutate(r, p);
//		}
//
//		private boolean isTriggered(double x, double step) {
//			double d = Math.abs((x - lmargin) / step);
//			if (d - Math.floor(d) < 0.001)
//				return false;
//			return true;
//		}
//
//		private void mutate(int cut, int p) {
//			final int n = x[cut].length;
//			// final double step = width / (n + 1);
//			for (int i = 0; i < p; i++)
//				if (n > 0) {
//					int m = rand.nextInt(n * 4);
//					int k = m / 4;
//					double s = x[cut][k];
//					final int l = rand.nextInt(n);
//					x[cut][k] = x[cut][l];
//					x[cut][l] = s;
//					/*
//					 * if (m%4==1 || m%4==3) { if (isTriggered(x[cut][k], step))
//					 * x[cut][k]-=step/2; else x[cut][k]+=step/2; } if (m%4==2
//					 * || m%4==3) { if (isTriggered(x[cut][l], step))
//					 * x[cut][l]-=step/2; else x[cut][l]+=step/2; }
//					 */
//				}
//		}
//
//		Organism(Organism o) {
//			for (int i = 0; i < o.x.length; i++) {
//				x[i] = new double[o.x[i].length];
//				aux[i] = new double[x[i].length];
//			}
//			for (int i = 0; i < o.x.length; i++)
//				for (int j = 0; j < o.x[i].length; j++) {
//					x[i][j] = o.x[i][j];
//				}
//			this.cut = o.cut;
//			mutate(cut, 2);
//			// System.err.println("After mutate");
//			fitness = fitnessDefinition();
//		}
//
//		Organism(Organism o1, Organism o2) {
//			for (int i = 0; i < o1.x.length; i++) {
//				x[i] = new double[o1.x[i].length];
//				aux[i] = new double[x[i].length];
//			}
//			cut = o1.x.length > 3 ? 1 + rand.nextInt(o1.x.length - 2) : 0;
//			for (int i = 0; i < cut; i++)
//				for (int j = 0; j < o1.x[i].length; j++) {
//					x[i][j] = o1.x[i][j];
//				}
//			for (int i = cut; i < o2.x.length; i++)
//				for (int j = 0; j < o2.x[i].length; j++) {
//					x[i][j] = o2.x[i][j];
//				}
//			fitness = fitnessDefinition();
//		}
//
//		private void init() {
//			int i = 0;
//			for (Layer ns : layers) {
//				for (int j = 0; j < ns.data.size(); j++)
//					x[i][j] = ns.data.get(j).x;
//				i++;
//			}
//		}
//
//		void set() {
//			int i = 0;
//			for (Layer ns : layers) {
//				for (int j = 0; j < ns.data.size(); j++) {
//					aux[i][j] = ns.data.get(j).x;
//					ns.data.get(j).x = x[i][j];
//				}
//				i++;
//			}
//		}
//
//		void reset() {
//			int i = 0;
//			for (Layer ns : layers) {
//				for (int j = 0; j < ns.data.size(); j++)
//					ns.data.get(j).x = aux[i][j];
//				i++;
//			}
//		}
//
//	}
//
//	class Layer {
//		final int rank;
//		final ArrayList<LatticeGraphNode> data;
//		final double step;
//
//		Layer(ArrayList<LatticeGraphNode> data, int rank) {
//			this.data = data;
//			this.rank = rank;
//			this.step = (minSize.getHeight() - 2 * border) / layers.length;
//		}
//	}
//
//	Organism[] elitePopulation;
//
//	// Organism[][] islands;
//
//	public LatticeGraph(IFigureConstructionEnv fpa, PropertyManager properties,
//			IList nodes, IList edges) {
//		super( properties);
//		this.nodes = new ArrayList<LatticeGraphNode>();
//		propt = tf.abstractDataType(ts, "propt");
//		shapeCurved = tf.constructor(ts, propt, "shapeCurved", tf.boolType());
//		minSize.setWidth(getWidthProperty());
//		minSize.setHeight(getHeightProperty());
//		// if (debug)
//			System.err.println("LatticeGraph:"+minSize.getWidth()+" "+minSize.getHeight());
//		registered = new HashMap<String, LatticeGraphNode>();
//		for (IValue v : nodes) {
//			IConstructor c = (IConstructor) v;
//			Figure ve = FigureFactory.make(fpa, c, properties, null);
//			String name = ve.getIdProperty();
//			if (name.length() == 0)
//				throw RuntimeExceptionFactory.figureException(
//						"Id property should be defined", v,
//						fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
//			LatticeGraphNode node = new LatticeGraphNode(name, ve);
//			this.nodes.add(node);
//			register(name, node);
//		}
//
//		this.edges = new ArrayList<LatticeGraphEdge>();
//		for (IValue v : edges) {
//			IConstructor c = (IConstructor) v;
//			LatticeGraphEdge e = FigureFactory.makeLatticeGraphEdge(this, fpa,
//					c, properties);
//			this.edges.add(e);
//			e.getFrom().addOut(e.getTo());
//			e.getTo().addIn(e.getFrom());
//		}
//
//		if (!isLattice())
//			throw RuntimeExceptionFactory.figureException("Not a lattice",
//					null, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
//
//		assignRank();
//		computeReached();
//		initialPlacement();
//		elitePopulation = computeFirstElitePopulation();
//		evolution();
//		IValueFactory vf = ValueFactoryFactory.getValueFactory();
//		for (LatticeGraphNode n : this.nodes)
//			for (LatticeGraphEdge e : this.edges)
//				if (!e.getFrom().equals(n) && !e.getTo().equals(n))
//					if (linSolve.isOnEdge(e, n)) {
//						// System.err.println("BINGO");
//						PropertyManager ep = new PropertyManager(fpa,
//								properties, vf.list(vf.constructor(shapeCurved,
//										vf.bool(true))));
//						e.prop = ep;
//					}
//	}
//
//	public void register(String name, LatticeGraphNode nd) {
//		registered.put(name, nd);
//	}
//
//	public LatticeGraphNode getRegistered(String name) {
//		return registered.get(name);
//	}
//
//	private void initialPlacement() {
//		int i = 0;
//		double y = border;
//		for (Layer layer : layers) {
//			int s = layer.data.size();
//			// System.err.println("layer.size:"+s);
//			if (s > 0) {
//				double step = minSize.getWidth() / (s + 1);
//				// System.err.println("width:"+width);
//				// System.err.println("step:"+step);
//				// double x = i % 2 == 0 ? step / 2 : (width - step / 2);
//				double x = lmargin + step;
//				for (LatticeGraphNode n : layer.data) {
//					// n.x = (double) (n.x*Math.cos(phi)+n.y*Math.sin(phi));
//					n.x = x;
//					n.y = y;
//					// x += (i % 2 == 0 ? step : -step);
//					x += step;
//					// System.err.println("y:"+n.y);
//				}
//				int z = 0;
//				for (int k = 0; k < s; k++) {
//					LatticeGraphNode q = layer.data.get(k);
//					for (int j = k + 1; j < s; j++) {
//						if (q.isConnected(layer.data.get(j))) {
//							z++;
//							layer.data.get(j).y += (z * layer.step / s);
//
//						}
//					}
//				}
//				// System.err.println("Problematic:" + i + " " + z);
//				y += layer.step;
//				i++;
//			}
//		}
//	}
//
//	@Override
//	public void draw(GraphicsContext gc) {
//		applyProperties(gc);
//		for (LatticeGraphEdge e : edges)
//			e.draw(gc);
//		for (LatticeGraphNode n : nodes) {
//			n.draw(gc);
//		}
//	}
//
//	/**
//	 * Draw focus around this figure
//	 */
//	public void drawFocus(GraphicsContext gc) {
//		// System.err.println("drawFocus: " + this.left);
//		gc.stroke(FigureColorUtils.colorNames.get("red").intValue());
//		gc.strokeWeight(1);
//		gc.noFill();
//		gc.rect(getLeft(), getTop(), minSize.getWidth(), minSize.getHeight());
//	}
//
//	private void evolution() {
//		for (int i = 0; i < G; i++) {
//			Arrays.sort(elitePopulation, this);
//			elitePopulation = nextPopulation(elitePopulation);
//		}
//		Arrays.sort(elitePopulation, this);
//		System.err.println("After evolution Fitness:"
//				+ +elitePopulation[0].fitness);
//		elitePopulation[0].set();
//	}
//
//	public boolean isLattice() {
//		boolean r1 = false, r2 = false;
//		topNode = bottomNode = null;
//		for (LatticeGraphNode n : nodes) {
//			if (n.out.isEmpty()) {
//				if (!r1)
//					bottomNode = n;
//				else
//					bottomNode = null;
//				r1 = true;
//			}
//			if (n.in.isEmpty()) {
//				if (!r2)
//					topNode = n;
//				else
//					topNode = null;
//				r2 = true;
//			}
//		}
//		return topNode != null && bottomNode != null;
//	}
//
//	private int assignRank(final boolean top) {
//		LatticeGraphNode first = top ? topNode : bottomNode;
//		nextLayer.add(first);
//		visit.put(first, first);
//		do {
//			LinkedList<LatticeGraphNode> newLayer = new LinkedList<LatticeGraphNode>();
//			while (!nextLayer.isEmpty()) {
//				LatticeGraphNode node = nextLayer.remove();
//				for (LatticeGraphNode n : (top ? node.out : node.in)) {
//					LatticeGraphNode oldNode = visit.put(n, n);
//					if (oldNode == null) {
//						newLayer.add(n);
//						if (top)
//							n.rankTop = node.rankTop + 1;
//						else
//							n.rankBottom = node.rankBottom + 1;
//					} else {
//						if (top)
//							oldNode.rankTop = node.rankTop + 1;
//						else
//							oldNode.rankBottom = node.rankBottom + 1;
//					}
//				}
//			}
//			nextLayer = newLayer;
//		} while (!nextLayer.isEmpty());
//		return top ? bottomNode.rankTop : topNode.rankBottom;
//	}
//
//	@SuppressWarnings("unchecked")
//	private void assignRank() {
//		visit.clear();
//		int maxTop = assignRank(true);
//		// System.err.println("maxTop:"+maxTop);
//		visit.clear();
//		int maxBottom = assignRank(false);
//		// System.err.println("maxBottom:"+maxBottom);
//		int len = maxBottom + maxTop + 1;
//		ArrayList<LatticeGraphNode>[] layer = new ArrayList[len];
//		for (int i = 0; i < len; i++)
//			layer[i] = new ArrayList<LatticeGraphNode>();
//		for (LatticeGraphNode n : nodes) {
//			// n.rank = n.rankTop - n.rankBottom + maxBottom;
//			n.rank = (n.rankBottom - n.rankTop + maxTop);
//			layer[n.rank].add(n);
//		}
//		int k = 0;
//		for (int i = 0; i < layer.length; i++) {
//			if (i == 0 || i == layer.length - 1 || layer[i].size() > 1)
//				k++;
//		}
//		this.layers = new Layer[k];
//		k = -1;
//		for (int i = 0; i < layer.length; i++) {
//			if (i == 0 || i == layer.length - 1 || layer[i].size() > 1) {
//				k++;
//				this.layers[k] = new Layer(layer[i], k);
//			} else
//				for (LatticeGraphNode n : layer[i]) {
//					layers[k].data.add(n);
//				}
//		}
//	}
//
//	private void computeReached(LatticeGraphNode node) {
//		for (LatticeGraphNode n : node.in) {
//			LatticeGraphNode oldNode = visit.put(n, n);
//			if (oldNode == null)
//				computeReached(n);
//			node.reached.add(n);
//		}
//		for (LatticeGraphNode n : node.in)
//			for (LatticeGraphNode m : n.reached)
//				node.reached.add(m);
//	}
//
//	private void computeReached() {
//		visit.clear();
//		computeReached(bottomNode);
//	}
//
//	private boolean isLessThen(LatticeGraphNode a, LatticeGraphNode b) {
//		return b.reached.contains(a);
//	}
//
//	private double C() {
//		double r = 0;
//		int i = 0;
//		for (LatticeGraphNode n : nodes)
//			for (LatticeGraphNode m : n.reached) {
//				double t = (n.x - m.x) * (n.x - m.x) + (n.y - m.y) * (n.y - m.y);
//				r += t;
//				i++;
//			}
//		return r / i;
//	}
//
//	private double Y() {
//		double r = 0;
//		// int i = 0;
//		for (Layer ns : layers) {
//			for (LatticeGraphNode n : ns.data)
//				for (LatticeGraphNode m : ns.data)
//					if (!m.equals(n)) {
//						final double k = (n.x - m.x) * (n.x - m.x);
//						r += k;
//					}
//		}
//		return r;
//	}
//
//	/* Number Of Crossings */
//	private double Z() {
//		int r = 0, s = 0;
//		for (LatticeGraphEdge e1 : edges)
//			for (LatticeGraphEdge e2 : edges)
//				if (!e1.equals(e2)) {
//					boolean b = linSolve.isCuttingPointInside(e1, e2);
//					if (b)
//						r++;
//					else
//						s++;
//				}
//		// System.err.println("r="+r+" s="+s+" t="+(r+s));
//		return r;
//	}
//
//	private Organism[] computeFirstElitePopulation() {
//		Organism[] result = new Organism[E2];
//		for (int i = 0; i < E; i++) {
//			Organism[] organism = new Organism[E2];
//			for (int k = 0; k < E2; k++) {
//				final Organism o = new Organism();
//				organism[k] = o;
//			}
//			Arrays.sort(organism, this);
//			for (int j = 0; j < E; j++) {
//				result[i * E + j] = organism[j];
//			}
//			// System.err.println("Start Fitness:" + organism[0].fitness + " i="
//			// + i);
//		}
//		Arrays.sort(result, this);
//		result[0].set();
//		System.err.println("Initial Fitness:" + result[0].fitness);
//		return result;
//	}
//
//	private Organism[] nextPopulation(Organism[] p) {
//		Organism[] result = new Organism[E2];
//		for (int i = 0; i < E; i++)
//			for (int j = 0; j < E; j++) {
//				// System.err.println("i=" + i + " j=" + j);
//				Organism o1 = new Organism(p[i], p[j]);
//				Organism o2 = new Organism(o1);
//				result[E * i + j] = (o1.fitness < o2.fitness) ? o2 : o1;
//			}
//		return result;
//	}
//
//	public int compare(Organism o1, Organism o2) {
//		// The biggest first
//		return FigureMath.round(o2.fitness - o1.fitness);
//	}
//	
//	@Override
//	public void bbox() {
//		for(LatticeGraphNode g : nodes){
//			g.bbox();
//		}
//		setNonResizable();
//		super.bbox();
//	}
//	
//	
//	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
//		if(!mouseInside(c.getX(), c.getY())) return false;
//		boolean found = false;
//		for(int i = nodes.size()-1 ; i >= 0 ; i--){
//			if(nodes.get(i).figure != null && nodes.get(i).figure.getFiguresUnderMouse(c, result)){
//				found=true;
//				break;
//			}
//		}
//		if(!found){
//			for(int i = nodes.size()-1 ; i >= 0 ; i--){
//				if(edges.get(i).getFiguresUnderMouse(c, result)){
//					break;
//				}
//			}
//		}
//		addMeAsFigureUnderMouse(result);
//		return true;
//	}
//	
//	public void computeFiguresAndProperties(ICallbackEnv env){
//		super.computeFiguresAndProperties(env);
//		for(LatticeGraphNode node : nodes){
//			node.figure.computeFiguresAndProperties(env);
//		}
//	}
//	
//
//	public void registerNames(NameResolver resolver){
//		super.registerNames(resolver);
//		for(LatticeGraphNode node : nodes){
//			node.figure.registerNames(resolver);
//		}
//	}
//
//	@Override
//	public void layout() {
//		size.set(minSize);
//		for(LatticeGraphNode node : nodes){
//			node.layout();
//		}
//		for(LatticeGraphEdge edge : edges){
//			edge.layout();
//		}
//   }
//
//}

