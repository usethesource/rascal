package org.rascalmpl.library.vis.graph.layered;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;
import  org.rascalmpl.library.vis.graph.layered.Direction;

import processing.core.PApplet;

/**

 * Layered Graph layout. Given a list of nodes and edges a graph layout is computed with given size.
 * 
 * We use a layered drawing method inspired by (but largely extended and tailored):
 * 
 * 		Battista, et. al Graph Drawing, Prentice Hall, 1999
 * 
 * This graph layout can be selected with the property hint("layered")
 * 
 * @author paulk
 * 
 */
public class LayeredGraph extends Figure {
	protected ArrayList<LayeredGraphNode> nodes;
	protected ArrayList<LayeredGraphEdge> edges;
	protected HashMap<String, LayeredGraphNode> registered;
	IEvaluatorContext ctx;
	
//	private static boolean debug = false;
	public LayeredGraph(FigurePApplet fpa, IPropertyManager properties, IList nodes,
			IList edges, IEvaluatorContext ctx) {
		super(fpa, properties);
		
		this.ctx = ctx;
		width = getWidthProperty();
		height = getHeightProperty();
		
		// Create the nodes
		
		this.nodes = new ArrayList<LayeredGraphNode>();
		registered = new HashMap<String,LayeredGraphNode>();
		
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			Figure fig = FigureFactory.make(fpa, c, properties, ctx);
			String name = fig.getIdProperty();

			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Id property should be defined", v, ctx.getCurrentAST(), ctx.getStackTrace());

			LayeredGraphNode node = new LayeredGraphNode(name, fig);
			this.nodes.add(node);
			register(name, node);
		}

		// Create the edges
		
