/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.spring;

import java.util.LinkedList;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.util.vector.Vector2D;

/**
 * A SpringGraphNode is created for each "node" constructor that occurs in the
 * graph.
 * 
 * @author paulk
 * 
 */
public class SpringGraphNode extends Figure {

	private final SpringGraph G;
	protected final String name;
	protected final Figure figure;
	private double x;	// Coordinates of center of node
	private double y;

	double temperature;
	double skew;
	Vector2D oldImpulse;

	protected LinkedList<SpringGraphNode> in;
	protected LinkedList<SpringGraphNode> out;
	private static boolean debug = false;

	SpringGraphNode(SpringGraph springGraph, String name, Figure fig) {
		super(springGraph.prop);
		this.G = springGraph;
		this.name = name;
		this.figure = fig;
		this.children = new Figure[1];
		this.children[0] = fig;
		in = new LinkedList<SpringGraphNode>();
		out = new LinkedList<SpringGraphNode>();
	}

	public void init() {
		x = FigureMath.random(width()/2, G.minSize.getX() - width()/2);
		y = FigureMath.random(height()/2, G.minSize.getY() -height()/2);
		temperature = G.MAX_LOCAL_TEMPERATURE;
		skew = 0;
		oldImpulse = new Vector2D(0, 0);
	}

	public void addIn(SpringGraphNode n) {
		if (!in.contains(n))
			in.add(n);
	}

	public void addOut(SpringGraphNode n) {
		if (!out.contains(n))
			out.add(n);
	}

	public int degree() {
		return in.size() + out.size();
	}

	double width() {
		return figure != null ? figure.minSize.getX() : 0;
	}

	double height() {
		return figure != null ? figure.minSize.getY() : 0;
	}

	protected double getCenterX() {
		return x;
	}
	
	protected double getCenterY() {
		return y;
	}
	
	public Vector2D getCenter() {
		return new Vector2D(x, y);
	}
	
	// Move node during simulation
	
