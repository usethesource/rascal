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
		
		for(LayeredGraphEdge e : this.edges){
			System.err.println(" xx edge: " + e.getFrom().name + " -> " + e.getTo().name + " toArrow=" + e.toArrow + " fromArrow=" + e.fromArrow);
		}
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
			System.err.println(g.name + ":\n  in: ");
			for(LayeredGraphNode gin : g.in){
				System.err.printf("%s ", gin.name);
			}
			System.err.printf("\n  out: ");
			for(LayeredGraphNode gout : g.out){
				System.err.printf("%s ", gout.name);
			}
			System.err.println("");
		}
	}
	
	private void initialPlacement(){
		@SuppressWarnings("unused")  // TODO: Use W as width
		int W = PApplet.round(1.5f * PApplet.sqrt(nodes.size()) + 1); 
		
		printGraph("Initial graph");
		LinkedList<LinkedList<LayeredGraphNode>> layers = assignLayers(1000); print("assignLayers", layers);
		layers = insertVirtualNodes(layers);print("insertVirtualNodes", layers);
		layers = reduceCrossings(layers);print("reduceCrossings", layers);
		horizontalPlacement(layers);
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
			if (n.mousePressed(mousex, mousey))
				return true;
		}
		return super.mouseOver(mousex, mousey, false);
	}
	
	/**
	 * Assign nodes to layers using topological sorting
	 * @param W	TODO unused
	 * @return
	 */
	private LinkedList<LinkedList<LayeredGraphNode>>assignLayers(int W){
		if(nodes.size() == 0)
			return new LinkedList<LinkedList<LayeredGraphNode>>();
		
		// Compute Topological ordering
		LinkedList<LayeredGraphNode> worklist = new LinkedList<LayeredGraphNode>();
		LinkedList<LayeredGraphNode> rootlist = new LinkedList<LayeredGraphNode>();
	
		int label = 0;
		for(LayeredGraphNode g : nodes){
			g.label = -1;
			if(g.in.size() == 0)
				rootlist.addLast(g);
		}
		
		if(rootlist.size() == 0)
			rootlist.add(nodes.get(0));
		
		for(LayeredGraphNode g : rootlist){
			g.label = label++;
			worklist.addLast(g);
		}
		
		while(!worklist.isEmpty()){
			LayeredGraphNode current = worklist.remove();
			System.err.println("current = " + current.name + "label = " + current.label);
			for(LayeredGraphNode child : current.sortedOut()){
				if(child.label < 0){
					child.label = label++;
					worklist.addLast(child);
				}
			}
		}
		
		// Reverse all edges that go upwards and obtain an a-cyclical graph
		
		for(LayeredGraphEdge e : edges){
			if(e.getFrom().label >  e.getTo().label){
				System.err.println("Inverting " + e.getFrom().name + " => " + e.getTo().name);
				e.reverse();
			}
		}
		
		for(LayeredGraphNode g : rootlist){
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
			for(LayeredGraphNode child : current.sortedOut()){
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
			
			v.in.add(from);
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
				for(int i = 0; i < from.out.size(); i++){
					LayeredGraphNode nd =  from.out.get(i);
					if(from.in.contains(nd))
						exceptions.add(nd);
					insertVirtualNode(layers, from, nd);
				}
				for(int i = 0; i < from.in.size(); i++){
					LayeredGraphNode nd =  from.in.get(i);
					if(!exceptions.contains(nd))
						insertVirtualNode(layers, from, from.in.get(i));
				}
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
		if(down){ // u,v in L2, inputs in L1
			for(LayeredGraphNode iu : u.in){
				for(LayeredGraphNode iv : v.in){
					//System.err.printf("iu=%s, iv=%s\n", iu.name, iv.name);
					if(L1.indexOf(iu) > L1.indexOf(iv)){
						//System.err.printf("Crossing for %s (%d) and %s (%d)\n", 
						//		           iu.name, L1.indexOf(iu), iv.name, L1.indexOf(iv));
						n++;
					}
				}
			}
		} else { // u, v in L2, outputs in L1
			for(LayeredGraphNode ou : u.out){
				for(LayeredGraphNode ov : v.out){
					System.err.printf("ou=%s (index=%d), ov=%s (index=%d)\n", ou.name,  L1.indexOf(ou), ov.name, L1.indexOf(ov));
					if(L1.indexOf(ou) > L1.indexOf(ov)){
						System.err.printf("Crossing for %s (%d) and %s (%d)\n", 
								           ou.name, L1.indexOf(ou), ov.name, L1.indexOf(ov));
						n++;
					}
				}
			}
		}
		System.err.println("cn(" + (down ? "down" : "up") + ", " + u.name + ", " + v.name + ") -> " + n);
		return n;
	}
	
	/**
	 * Reduce the number of crossings by making top/down and bottom/up sweeps across the layers and exchanging
	 * nodes in a layer when appropriate. This is potentially extremely inefficient.
	 * @param layers of the graph
	 * @return the modified layers
	 */
	private LinkedList<LinkedList<LayeredGraphNode>>  reduceCrossings(LinkedList<LinkedList<LayeredGraphNode>> layers){
		
		// Place each node in each layer at its barycenter
		
		for(int i = layers.size() - 1; i > 0; i--){
			System.err.println("Layer " + i);
			LinkedList<LayeredGraphNode> P = layers.get(i-1);
			LinkedList<LayeredGraphNode> L = layers.get(i);
			
			LayeredGraphNode[] A = new LayeredGraphNode[L.size()];
			for(int j = 0; j < L.size(); j++){
				LayeredGraphNode g = L.get(j);
				System.err.println("Node " + g.name);
				int sum = 0;
				for(int k = 0; k < g.in.size(); k++){
					sum += P.indexOf(g.in.get(k));
				}
				int degree = g.in.size() + g.out.size();
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
		
		print("First phase reduceCrossings", layers);

		// Iteratively exchange in each layer nodes until no more crossings can be removed.
		
        int mincrossings = 1000000;
        int crossings = mincrossings - 1;
		for(int iter = 0; crossings < mincrossings; iter++){
			mincrossings = crossings;
			crossings = 0;
			boolean down = iter % 2 == 0;
			System.err.println("=== iter = " + iter + ", " + (down ? "down" : "up") + " mincrossings = " + mincrossings + " ===");
			
			for(int i = down ? 0 : layers.size()-1; down ? (i <= layers.size()-2) : (i > 0); i += (down ? 1 : -1)){
				System.err.println("--- for layer i = " + i);
				LinkedList<LayeredGraphNode> L1 = layers.get(i);
				LinkedList<LayeredGraphNode> L2 = layers.get(down ? i+1 : i-1);
				boolean reducing = true;
				int ncross = 0;
				while(reducing){
					System.err.println("while reducing layer " + i + (down ? " down" : " up"));
					reducing = false;
					for(int j = 0; j <= L2.size() - 2; j++){
						System.err.println("for j = " + j);
						LayeredGraphNode u = L2.get(j);
						LayeredGraphNode v = L2.get(j+1);
						int cnbefore = cn(down, L1, L2, u, v);
						L2.set(j, v);
						L2.set(j+1, u);
						int cnafter = cn(down, L1, L2, v, u);
						System.err.printf("i=%d, j=%d, u=%s, v=%s, cnb=%d, cna=%d\n", i, j, u.name, v.name, cnbefore, cnafter);
						if(cnbefore > cnafter){
							ncross += cnafter;
							reducing = true;
							System.err.println("Exchange " + u.name + " and " + v.name);
						//} else if(cnbefore == cnafter && cnbefore > 0){
						//	ncross += cnbefore;
						} else {
							ncross += cnbefore;
							L2.set(j, u);
							L2.set(j+1, v);
						}
					}
				}
				crossings += ncross;
			}
		}
		System.err.println("mincrossings = " + mincrossings);
		return layers;
	}
	
	/**
	 * Place the nodes in each layer
	 * @param layers list of layers
	 * @return the layers with position information added to each node.
	 */
	private LinkedList<LinkedList<LayeredGraphNode>>horizontalPlacement(LinkedList<LinkedList<LayeredGraphNode>> layers){
		int n = layers.size();
		float wlayer[] = new float[n];
		float hlayer[] = new float[n];
		float maxWidth = 0;
		
		float hgap = max(getHGapProperty(), 10);
		float vgap = max(getVGapProperty(), 10);
		
		// Pass 1: collect the size of the nodes in each layer
		
		int l = 0;
		for(LinkedList<LayeredGraphNode> layer : layers){
			wlayer[l] = hlayer[l] = 0;
			for(LayeredGraphNode g : layer){
				if(!g.isVirtual()){
					g.bbox();
					wlayer[l] += g.width() + hgap;
					hlayer[l] = max(hlayer[l], g.height());
				}
			}
			for(LayeredGraphNode g : layer){
				g.layerHeight = hlayer[l];
			}
			hlayer[l] += vgap;
			maxWidth = max(maxWidth, wlayer[l]);
			l++;
		}
		
		// Pass 2: Actual horizontal placement
		
		l = 0;
		float y = 0;
		for(LinkedList<LayeredGraphNode> layer : layers){
			
			int nVirtual = layer.size() - countRealNodes(layer);			
			
			float deltax = (maxWidth - wlayer[l])/(layer.size() - nVirtual);
			float x = max(hgap, deltax);
			
			// Place all real nodes
			for(int i = 0; i < layer.size(); i++){
				LayeredGraphNode g = layer.get(i);
				if(!g.isVirtual()){
					if(placeTwoNodesBelowCommonParent(layers, l, i, x)){
						LayeredGraphNode gright = layer.get(i+1);
						x = gright.x + gright.width() + hgap;
						gright.y = y + hlayer[l]/2;
						i++;
					} else if(l > 0 && placeNodeBetweenTwoParents(layers.get(l-1), g)){
						x = g.x + g.width() + hgap;
					} else if(placeNodeBelowParent(layers, l, i)){
							x = g.x + g.width() + hgap;
					} else {  // No heuristic applies, do standard placement
						
						g.x = x + g.width()/2;
						x += g.width() + deltax;
						placeVirtualNodeAboveChild(layers, l, g);
						System.err.println(g.name + ": Real node, standard placement at " + g.x + ", " + g.y);
					}
					g.y = y + hlayer[l]/2;
				}
			}
			// Place all virtual nodes
			//x = deltax;
			int i = 0;
			int prevNonVirtual = -1;
			while(i < layer.size()){
				LayeredGraphNode g = layer.get(i);
				if(!g.isVirtual()){
					//x = g.x + g.width()/2 + deltax;
					prevNonVirtual = i;
					i++;
				} else {
					if(i == layer.size() - 1 || !layer.get(i+1).isVirtual()){
						// A single virtual node
						if(placeVirtualNodeBelowParent(layers, l, i)){
							//x = g.x + hgap/2;
						} else {
							if(prevNonVirtual >= 0){
								System.err.println(g.name + ": Virtual node, right of predecessor");
								LayeredGraphNode left = layer.get(prevNonVirtual);
								System.err.println("left = " + left.name + ", x = " + left.x + " width = " + left.width());
								g.x = left.x + hgap/5; // TODO left.width()/2
							} else{
								System.err.println(g.name + ": Virtual node, standard placement");
								LayeredGraphNode right = layer.get(i+1); // a real node
								if(right.x > 0)
									//g.x = (i == 0) ? 0 : right.x/2;
									g.x = right.x - hgap/5;
								else
									//g.x =  right.x + right.width()/2 + hgap/4;
									g.x = deltax;
							}
							//x +=  hgap/4;
						}
						g.y = y + hlayer[l]/2;
						i++;
					} else {
						// Consecutive virtual nodes

						int firstVirtual = i;
						
						while(i < layer.size() &&  layer.get(i).isVirtual() && placeVirtualNodeBelowParent(layers, l, i)){
							firstVirtual = i;
							i++;
						}
						int lastVirtual = firstVirtual;
						while(i < layer.size() && layer.get(i).isVirtual()){
							lastVirtual = i;
							i++;
						}
						
						if(lastVirtual > firstVirtual){
						
							float startx;
							if(prevNonVirtual >= 0){
								LayeredGraphNode left = layer.get(prevNonVirtual);
								startx = left.x + left.width()/2;
							} else {
								LayeredGraphNode right = layer.get(lastVirtual+1);
								startx =  right.x - right.width()/2 - hgap;
							}
							float endx;
							if(lastVirtual < layer.size()-1){
								LayeredGraphNode right = layer.get(lastVirtual+1);
								endx = right.x - right.width()/2;
							} else {
								LayeredGraphNode left = layer.get(prevNonVirtual);
								endx = left.x + left.width()/2 + hgap;
							}

							float dx = max(hgap, (endx - startx)/(1 + lastVirtual - firstVirtual));

							System.err.printf("Consecutive virtual nodes: startx=%f, endx=%f, dx=%f\n", startx, endx, dx);
							for(int j = firstVirtual; j <= lastVirtual; j++){
								g = layer.get(j);
								g.x = startx + (j - firstVirtual) *dx;
								System.err.printf("%s.x = %f\n", g.name, g.x);
								g.y = y + hlayer[l]/2;
							}
						}
					}
				}
			}
			y += hlayer[l];
			l++;
		}
		return layers;
	}
	
	private int countRealNodes(LinkedList<LayeredGraphNode> inout){
		int n = 0;
		for( LayeredGraphNode g : inout){
			if(!g.isVirtual())
				n++;
		}
		return n;
	}
	
	// Various horizontal placement heuristics
	
	/**
	 * Place currentNode vertically below its parent
	 * @param layer
	 * @param current
	 * @return true if placement succeeded
	 */
	public boolean placeNodeBelowParent(LinkedList<LinkedList<LayeredGraphNode>> layers, int current, int currentNode){
	
		if(current == 0)
			return false;
		LinkedList<LayeredGraphNode> layer = layers.get(current);
		LinkedList<LayeredGraphNode> parentLayer = layers. get(current-1);
		
		LayeredGraphNode g = layer.get(currentNode);
		
		int n = parentLayer.size() - 1;
		for(int i = 0; i <= n; i++){
			LayeredGraphNode gin = parentLayer.get(i);
			if(gin.isVirtual())
				continue;
			if(gin.out.contains(g) && (currentNode == 0 || (gin.x > layer.get(currentNode-1).x && countRealNodes(gin.out) == 1))){
				g.x = gin.x;
				System.err.println(g.name + ": placeNodeBelowParent applied at " + g.x);
				System.err.println("Parent " + gin.name + " at " + gin.x);
				return true;
			}
		}
		
		for(int i = 0; i <= n; i++){
			LayeredGraphNode gin = parentLayer.get(i);
			if(!gin.isVirtual())
				continue;
			if(gin.out.contains(g) && (currentNode == 0 || (gin.x >= layer.get(currentNode-1).x && countRealNodes(gin.out) == 1))){
				g.x = gin.x;
				System.err.println(g.name + ": placeNodeBelowParent applied at " + g.x);
				System.err.println("Parent " + gin.name + " at " + gin.x);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Place virtual node currentNode vertically below its parent
	 * @param layer
	 * @param currentNode
	 * @return true if placement succeeded
	 */
	public boolean placeVirtualNodeBelowParent(LinkedList<LinkedList<LayeredGraphNode>> layers, int current, int currentNode){
		if(current == 0)
			return false;
		LinkedList<LayeredGraphNode> layer = layers.get(current);
		LinkedList<LayeredGraphNode> parentLayer = layers.get(current-1);
		LayeredGraphNode g = layer.get(currentNode);

		int n = parentLayer.size() - 1;
		int nCurrent = layer.size()-1;
		for(int i = 0; i <= n; i++){
			LayeredGraphNode gin = parentLayer.get(i);
			if(gin.out.contains(g))
				if(( currentNode == 0 && (gin.x < layer.get(currentNode+1).x)) ||
				   ( currentNode == nCurrent && nCurrent > 0 && (layer.get(nCurrent-1).x < gin.x)) ||
				   ( currentNode > 0 && currentNode < nCurrent && (layer.get(currentNode-1).x < gin.x) && (gin.x < layer.get(currentNode+1).x))){
					g.x = gin.x;
					System.err.println(g.name + ": placeVirtualNodeBelowParent applied");
					return true;
				} 
		}
		return false;
	}
	
	/**
	 * Place a virtual ancestor nodes of currentNode vertically above its (non-virtual) child
	 * @param layer
	 * @param currentNode
	 * @return true if placement succeeded
	 */
	public boolean placeVirtualNodeAboveChild(LinkedList<LinkedList<LayeredGraphNode>> layers, int current, LayeredGraphNode g){
		if(current == 0)
			return false;
		LinkedList<LayeredGraphNode> parentLayer = layers. get(current-1);
		
		int n = parentLayer.size() - 1;
		for(int i = 0; i <= n; i++){
			LayeredGraphNode gin = parentLayer.get(i);
			if(gin.isVirtual() && g.in.contains(gin)){
				if((i == 0 && (parentLayer.get(i+1).x > g.x)) ||
				   (i == n && (parentLayer.get(i-1).x < g.x)) ||
				   (i > 0 && i < n && (parentLayer.get(i-1).x < g.x) &&  (parentLayer.get(i+1).x > g.x)))
				{
					gin.x = g.x;
					placeVirtualNodeAboveChild(layers, current - 1, gin);
					System.err.println(g.name + ": placeVirtualNodeAboveChild applied");
					return true;
				} 
			}
		}
		return false;
	}
	
	/**
	 * Place node g in the middle between its two parents in the previous layer
	 * @param previousLayer
	 * @param g
	 * @return true if placement succeeded
	 */
	public boolean placeNodeBetweenTwoParents(LinkedList<LayeredGraphNode> previousLayer, LayeredGraphNode g){
        for(int i = 0; i < previousLayer.size()-1; i++){
        	LayeredGraphNode p1 = previousLayer.get(i);
        	if(p1.isVirtual())
        			continue;
        	LayeredGraphNode p2 = previousLayer.get(i+1);
        	if(p2.isVirtual())
        		continue;
        	if(g.in.contains(p1) && g.in.contains(p2)){
        		g.x = p1.x + (p2.x - p1.x)/2;
        		System.err.println(g.name + ": placeNodeBetweenTwoParents applied");
        		return true;
        	}
        }
		return false;
	}
	
	/**
	 * Place currentNode and its right neighbour node symmetrically below their common parent
	 * @param layer
	 * @param currentNode
	 * @param x
	 * @return true if placement succeeded
	 */
	public boolean placeTwoNodesBelowCommonParent(LinkedList<LinkedList<LayeredGraphNode>> layers, int current, int currentNode, float x){
		
		if(current == 0)
			return false;
		
		LinkedList<LayeredGraphNode> layer =  layers.get(current);
		int i = currentNode;
		LayeredGraphNode g = layer.get(i);
		
		if(i == layer.size() - 1)
			return false;
		LayeredGraphNode gright = layer.get(i + 1);
		if(gright.isVirtual())
			return false;
		
		for(LayeredGraphNode gin : g.in){
			if(gright.in.contains(gin)){
				if(i == 0 || (gin.x >= layer.get(i-1).x)){
					g.x = gin.x - 50; // TODO: change constants
					gright.x = gin.x + 50;
					placeVirtualNodeAboveChild(layers, current, g);
					placeVirtualNodeAboveChild(layers, current, gright);
					System.err.println(g.name + ": placeTwoNodesBelowCommonParent applied");
					return true;
				} 
			}
		}
		return false;
	}
}
