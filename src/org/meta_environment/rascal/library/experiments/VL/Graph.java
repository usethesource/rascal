package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

import processing.core.PApplet;

/**
 * Graph layout. Given a list of nodes and edges a graph layout is computed with given size.
 * We use a spring layout approach as described in 
 * 
 * 		Fruchterman, T. M. J., & Reingold, E. M. (1991). 
 * 		Graph Drawing by Force-Directed Placement. 
 * 		Software: Practice and Experience, 21(11).
 * 
 * @author paulk
 *
 */
public class Graph extends VELEM {
	protected ArrayList<GraphNode> nodes;
	protected ArrayList<GraphEdge> edges;
	protected float springConstant;
	protected float springConstant2;
	protected int temperature;
	private static boolean debug = false;

	Graph(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		this.nodes = new ArrayList<GraphNode>();
		width = getWidthProperty();
		height = getHeightProperty();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			GraphNode node = new GraphNode(this, name, ve);
			this.nodes.add(node);
			vlp.register(name, node);
		}
	
		this.edges = new ArrayList<GraphEdge>();
		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			GraphEdge e = VELEMFactory.makeGraphEdge(this, vlp, c, properties, ctx);
			this.edges.add(e);
			e.from.addOut(e.to);
			e.to.addIn(e.from);
		}
		
		//float connectivity = edges.length()/nodes.length();
		springConstant = // (connectivity > 1 ? 0.5f : 0.3f) * 
		                 PApplet.sqrt((width * height)/nodes.length());
		if(debug)System.err.printf("springConstant = %f\n", springConstant);
		springConstant2 = springConstant * springConstant;
		initialPlacement();
	}
	
	
	private void initialPlacement(){
		GraphNode root = null;
//		for(GraphNode n : nodes){
//			if(n.in.isEmpty()){
//				root = n;
//				break;
//			}
//		}
//		if(root != null){
//			root.x = width/2;
//			root.y = height/2;
//		}
		for(GraphNode n : nodes){
			if(n != root){
				n.x = vlp.random(width);
				n.y = vlp.random(height);
			}
		}
	}
	
	protected float attract(float d){
		return (d * d) / springConstant;
	}
	
	protected float repel(float d){
		return springConstant2 / d;
	}
	
	@Override
	void bbox() {
	
		temperature = 50;
		for(int iter = 0; iter < 150; iter++){
		for(GraphNode n : nodes)
			n.velem.bbox();
		
			for(GraphNode n : nodes)
				n.relax();
			for(GraphEdge e : edges)
				e.relax(this);
			for(GraphNode n : nodes)
				n.update(this);
			if(iter % 4 == 0 && temperature > 0)
				temperature--;
		}
		
		// Now scale (back or up) to the desired width x height frame
		float minx = Float.MAX_VALUE;
		float maxx = Float.MIN_VALUE;
		float miny = Float.MAX_VALUE;
		float maxy = Float.MIN_VALUE;
		
		for(GraphNode n : nodes){
			float w2 = n.velem.width/2;
			float h2 = n.velem.height/2;
			if(n.x - w2 < minx)
				minx = n.x - w2;
			if(n.x + w2 > maxx)
				maxx = n.x + w2;
			
			if(n.y - h2 < miny)
				miny = n.y - h2;
			if(n.y + h2 > maxy)
				maxy = n.y + h2;
		}
		
		float scalex = width / (maxx - minx);
		float scaley = height / (maxy - miny);
		
		for(GraphNode n : nodes){
			n.x = n.x - minx; n.x *= scalex;
			n.y = n.y - miny; n.y *= scaley;
		}
	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		for(GraphEdge e : edges)
			e.draw();
		for(GraphNode n : nodes){
			n.draw();
		}
	}
}