		this.edges = new ArrayList<LayeredGraphEdge>();
		for (IValue v : edges) {
			IConstructor c = (IConstructor) v;
			LayeredGraphEdge e = FigureFactory.makeLayeredGraphEdge(this, fpa, c, properties, ctx);
			boolean done = false;
			
			for(LayeredGraphEdge other : this.edges){
				if(other.getFrom() == e.getFrom() && other.getTo() == e.getTo()){
					// Identical edge, jut skip it;
					System.err.println("Found identical edge");
					done = true;
					break;
				} else if(other.getFrom() == e.getTo() && other.getTo() == e.getFrom()){
					// Reverse edge, copy its arrows
					System.err.println("Found reverse edge");
					Figure toArrow = e.toArrow;
					if(toArrow != null && other.fromArrow == null)
						other.fromArrow = toArrow;
					Figure fromArrow = e.fromArrow;
					if(fromArrow != null && other.toArrow == null)
						other.toArrow = fromArrow;
					other.getFrom().addIn(e.getFrom());
					other.getTo().addOut(e.getTo());
					done = true;
					break;
				}
			}
			
			if(!done){
				this.edges.add(e);
				e.getFrom().addOut(e.getTo());
				e.getTo().addIn(e.getFrom());
			}
		}
		
//		for(LayeredGraphEdge e : this.edges){
//			System.err.println(" xx edge: " + e.getFrom().name + " -> " + e.getTo().name + " toArrow=" + e.toArrow + " fromArrow=" + e.fromArrow);
//		}
	}
	
	/**
	 * Associate name with node nd
	 * @param name
	 * @param nd
	 */
	public void register(String name, LayeredGraphNode nd){
		registered.put(name, nd);
	}

	/**
	 * Get the node associated with name
	 * @param name
	 * @return
	 */
	public LayeredGraphNode getRegistered(String name) {
		return registered.get(name);
	}
	
	/**
	 * Print msg followed by the layers in readable format
	 * @param msg
	 * @param layers
	 */
	private void print(String msg, LinkedList<LinkedList<LayeredGraphNode>> layers){
		System.err.println("---- " + msg);
		for(int i = 0; i < layers.size(); i++){
			LinkedList<LayeredGraphNode> layer = layers.get(i);
			if(i > 0) System.err.println("");
			System.err.print("Layer " + i + ": ");
			for(int j = 0; j < layer.size(); j++){
				LayeredGraphNode g = layer.get(j);
				System.err.print("(" + j + ", " + g.name + ") ");
			}
		}
		System.err.println("\n----");
	}
	
	
	private void printGraph(String txt){
		System.err.println(txt);
		for(LayeredGraphNode g : nodes){
			System.err.println("*** " +g.name);
			System.err.printf("\tin    = ");
			for(LayeredGraphNode gin : g.in){
				System.err.printf("%s ", gin.name);
			}
			System.err.println("");
			System.err.printf("\tout   = ");
			for(LayeredGraphNode gout : g.out){
				System.err.printf("%s ", gout.name);
			}
			System.err.println("");
			System.err.println("\troot  = " + (g.root  != null ? g.root.name:  "null"));
			System.err.println("\talign = " + (g.align != null ? g.align.name: "null"));
			System.err.println("\tsink  = " +  (g.sink != null ? g.sink.name:  "null"));
			System.err.println("\tsink.shift  = " +  (g.sink != null ? g.sink.shift :  -1));
			System.err.println("\tthis.X= " +  g.x);
			System.err.println("\tthis.shift  = " +  g.shift);
			
		}
	}
	
	private void initialPlacement(){
		@SuppressWarnings("unused")  // TODO: Use W as width
		int W = PApplet.round(1.5f * PApplet.sqrt(nodes.size()) + 1); 
		
		printGraph("Initial graph");
		LinkedList<LinkedList<LayeredGraphNode>> layers = assignLayers(1000); print("assignLayers", layers);
		layers = insertVirtualNodes(layers);print("insertVirtualNodes", layers);
		layers = reduceCrossings(layers);print("reduceCrossings", layers);
		horizontalPlacement2(layers);
		printGraph("Final graph");
	}

	@Override
	public void bbox() {
	 initialPlacement();

		// Now scale (back or up) to the desired width x height frame
		float minx = Float.MAX_VALUE;
		float maxx = Float.MIN_VALUE;
		float miny = Float.MAX_VALUE;
		float maxy = Float.MIN_VALUE;
		
		for(LayeredGraphNode n : nodes){
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

		for (LayeredGraphNode n : nodes) {
			n.x = n.x - minx;
			n.x *= scalex;
			n.y = n.y - miny;
			n.y *= scaley;
		}
	}

	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);

		applyProperties();		
		
		for (LayeredGraphEdge e : edges)
			e.draw(left, top);
		
		for (LayeredGraphNode n : nodes) {
			n.draw(left, top);
		}
		
	}

	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent) {
		for (LayeredGraphNode n : nodes) {
			if (n.mouseOver(mousex, mousey,mouseInParent))
				return true;
		}
		return super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}

	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e) {
		for (LayeredGraphNode n : nodes) {
			if (n.mousePressed(mousex, mousey, e))
				return true;
		}
		return super.mouseOver(mousex, mousey, false);
	}
	
	private int findSink(LinkedList<LayeredGraphNode> nlist){
		int n = 0;
		for(LayeredGraphNode g : nlist){
			if(g.isSink())
				return n;
			n++;
		}
		return -1;
	}
	
	private int findSource(LinkedList<LayeredGraphNode> nlist){
		int n = 0;
		for(LayeredGraphNode g : nlist){
			if(g.isSource())
				return n;
			n++;
		}
		return -1;
	}
	
	private int findLargestOutIn(LinkedList<LayeredGraphNode> nlist){
		int n = -1;
		int max = -100000;
		for(int i = 0; i < nlist.size(); i++){
			LayeredGraphNode g = nlist.get(i);
			int d = g.getOutInDiff();
			if(d > max){
				n = i;
				max = d;
			}
		}
		return n;
	}
	
	/**
	 * Order the nodes so that they are topologically ordered and remove cycles.
	 * Uses the Greedy-Cycle-Removal (Algorithm 9.4 in Battista et al.)
	 * 
	 */
	public void orderNodesAndRemoveCycles(){
		LinkedList<LayeredGraphNode> SL = new LinkedList<LayeredGraphNode>();
		LinkedList<LayeredGraphNode> SR = new LinkedList<LayeredGraphNode>();
		
		LinkedList<LayeredGraphNode> shadowNodes = new LinkedList<LayeredGraphNode>();

		for(LayeredGraphNode g : nodes){
			g.addShadowConnections();
			shadowNodes.add(g);
		}
		
		while(shadowNodes.size() > 0){
			do {
				int n = findSink(shadowNodes);
				if(n >= 0){
					LayeredGraphNode g = shadowNodes.remove(n);
					g.disconnect();
					SR.addFirst(g);
					System.err.printf("add sink %s\n", g.name);
				} else
						break;
			} while(true);
			do {
				int n = findSource(shadowNodes);
				if(n >= 0){
					LayeredGraphNode g = shadowNodes.remove(n);
					g.disconnect();
					SL.addLast(g);
					System.err.printf("add source %s\n", g.name);
				} else
						break;
			} while(true);
			
			if(shadowNodes.size() > 0){
				int max = findLargestOutIn(shadowNodes);
				if(max >= 0){
					LayeredGraphNode g = shadowNodes.removeFirst();
					SL.addLast(g);
					g.disconnect();
					System.err.printf("add node %s\n", g.name);
				}
			}
		}
		
		// Set the label in the original nodes.
		
		int n = 0;
		// First nodes in SL ...
		for(LayeredGraphNode g : SL)
			g.label = n++;
	
		// ... and then the nodes in SR
		for(LayeredGraphNode g : SR)
			g.label = n++;
		
		// Reverse all edges that go upwards and obtain an a-cyclical graph
		
		for(LayeredGraphEdge e : edges){
			if(e.getFrom().label >  e.getTo().label){
				System.err.println("Inverting " + e.getFrom().name + " => " + e.getTo().name);
				e.reverse();
			}
		}
		
		for(LayeredGraphNode g : nodes){
			g.delShadowConnections();
		}
	}
	
	/**
	 * Assign nodes to layers using Coffman-Graham-Layering (Alg. 9.1 in Battista et al)
	 * @param W	TODO unused
	 * @return
	 */
	private LinkedList<LinkedList<LayeredGraphNode>>assignLayers(int W){
		if(nodes.size() == 0)
			return new LinkedList<LinkedList<LayeredGraphNode>>();
		
		orderNodesAndRemoveCycles();
		
		for(LayeredGraphNode g : nodes){
			System.err.printf("Node %s, label=%d, isSource=%b, isSink=%b\n", g.name, g.label, g.isSource(), g.isSink());
		}
		
		// Label the nodes (again!) and find the roots
		
		LinkedList<LayeredGraphNode> worklist = new LinkedList<LayeredGraphNode>();
		LinkedList<LayeredGraphNode> rootlist = new LinkedList<LayeredGraphNode>();
	
		for(LayeredGraphNode g : nodes){
			g.label = -1;
			if(g.in.size() == 0)
				rootlist.addLast(g);
		}
		
		if(rootlist.size() == 0)
			rootlist.add(nodes.get(0));
		
		int label = 0;
		for(LayeredGraphNode g : rootlist){
			g.label = label++;
			worklist.addLast(g);
		}
		
		while(!worklist.isEmpty()){
			LayeredGraphNode current = worklist.remove();
			System.err.println("current = " + current.name + "label = " + current.label);
			for(LayeredGraphNode child : current.increasingSortedOut()){
				if(child.label < 0){
					child.label = label++;
					worklist.addLast(child);
				}
			}
		}
		
		// Place all the source nodes of the graph in worklist
		
		for(LayeredGraphNode g : rootlist){
			System.err.println("Node " + g.name + " isSource = " + g.isSource());
			worklist.addLast(g);
		}
		
		LinkedList<LinkedList<LayeredGraphNode>> layers = new LinkedList<LinkedList<LayeredGraphNode>>();
		
		LinkedList<LayeredGraphNode> currentLayer = new LinkedList<LayeredGraphNode>();
		
		while(!worklist.isEmpty()){
			LayeredGraphNode current = null;
			for(LayeredGraphNode g : worklist){
				if(g.AllInAssignedToLayers(layers.size())){
					current = g;
					worklist.remove(g);
					break;	
				}
			}
			if(current == null)
				current = worklist.remove();
			//System.err.println("current = " + current.name);
			if(layers.size() == 0 && currentLayer.size() == 0){
				if(current.layer < 0){
					currentLayer.addLast(current);
					current.layer = layers.size();
				}
			} else if (currentLayer.size() < W && current.AllInAssignedToLayers(layers.size() - 1)){
				//System.err.println("case 1");
				if(current.layer < 0){
					currentLayer.addLast(current);
					current.layer = layers.size();
				}
			} else {
				//System.err.println("case 2");
				if(current.layer < 0){
					layers.addLast(currentLayer);
					currentLayer = new LinkedList<LayeredGraphNode>();
					currentLayer.addLast(current);
					current.layer = layers.size();
				}
			}
			System.err.println("Assign " + current.name + " to layer " + current.layer);
			for(LayeredGraphNode child : current.increasingSortedOut()){
					if(child.layer < 0)
						worklist.addLast(child);
			}
		}	
		layers.addLast(currentLayer);
		
		return layers; 
	}
	
	/**
	 * Insert a virtual node between nodes from and to.
	 * @param layers
	 * @param from
	 * @param to
	 */
	private void insertVirtualNode(LinkedList<LinkedList<LayeredGraphNode>> layers, LayeredGraphNode from, LayeredGraphNode to){
		if(to.layer - from.layer > 1){
			// Create virtual node
			String vname =  from.name + "_" + to.name + "[" + (from.layer + 1) + "]";
			System.err.println("Creating virtual node " + vname + " between " + from.name + " and " + to.name);
			LayeredGraphNode v = new LayeredGraphNode(vname, null);
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			IString vfVname = vf.string(vname);
			nodes.add(v);
			register(vname, v);
			
			v.in.add(from);  // TODO: What happens when inverted?
			v.out.add(to);
			v.layer = from.layer + 1;
			from.out.set(from.out.indexOf(to), v);
			to.in.set(to.in.indexOf(from), v);

			LayeredGraphEdge old = null;
			for(LayeredGraphEdge e : edges){
				System.err.println("Consider edge " + e.getFrom().name + " -> " + e.getTo().name);
				if(e.getFrom() == from && e.getTo() == to){
					old = e;

					System.err.println("Removing old edge " + from.name + " -> " + to.name);
					break;
				}
			}
			if(old == null)
				throw RuntimeExceptionFactory.figureException("Internal error in insertVirtualNode", null, ctx.getCurrentAST(), ctx.getStackTrace());

			IString vfGname = vf.string(from.name);
			IString vfOname = vf.string(to.name);
			if(old.isReversed()){
				edges.add(new LayeredGraphEdge(this, fpa, properties, vfGname, vfVname, old.fromArrow, old.toArrow, ctx));
				edges.add(new LayeredGraphEdge(this, fpa, properties, vfVname, vfOname, old.fromArrow, old.toArrow,ctx));
			} else {
				edges.add(new LayeredGraphEdge(this, fpa, properties, vfGname, vfVname, old.toArrow, old.fromArrow, ctx));
				edges.add(new LayeredGraphEdge(this, fpa, properties, vfVname, vfOname, old.toArrow, old.fromArrow,ctx));
			}

			edges.remove(old);
			
			layers.get(from.layer+1).addFirst(v);
		}
	}

	/**
	 * Insert virtual nodes in all layers
	 * @param layers
	 * @return
	 */
	private LinkedList<LinkedList<LayeredGraphNode>>  insertVirtualNodes(LinkedList<LinkedList<LayeredGraphNode>> layers){
		for(LinkedList<LayeredGraphNode> layer : layers){
			for(LayeredGraphNode from : layer){
				LinkedList<LayeredGraphNode> exceptions = new LinkedList<LayeredGraphNode>();
				LinkedList<LayeredGraphNode> fromOutBelow = from.getOutBelow();
				LinkedList<LayeredGraphNode> fromInAbove = from.getInAbove();
				for(int i = 0; i < fromOutBelow.size(); i++){
					LayeredGraphNode nd =  fromOutBelow.get(i);
					if(fromInAbove.contains(nd))
						exceptions.add(nd);
					insertVirtualNode(layers, from, nd);
				}
//				for(int i = 0; i < fromInAbove.size(); i++){
//					LayeredGraphNode nd =  fromInAbove.get(i);
//					if(!exceptions.contains(nd))
//						insertVirtualNode(layers, from, fromInAbove.get(i));
//				}
			}
		}
		return layers;
	}
	
	/**
	 * Compute the number of crossings for two adjacent nodes u and v with nodes in the "previous" layer
	 * Going down:  L1
	 *              L2 (u, v)
	 *              use the input of u and v
	 * Going up:	L2 (u,v)
	 *              L1
	 *              use the outputs of u and v
	 * @param down	when true go down, otherwise go up.
	 * @param L1	top layer (when down = true)
	 * @param L2	bottom layer (when down = true)
	 * @param u		left node
	 * @param v		right node
	 * @return the number of crossings
	 */
	private int cn(boolean down, LinkedList<LayeredGraphNode> L1, LinkedList<LayeredGraphNode> L2, LayeredGraphNode u, LayeredGraphNode v){
		
		int n = 0;
		System.err.printf("cn(%s,%s,%b)\n", u.name, v.name, down);
/*	//	LinkedList<LayeredGraphNode> L = down ? L2 : L1;
		LinkedList<LayeredGraphNode> L = down ? L1 : L2;
		for(LayeredGraphNode iu : down ? u.in : u.out){
			for(LayeredGraphNode iv : down ? v.in : v.out){
				if(L.indexOf(iu) > L.indexOf(iv)){
					System.err.printf("Crossing for %s (%d) and %s (%d)\n", 
							           iu.name, L.indexOf(iu), iv.name, L.indexOf(iv));
					n++;
				}
			}
		}
*/		
		if(down){ // going down: top layer L1, bottom layer L2, u,v in L2, inputs in L1
			for(LayeredGraphNode iu : u.getInAbove()){
				for(LayeredGraphNode iv : v.getInAbove()){
					System.err.printf("iu=%s, iv=%s\n", iu.name, iv.name);
					if(L1.indexOf(iu) > L1.indexOf(iv)){
						System.err.printf("Crossing for %s (%d) and %s (%d)\n", 
								           iu.name, L1.indexOf(iu), iv.name, L1.indexOf(iv));
						n++;
					}
				}
			}
		} else { // going up: top Layer L2, bottom layer L1, u, v in L2, outputs in L1
			for(LayeredGraphNode ou : u.getOutBelow()){
				for(LayeredGraphNode ov : v.getOutBelow()){
					System.err.printf("ou=%s (index=%d), ov=%s (index=%d)\n", ou.name,  L1.indexOf(ou), ov.name, L1.indexOf(ov));
					if(L1.indexOf(ou) > L1.indexOf(ov)){
						System.err.printf("Crossing for %s (%d) and %s (%d)\n", 
								           ou.name, L1.indexOf(ou), ov.name, L1.indexOf(ov));
						n++;
					}
				}
			}
		}
		System.err.println("cn(" +  u.name + ", " + v.name + (down ? ", down" : ", up") + ") -> " + n);
		return n;
	}
	
	/**
	 * Place each node in each layer at its barycenter
	 * @param layers
	 * @return modified layers
	 */
	private LinkedList<LinkedList<LayeredGraphNode>> placeAtBaryCenters(LinkedList<LinkedList<LayeredGraphNode>> layers){
		for(int i = layers.size() - 1; i > 0; i--){
			System.err.println("Layer " + i);
			LinkedList<LayeredGraphNode> P = layers.get(i-1);
			LinkedList<LayeredGraphNode> L = layers.get(i);

			LayeredGraphNode[] A = new LayeredGraphNode[L.size()];
			for(int j = 0; j < L.size(); j++){
				LayeredGraphNode g = L.get(j);
				System.err.println("Node " + g.name);
				int sum = 0;
				LinkedList<LayeredGraphNode> gInAbove = g.getInAbove();
				LinkedList<LayeredGraphNode> gOutBelow = g.getOutBelow();
				for(int k = 0; k < gInAbove.size(); k++){
					sum += P.indexOf(gInAbove.get(k));
				}
				int degree = gInAbove.size() + gOutBelow.size();
				int median= PApplet.round(sum/degree) % L.size();
				System.err.println("median = " + median);
				for(int l = median; ; l = (l + 1) % L.size()){
					if(A[l] == null){
						A[l] = g;
						break;
					}
				}
				System.err.println("copying A to LR");
				LinkedList<LayeredGraphNode> LR = new LinkedList<LayeredGraphNode>();
				for(int l = 0; l < L.size(); l++){
					LR.add(l, A[l]);
				}
				layers.set(i, LR);
			}
		}
		return layers;
	}
	
	private int exchangeAdjacentNodes(LinkedList<LayeredGraphNode> L1, LinkedList<LayeredGraphNode> L2, boolean down){
		int prevCrossings = 100000;
		int curCrossings = prevCrossings - 1;
	
		while(curCrossings < prevCrossings){
			prevCrossings = curCrossings;
			curCrossings = 0;

			for(int j = 0; j < L2.size() - 1; j++){
				LayeredGraphNode u = L2.get(j);
				LayeredGraphNode v = L2.get(j+1);
				//System.err.println("for node u=" + u.name + " (j = " + j + ") and node v=" + v.name + " in layer " + (down ? i+1 : i-1));
				int cnbefore = cn(down, L1, L2, u, v);
				L2.set(j, v);
				L2.set(j+1, u);
				int cnafter = cn(down, L1, L2, v, u);
				//System.err.printf("i=%d, j=%d, u=%s, v=%s, cnb=%d, cna=%d\n", i, j, u.name, v.name, cnbefore, cnafter);
				if(cnbefore > cnafter){
					curCrossings += cnafter;
					System.err.println("*** Exchange " + u.name + " and " + v.name);
				} else if(cnbefore == cnafter){
					curCrossings += cnbefore;
					System.err.println("*** Exchange (equal) " + u.name + " and " + v.name);
				} else {
					curCrossings += cnbefore;
					L2.set(j, u);
					L2.set(j+1, v);
				}
			}
		}
		return prevCrossings;
	}
	
	/**
	 * Reduce the number of crossings by making top/down and bottom/up sweeps across the layers and exchanging
	 * nodes in a layer when appropriate. This is potentially extremely inefficient.
	 * @param layers of the graph
	 * @return the modified layers
	 */
	private LinkedList<LinkedList<LayeredGraphNode>> reduceCrossings(LinkedList<LinkedList<LayeredGraphNode>> layers){
		
		layers = placeAtBaryCenters(layers);
		
		print("reduceCrossings: placeAtBaryCenters done", layers);
		
        int prevCrossings[] = {1000000, 1000000};
        int curCrossings[] =  {999999, 999999};
        int grace = 10;
		for(int iter = 0; curCrossings[0] + curCrossings[1] < prevCrossings[0] + prevCrossings[1] || grace-- > 0; iter++){
			prevCrossings[iter % 2] = curCrossings[iter % 2];
			curCrossings[iter % 2] = 0;
			boolean down = iter % 2 == 0;
			System.err.println("=== iter = " + iter + ", " + (down ? "down" : "up") + " mincrossings = " + prevCrossings + " ===");
			print("At start of iteration:", layers);
			for(int i = down ? 0 : layers.size()-1; down ? (i <= layers.size()-2) : (i > 0); i += (down ? 1 : -1)){
				System.err.println("--- for layer i = " + i);
				LinkedList<LayeredGraphNode> L1 = layers.get(i);
				LinkedList<LayeredGraphNode> L2 = layers.get(down ? i+1 : i-1);
				curCrossings[iter % 2] += exchangeAdjacentNodes(L1, L2, down);
			}
		}
		System.err.println("crossings = [" + prevCrossings[0] + ", " + prevCrossings[1] + "]");
		
		// Assign layer position to each node;
		
		for(LinkedList<LayeredGraphNode> layer : layers){
			for(int i = 0; i < layer.size(); i++)
				layer.get(i).pos = i;
		}
		return layers;
	}
	
	/*
	 * Horizontal placement is based on
	 * Ulrik Brandes & Boris Kopf, Fast and Simple Horizontal Coordinate Assignment
	 * Graph Drawing 2001, LNCS 2265, pp 31-44, 2002.
	 */
	
	private void preprocess(LinkedList<LinkedList<LayeredGraphNode>> layers){
		int h = layers.size()-1;
		for(int i = 2; i <= h-2; i++){
			int k0 = 0;
			int l = 1;
			LinkedList<LayeredGraphNode> Li = layers.get(i);
			int lastLi = Li.size() - 1;
			LinkedList<LayeredGraphNode> Lip1 = layers.get(i+1);
			int lastLip1 = Lip1.size()-1;
			for(int l1 = 0; l1 <= lastLip1; l1++){
				LayeredGraphNode vl1 = Lip1.get(l1);
				if(l1 == lastLip1 || vl1.isVirtual()){
					int k1 = lastLi;
					if(vl1.isVirtual()){
						k1 = vl1.in.get(0).pos;
					}
					while (l <= l1){
						for(LayeredGraphNode vki : Lip1.get(l).getInAbove()){
							int k = vki.pos;
							if(k < k0 || k > k1){
								System.err.println("mark vki -> vl.in");
								vki.marked = true;
							}
						}
						l++;
					}
					k0 = k1;
				}
			}
		}
	}
	
	private void alignVertical(LinkedList<LinkedList<LayeredGraphNode>> layers, Direction dir){
		switch(dir){
		case TOP_LEFT: alignVerticalTL(layers); break;
		case TOP_RIGHT: alignVerticalTR(layers); break;
		case BOTTOM_LEFT: alignVerticalBL(layers); break;
		case BOTTOM_RIGHT: alignVerticalBR(layers); break;
		}
	}
	
	private void alignVerticalTL(LinkedList<LinkedList<LayeredGraphNode>> layers){
		int h = layers.size()-1;
		for(int i = 0; i <= h; i++){
			LinkedList<LayeredGraphNode> Li = layers.get(i);
			int r = -1;
			for(int k = 0; k < Li.size(); k++){
				LayeredGraphNode vk = Li.get(k);
				
				LinkedList<LayeredGraphNode> vkInAbove = vk.getInAbove();
				int d = vkInAbove.size();
				if(d > 0){
					int [] ms = { (d + 1)/2 -1, (d + 2)/2 - 1};
					
					for(int m : ms){
						if(vk.align == vk){
							LayeredGraphNode um = vkInAbove.get(m);
							if(!um.marked && 0 <= r && r < um.pos){
								um.align = vk;
								vk.root = um.root;
								vk.align = vk.root;
								r = um.pos;
								System.err.printf("%s.align = %s, %s.root = %s, %s.align = %s\n", um.name, um.align.name, vk.name, vk.root.name, vk.name, vk.root.name);
							}
						}
					}
				}
			}
		}
	}
	
	// TODO
	
	private void alignVerticalBL(LinkedList<LinkedList<LayeredGraphNode>> layers){
		int h = layers.size()-1;
		for(int i = 0; i <= h; i++){
			LinkedList<LayeredGraphNode> Li = layers.get(i);
			int r = -1;
			for(int k = 0; k < Li.size(); k++){
				LayeredGraphNode vk = Li.get(k);
				LinkedList<LayeredGraphNode> vkOutBelow = vk.getOutBelow();
				
				int d = vkOutBelow.size();
				if(d > 0){
					int [] ms = { (d + 1)/2 -1, (d + 2)/2 - 1};
					
					for(int m : ms){
						if(vk.align == vk){
							LayeredGraphNode um = vkOutBelow.get(m);
							if(!um.marked && 0 <= r && r < um.pos){
								vk.align = um;
								um.root = vk.root;
								um.align = vk.root;
								r = um.pos;
								System.err.printf("%s.align = %s, %s.root = %s, %s.align = %s\n", um.name, um.align.name, vk.name, vk.root.name, vk.name, vk.root.name);
							}
						}
					}
				}
			}
		}
	}
	
	// TODO
	private void alignVerticalBR(LinkedList<LinkedList<LayeredGraphNode>> layers){
		int h = layers.size()-1;
		for(int i = 0; i <= h; i++){
			LinkedList<LayeredGraphNode> Li = layers.get(i);
			int r = Li.size();
			for(int k = Li.size()-1; k >= 0; k--){
				LayeredGraphNode vk = Li.get(k);
				LinkedList<LayeredGraphNode> vkOutBelow = vk.getOutBelow();
				System.err.printf("alignVerticalBR: %s\n", vk.name);
				int d = vkOutBelow.size();
				if(d > 0){
					int [] ms = {  (d + 2)/2 - 1, (d + 1)/2 -1};
					
					for(int m : ms){
						if(vk.align == vk){
							LayeredGraphNode um = vkOutBelow.get(m);
							System.err.printf("um = %s, marked=%b, pos=%d, r=%d\n", um.name, um.marked, um.pos, r);
							if(!um.marked && um.pos <= r && r <  Li.size()){
								vk.align = um;
								um.root = vk.root;
								um.align = vk.root;
								r = um.pos;
								System.err.printf("%s.align = %s, %s.root = %s, %s.align = %s\n", um.name, um.align.name, vk.name, vk.root.name, vk.name, vk.root.name);
							}
						}
					}
				}
			}
		}
	}
	
	private void alignVerticalTR(LinkedList<LinkedList<LayeredGraphNode>> layers){
		int h = layers.size()-1;
		for(int i = 0; i <= h; i++){
			LinkedList<LayeredGraphNode> Li = layers.get(i);
			int r = Li.size();
			for(int k = Li.size()-1; k >= 0; k--){
				LayeredGraphNode vk = Li.get(k);
				LinkedList<LayeredGraphNode> vkInAbove = vk.getInAbove();
				System.err.printf("alignVerticalTR: %s\n", vk.name);
				int d = vkInAbove.size();
				if(d > 0){
					int [] ms = {  (d + 2)/2 - 1, (d + 1)/2 -1};
					
					for(int m : ms){
						if(vk.align == vk){
							LayeredGraphNode um = vkInAbove.get(m);
							if(!um.marked && um.pos <= r && r <  Li.size()){
								um.align = vk;
								vk.root = um.root;
								vk.align = vk.root;
								r = um.pos;
								System.err.printf("%s.align = %s, %s.root = %s, %s.align = %s\n", um.name, um.align.name, vk.name, vk.root.name, vk.name, vk.root.name);
							}
						}
					}
				}
			}
		}
	}

	
	int delta = 40;
	final int INFINITY = 1000000;
	final int WIDTH = 400;
	
	private void placeBlock(LinkedList<LinkedList<LayeredGraphNode>> layers, LayeredGraphNode v, Direction dir){
		if(dir == Direction.TOP_LEFT || dir == Direction.BOTTOM_LEFT)
			placeBlockL(layers, v, dir);
		else
			placeBlockR(layers, v, dir);
	}
	
	private void placeBlockL(LinkedList<LinkedList<LayeredGraphNode>> layers, LayeredGraphNode v, Direction dir){
		if(v.getX(dir) < 0){
			System.err.println("placeBlockL: " + v.name + " x = " + v.getX(dir));
			v.setX(dir, 0);
			LayeredGraphNode w = v;
			do {
				System.err.println("placeBlock: v = " + v.name + " w = " + w.name);
				if(w.pos > 0){
					LinkedList<LayeredGraphNode> layer = layers.get(w.layer);
					LayeredGraphNode u = layer.get(w.pos-1).root;
					placeBlockL(layers, u, dir);
					if(v.sink == v){
						v.sink = u.sink;
						System.err.println("placeBlockL: " + v.name + ".sink => " + u.sink.name);
					}
					if(v.sink != u.sink){
						u.sink.shift = min(u.sink.shift, v.getX(dir) - u.getX(dir) - delta);
						System.err.println("placeBlockL: " + u.sink.name + ".sink.shift => " + u.sink.shift );
					} else {
						v.setX(dir, max(v.getX(dir), u.getX(dir) + delta));
						System.err.println(v.name + ".x -> " + v.getX(dir));
					}
					w = w.align;
				}
			} while (w.pos > 0 && w != v);
		}
		System.err.println("placeBlockL =>  " + v.name + " x = " + v.getX(dir));
	}
	
	private void placeBlockR(LinkedList<LinkedList<LayeredGraphNode>> layers, LayeredGraphNode v, Direction dir){
		
		if(v.getX(dir) < 0){
			System.err.println("placeBlockR: " + v.name + " x = " + v.getX(dir) + ", sink = " + v.sink.name);
			v.setX(dir, WIDTH);
			LayeredGraphNode w = v;
			do {
				System.err.println("placeBlockR: v = " + v.name + " w = " + w.name);
				LinkedList<LayeredGraphNode> layer = layers.get(w.layer);
				if(w.pos < layer.size() - 1){
					
					LayeredGraphNode u = layer.get(w.pos+1).root;
					placeBlockR(layers, u, dir);
					if(v.sink == v){   // OK?
						v.sink = u.sink;
						System.err.println("placeBlockR: " + v.name + ".sink => " + u.sink.name);
					}
					if(v.sink != u.sink){
						u.sink.shift = min(u.sink.shift, u.getX(dir) - v.getX(dir) - delta);
						System.err.println("placeBlockR: " + u.sink.name + ".sink.shift => " + u.sink.shift );
					} else {
						v.setX(dir, min(v.getX(dir), u.getX(dir) - delta));
						System.err.println(v.name + ".x -> " + v.getX(dir));
					}
					w = w.align;
				}
			} while (w.pos < layers.get(w.layer).size() - 1 && w != v);
		}
		System.err.println("placeBlockR =>  " + v.name + " x = " + v.getX(dir));
	}
	
	
	private void compactHorizontal(LinkedList<LinkedList<LayeredGraphNode>> layers, Direction dir){
		for(LayeredGraphNode v : nodes){
			System.err.println("compactHorizontal1: " + v.name);
			if(v.root == v)
				placeBlock(layers, v, dir);
		}
		
		for(LayeredGraphNode v : nodes){
			System.err.println("compactHorizontal2: " + v.name);
			v.setX(dir, v.root.getX(dir));
			if(v.root == v && v.sink.shift < INFINITY)
				if(dir == Direction.TOP_LEFT || dir == Direction.BOTTOM_LEFT)
					v.setX(dir, v.getX(dir) + v.sink.shift);
				else {
					v.setX(dir, v.sink.shift - v.getX(dir));
				}
		}
	}
	
	private void assignY(LinkedList<LinkedList<LayeredGraphNode>> layers){
		float vgap = max(getVGapProperty(), 10);
		float y = 0;
		
		for(LinkedList<LayeredGraphNode> layer : layers){
			float hlayer = 0;
			for(LayeredGraphNode g : layer){
				if(!g.isVirtual()){
					g.bbox();
					hlayer = max(hlayer, g.height());
				}
			}
			for(LayeredGraphNode g : layer){
				g.layerHeight = hlayer;
				g.y = y + hlayer/2;
			}
			y += hlayer + vgap;
		}
	}
	
	private LinkedList<LinkedList<LayeredGraphNode>>horizontalPlacement2(LinkedList<LinkedList<LayeredGraphNode>> layers){
		assignY(layers);
		System.err.println("assignY done");
		preprocess(layers);
		System.err.println("preprocess done");
		
		Direction[] dirs = {Direction.TOP_LEFT, Direction.TOP_RIGHT, Direction.BOTTOM_LEFT, Direction.BOTTOM_RIGHT};
		
		boolean all = true;
		if(all){
			for(Direction dir : dirs){
				for(LayeredGraphNode g : nodes){
					g.clearHorizontal();
				}

				alignVertical(layers, dir);
				System.err.println("alignVertical done");
				printGraph("after alignVertical");
				compactHorizontal(layers, dir);
				printGraph("after compactHorizontal");
				System.err.println("compactHorizontal done");
			}
			for(LayeredGraphNode g : nodes){
				g.averageHorizontal();
			}

		} else {
						Direction dir = Direction.TOP_LEFT;
			//			Direction dir = Direction.TOP_RIGHT;
			//			Direction dir = Direction.BOTTOM_LEFT;
			//			Direction dir = Direction.BOTTOM_RIGHT;

			alignVertical(layers, dir);
			System.err.println("alignVertical done");
			printGraph("after alignVertical");
			compactHorizontal(layers, dir);
			printGraph("after compactHorizontal");
			System.err.println("compactHorizontal done");
		}
		return layers;
	}
}