	protected void moveBy(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	// Place node after one round, update all dependent positions
	
	protected void placeCenter(double newX, double newY) {
		double w2 = width()/2;
		double h2 = height()/2;
		if (newX <= w2){
			System.err.printf("ERROR: node %s, x outside boundary: %f\n", name,	newX);
		    this.x = w2 + 20;	
		    if(oldImpulse.getX() < 0)
		    	oldImpulse.setX(-oldImpulse.getX());
		} else
		if(newX >= G.minSize.getX() - w2){
			System.err.printf("ERROR: node %s, x outside boundary: %f\n", name,	newX);
			this.x = G.minSize.getX() - w2 - 20;
			if(oldImpulse.getX() > 0)
				oldImpulse.setX(-oldImpulse.getX());
		} else
			this.x = newX; 
		
		
		if (newY <= h2){
			System.err.printf("ERROR: node %s, y outside boundary: %f\n", name,	newY);
		    this.y = h2 + 20;	
		    if(oldImpulse.getY() < 0)
		    	oldImpulse.setY(-oldImpulse.getY());
		} else
		if(newY >= G.minSize.getY() - h2){
			System.err.printf("ERROR: node %s, y outside boundary: %f\n", name,	newY);
			this.y = G.minSize.getY() - h2 - 20;	
			if(oldImpulse.getY() > 0)
				oldImpulse.setY(-oldImpulse.getY());
		} else
			this.y = newY; 
		
		setElementPosition();
	}
	
	public double distance(SpringGraphNode other){
		return getCenter().distance(other.getCenter());
	}
	
	public double distance2(SpringGraphNode other){
		return getCenter().distance2(other.getCenter());
	}
	
	// Mass of this node: surface * number of connections.
	
	public double getMass(){
		return  0.005 * width() * height() *  (1 + degree() / 2);
	}

	@Override
	public void computeMinSize() {
		minSize.set(figure.minSize.getX(), figure.minSize.getY());
	}

	@Override
	public void resizeElement(Rectangle view) {
		localLocation.set(0, 0);
		setElementPosition();
	}
	
	void setElementPosition(){
		figure.localLocation.set(
				localLocation.getX() + x - figure.minSize.getX() / 2,
				localLocation.getY() + y - figure.minSize.getY() / 2);
		
		figure.globalLocation.set(globalLocation);
		figure.globalLocation.set(figure.localLocation);
		figure.updateGlobalLocation();
		
		figure.computeMinSize();
	}

	// ---------------------

	private void print(String msg) {
		double ix = oldImpulse == null ? 0 : oldImpulse.getX();
		double iy = oldImpulse == null ? 0 : oldImpulse.getY();
		System.err.printf("%s: %s: (%3.2f,%3.2f), imp (%3.2f,%3.2f), temp %4.1f, skew %2.1f\n",
						  msg, name, x, y, ix, iy, temperature, skew);
	}

	// Perform one step of the spring simulation algorithm for this node

	public void step() {
		if(debug) print("update, before");
		Vector2D impulse = computeNodeImpulse();
		double angle = oldImpulse.angle(impulse);
		adjustTemperatureAndSkew(angle);
		double dx = G.UPDATE_STEP * impulse.getX() * temperature;
		double dy = G.UPDATE_STEP * impulse.getY() * temperature;

		moveBy(dx, dy); // adjust local position

		if(debug){
			print("relax, after ");
			System.err.printf("             imp (%3.2f, %3.2f), angle %1.2f, dx %3.2f, dy %3.2f\n",
						impulse.getX(), impulse.getY(), Math.toDegrees(angle),
						dx, dy);
		}
		oldImpulse = impulse;
	}

	/**
	 * Compute the attractive force A with another node: 
	 * A = (this - other) * distance^2 / (EDGE_LENGTH_2 * PHI) * ATTRACT
	 * 
	 */
	public Vector2D attractiveForce(SpringGraphNode other) {
		double distance2 = distance2(other);
		Vector2D thisVector = new Vector2D(x, y);
		Vector2D otherVector = new Vector2D(other.x, other.y);
		return thisVector.sub(otherVector).mul(distance2).div(G.EDGE_LENGTH_2 * getMass()) .mul(G.ATTRACT);
	}

	/**
	 * Compute repulsive force R with another node: 
	 * R = (this - other) * EDGE_LENGHT_2 / distance ^ 2
	 * 
	 */
	public Vector2D repulsiveForce(SpringGraphNode other) {
		double distance2 = distance2(other);

		if (distance2 > 0) {
			Vector2D thisVector = new Vector2D(getCenter());
			Vector2D otherVector = new Vector2D(other.getCenter());
			return thisVector.sub(otherVector).mul(G.EDGE_LENGTH_2).div(distance2).mul(G.REPEL);
		}
		return (new Vector2D(0, 0));
	}
	
	public Vector2D repulsiveForceWall(Vector2D wallVector) {
		Vector2D thisVector = new Vector2D(getCenter());
		double distance2 = thisVector.distance2(wallVector);

		if (distance2 > 0) {
			return thisVector.sub(wallVector).mul(G.EDGE_LENGTH_2).div(distance2).mul(10 * G.REPEL);
		}
		return (new Vector2D(0, 0));
	}

	private final static double Deg45 = Math.toDegrees(Math.PI/4);
	private final static double DegMin45 = Math.toDegrees(-Math.PI/4);
	private final static double Deg145 = Math.toDegrees(3*Math.PI/4);
	private final static double Deg225 = Math.toDegrees(5*Math.PI/4);
	
	/**
	 * Adjust the temperature of the node according to the old temperature and
	 * the the old impulse
	 */
	public void adjustTemperatureAndSkew(double angle) {
		// Angle is a positive value between 0 and 2 pi. 
		if (angle < Deg45 || angle > DegMin45 || (angle > Deg145 && angle < Deg225)) {
			// between -45 and 45 degrees there is acceleration, 
			// between 145 and 225 degree there is oscillation
			temperature *= (1 + G.OSCILLATION * Math.cos(angle));
		} else {
			// in the other ranges of the angle, there is rotation:
			skew += G.SKEW * Math.sin(angle);
			temperature -= G.ROTATION * Math.abs(skew);
		}
		temperature = FigureMath.constrain(temperature, 0, G.MAX_LOCAL_TEMPERATURE);
	}

	/**
	 * This method computes the normalized impulse F of the node by the sum of
	 * all force vectors. F = gravity + random + sum(n in nodes, repel(n)) -
	 * sum(n in adjacent, attract(n)) Returns norm(F)
	 */
	public Vector2D computeNodeImpulse() {

		// Add a random force and the gravitational force
		Vector2D resultForce = gravitionalForce().add(randomForce());

		// Repulsive forces
		for (SpringGraphNode otherNode : G.nodes) {
			if (otherNode != this) {
				resultForce = resultForce.add(repulsiveForce(otherNode));
			}
		}

		// Attractive forces
		for (SpringGraphNode otherNode : in) {
			resultForce = resultForce.sub(this.attractiveForce(otherNode));
		}

		for (SpringGraphNode otherNode : out) {
			resultForce = resultForce.sub(attractiveForce(otherNode));
		}
		
		// Repulsion of left and right wall
		
		resultForce = resultForce.add(repulsiveForceWall(new Vector2D(0, y)));
		resultForce = resultForce.add(repulsiveForceWall(new Vector2D(G.minSize.getX(), y)));
		
		// Repulsion of top and bottom wall
		
		resultForce = resultForce.add(repulsiveForceWall(new Vector2D(x, 0)));
		resultForce = resultForce.add(repulsiveForceWall(new Vector2D(x, G.minSize.getY())));

		// we only need the impulse
		return resultForce.normalize();
	}

	/**
	 * Compute the random force. 
	 * We need a random impulse [-RAND_CONSTANT, ..., +RAND_CONSTANT] 
	 * Range should be approx. [-1/4 ... +1/4] of desired edge length.
	 */
	public Vector2D randomForce() {
		return new Vector2D(FigureMath.random(-G.RAND_DISTURB, G.RAND_DISTURB),
			  	            FigureMath.random(-G.RAND_DISTURB, G.RAND_DISTURB));
	}

	/**
	 * This method computes the gravitational force between a node and the
	 * barycenter of the drawing. 
	 * G = (barycenter - thisVector) * Mass * Gravity
	 */
	public Vector2D gravitionalForce() {
		Vector2D barycenter = new Vector2D(G.getBaryCenter());
		Vector2D thisVector = new Vector2D(getCenter());
		return barycenter.sub(thisVector).mul(getMass()).mul(G.GRAVITY);
	}
}