//    /* 
//     * The indexing scheme for quadtree child nodes goes row by row.
//     *   0 | 1    0 -> top left,    1 -> top right
//     *  -------
//     *   2 | 3    2 -> bottom left, 3 -> bottom right
//     */
//
//    
//    public static final float DEFAULT_GRAV_CONSTANT = -1.0f;
//    public static final float DEFAULT_MIN_GRAV_CONSTANT = -10f;
//    public static final float DEFAULT_MAX_GRAV_CONSTANT = 10f;
//    
//    public static final float DEFAULT_DISTANCE = -1f;
//    public static final float DEFAULT_MIN_DISTANCE = -1f;
//    public static final float DEFAULT_MAX_DISTANCE = 500f;
//    
//    public static final float DEFAULT_THETA = 0.9f;
//    public static final float DEFAULT_MIN_THETA = 0.0f;
//    public static final float DEFAULT_MAX_THETA = 1.0f;
//    
//    public static final int GRAVITATIONAL_CONST = 0;
//    public static final int MIN_DISTANCE = 1;
//    public static final int BARNES_HUT_THETA = 2;
//    
//    private float xMin, xMax, yMin, yMax;
//    private QuadTreeNodeFactory factory = new QuadTreeNodeFactory();
//    private QuadTreeNode root;
//    
//    private Random rand = new Random(12345678L); // deterministic randomness
//
//    
//    /**
//     * Create a new NBodyForce.
//     * @param gravConstant the gravitational constant to use. Nodes will
//     * attract each other if this value is positive, and will repel each
//     * other if it is negative.
//     * @param minDistance the distance within which two particles will
//     * interact. If -1, the value is treated as infinite.
//     * @param theta the Barnes-Hut parameter theta, which controls when
//     * an aggregated mass is used rather than drilling down to individual
//     * item mass values.
//     */
//
//    /**
//     * Set the bounds of the region for which to compute the n-body simulation
//     * @param xMin the minimum x-coordinate
//     * @param yMin the minimum y-coordinate
//     * @param xMax the maximum x-coordinate
//     * @param yMax the maximum y-coordinate
//     */
//    private void setBounds(float xMin, float yMin, float xMax, float yMax) {
//        this.xMin = xMin;
//        this.yMin = yMin;
//        this.xMax = xMax;
//        this.yMax = yMax;
//    }
//
//    /**
//     * Clears the quadtree of all entries.
//     */
//    public void clear() {
//        clearHelper(root);
//        root = factory.getQuadTreeNode();
//    }
//    
//    private void clearHelper(QuadTreeNode n) {
//        for ( int i = 0; i < n.children.length; i++ ) {
//            if ( n.children[i] != null )
//                clearHelper(n.children[i]);
//        }
//        factory.reclaim(n);
//    }
//
//    /**
//     * Initialize the simulation with the provided enclosing simulation. After
//     * this call has been made, the simulation can be queried for the 
//     * n-body force acting on a given item.
//     */
//    public void init() {
//        clear(); // clear internal state
//        
//        // compute and squarify bounds of quadtree
//        float x1 = Float.MAX_VALUE, y1 = Float.MAX_VALUE;
//        float x2 = Float.MIN_VALUE, y2 = Float.MIN_VALUE;
// 
//        for(GraphNode item : nodes){
//      
//            float x = item.x;
//            float y = item.y;
//            if ( x < x1 ) x1 = x;
//            if ( y < y1 ) y1 = y;
//            if ( x > x2 ) x2 = x;
//            if ( y > y2 ) y2 = y;
//        }
//        float dx = x2-x1, dy = y2-y1;
//        if ( dx > dy ) { y2 = y1 + dx; } else { x2 = x1 + dy; }
//        setBounds(x1,y1,x2,y2);
//        
//        // insert items into quadtree
// 
//        for(GraphNode item : nodes){
//            insert(item);
//        }
//        
//        // calculate magnitudes and centers of mass
//        calcMass(root);
//    }
//
//    /**
//     * Inserts an item into the quadtree.
//     * @param item the GraphNode to add.
//     * @throws IllegalStateException if the current location of the item is
//     *  outside the bounds of the quadtree
//     */
//    public void insert(GraphNode item) {
//        // insert item into the quadtrees
//        try {
//            insert(item, root, xMin, yMin, xMax, yMax);
//        } catch ( StackOverflowError e ) {
//            // TODO: safe to remove?
//            e.printStackTrace();
//        }
//    }
//
//    private void insert(GraphNode p, QuadTreeNode n, 
//                        float x1, float y1, float x2, float y2)
//    {
//        // try to insert particle p at node n in the quadtree
//        // by construction, each leaf will contain either 1 or 0 particles
//        if ( n.hasChildren ) {
//            // n contains more than 1 particle
//            insertHelper(p,n,x1,y1,x2,y2);
//        } else if ( n.value != null ) {
//            // n contains 1 particle
//            if ( isSameLocation(n.value, p) ) {
//                insertHelper(p,n,x1,y1,x2,y2);
//            } else {
//                GraphNode v = n.value; n.value = null;
//                insertHelper(v,n,x1,y1,x2,y2);
//                insertHelper(p,n,x1,y1,x2,y2);
//            }
//        } else { 
//            // n is empty, so is a leaf
//            n.value = p;
//        }
//    }
//    
//    private static boolean isSameLocation(GraphNode f1, GraphNode f2) {
//        float dx = Math.abs(f1.x-f2.x);
//        float dy = Math.abs(f1.y-f2.y);
//        return ( dx < 0.01 && dy < 0.01 );
//    }
//    
//    private void insertHelper(GraphNode p, QuadTreeNode n, 
//                              float x1, float y1, float x2, float y2)
//    {   
//        float x = p.x, y = p.y;
//        float splitx = (x1+x2)/2;
//        float splity = (y1+y2)/2;
//        int i = (x>=splitx ? 1 : 0) + (y>=splity ? 2 : 0);
//        // create new child node, if necessary
//        if ( n.children[i] == null ) {
//            n.children[i] = factory.getQuadTreeNode();
//            n.hasChildren = true;
//        }
//        // update bounds
//        if ( i==1 || i==3 ) x1 = splitx; else x2 = splitx;
//        if ( i > 1 )        y1 = splity; else y2 = splity;
//        // recurse 
//        insert(p,n.children[i],x1,y1,x2,y2);        
//    }
//
//    private void calcMass(QuadTreeNode n) {
//        float xcom = 0, ycom = 0;
//        n.mass = 0;
//        if ( n.hasChildren ) {
//            for ( int i=0; i < n.children.length; i++ ) {
//                if ( n.children[i] != null ) {
//                    calcMass(n.children[i]);
//                    n.mass += n.children[i].mass;
//                    xcom += n.children[i].mass * n.children[i].com[0];
//                    ycom += n.children[i].mass * n.children[i].com[1];
//                }
//            }
//        }
//        if ( n.value != null ) {
//            n.mass += n.value.getMass();
//            xcom += n.value.getMass() * n.value.x;
//            ycom += n.value.getMass() * n.value.y;
//        }
//        n.com[0] = xcom / n.mass;
//        n.com[1] = ycom / n.mass;
//    }
//
//    /**
//     * Calculates the force vector acting on the given item.
//     * @param item the GraphNode for which to compute the force
//     */
//    public void getForce(GraphNode item) {
//        try {
//            forceHelper(item,root,xMin,yMin,xMax,yMax);
//        } catch ( StackOverflowError e ) {
//            // TODO: safe to remove?
//            e.printStackTrace();
//        }
//    }
//    
//    private void forceHelper(GraphNode item, QuadTreeNode n, 
//                             float x1, float y1, float x2, float y2)
//    {
//        float dx = n.com[0] - item.x;
//        float dy = n.com[1] - item.y;
//        float r  = (float)Math.sqrt(dx*dx+dy*dy);
//        boolean same = false;
//        if ( r == 0.0f ) {
//            // if items are in the exact same place, add some noise
//            dx = (rand.nextFloat()-0.5f) / 50.0f;
//            dy = (rand.nextFloat()-0.5f) / 50.0f;
//            r  = (float)Math.sqrt(dx*dx+dy*dy);
//            same = true;
//        }
//        boolean minDist = DEFAULT_MIN_DISTANCE>0f && r>DEFAULT_MIN_DISTANCE;
//        
//        // the Barnes-Hut approximation criteria is if the ratio of the
//        // size of the quadtree box to the distance between the point and
//        // the box's center of mass is beneath some threshold theta.
//        if ( (!n.hasChildren && n.value != item) || 
//             (!same && (x2-x1)/r < DEFAULT_THETA) ) 
//        {
//            if ( minDist ) return;
//            // either only 1 particle or we meet criteria
//            // for Barnes-Hut approximation, so calc force
//            float v = DEFAULT_GRAV_CONSTANT*item.getMass()*n.mass 
//                        / (r*r*r);
//            item.force[0] += v*dx;
//            item.force[1] += v*dy;
//        } else if ( n.hasChildren ) {
//            // recurse for more accurate calculation
//            float splitx = (x1+x2)/2;
//            float splity = (y1+y2)/2;
//            for ( int i=0; i<n.children.length; i++ ) {
//                if ( n.children[i] != null ) {
//                    forceHelper(item, n.children[i],
//                        (i==1||i==3?splitx:x1), (i>1?splity:y1),
//                        (i==1||i==3?x2:splitx), (i>1?y2:splity));
//                }
//            }
//            if ( minDist ) return;
//            if ( n.value != null && n.value != item ) {
//                float v = DEFAULT_GRAV_CONSTANT*item.getMass()*n.value.getMass()
//                            / (r*r*r);
//                item.force[0] += v*dx;
//                item.force[1] += v*dy;
//            }
//        }
//    }
//
//    /**
//     * Represents a node in the quadtree.
//     */
//    public static final class QuadTreeNode {
//        public QuadTreeNode() {
//            com = new float[] {0.0f, 0.0f};
//            children = new QuadTreeNode[4];
//        } //
//        boolean hasChildren = false;
//        float mass; // total mass held by this node
//        float[] com; // center of mass of this node 
//        GraphNode value; // GraphNode in this node, null if node has children
//        QuadTreeNode[] children; // children nodes
//    } // end of inner class QuadTreeNode
//
//    /**
//     * Helper class to minimize number of object creations across multiple
//     * uses of the quadtree.
//     */
//    public static final class QuadTreeNodeFactory {
//        private int maxNodes = 50000;
//        private ArrayList<QuadTreeNode> nodes = new ArrayList<QuadTreeNode>();
//        
//        public QuadTreeNode getQuadTreeNode() {
//            if ( nodes.size() > 0 ) {
//                return nodes.remove(nodes.size()-1);
//            } else {
//                return new QuadTreeNode();
//            }
//        }
//        public void reclaim(QuadTreeNode n) {
//            n.mass = 0;
//            n.com[0] = 0.0f; n.com[1] = 0.0f;
//            n.value = null;
//            n.hasChildren = false;
//            Arrays.fill(n.children, null);          
//            if ( nodes.size() < maxNodes )
//                nodes.add(n);
//        }
//    } // end of inner class QuadTreeNodeFactory
//
//} // end of class NBodyForce


