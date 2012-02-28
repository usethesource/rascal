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

import org.eclipse.imp.pdb.facts.IReal;
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
	private double x;
	private double y;

	double temperature;
	double skew;

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
		init();
	}

	public void init() {
		x = FigureMath.random(100, 400);
		y = FigureMath.random(100, 400);
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

	protected void setX(double x) {
		if (x < figure.minSize.getX() / 2 - 0.000001
				|| x > G.minSize.getX() - figure.minSize.getX() / 2 + 0.000001)
			System.err.printf("ERROR: node %s, x outside boundary: %f\n", name,
					x);
		this.x = x;
	}

	protected double getX() {
		return x;
	}

	protected void setY(double y) {
		if (y < figure.minSize.getY() / 2 - 0.000001
				|| y > G.minSize.getY() - figure.minSize.getY() / 2 + 0.000001)
			System.err.printf("ERROR: node %s, y outside boundary: %f\n", name,
					y);
		this.y = y;
	}

	protected double getY() {
		return y;
	}
	
	public Vector2D getCenter() {
		return new Vector2D(x, y);
	}
	
	public double distance(SpringGraphNode other){
		return getCenter().distance(other.getCenter());
	}
	
	public double distance2(SpringGraphNode other){
		return getCenter().distance2(other.getCenter());
	}

	protected void moveBy(double dx, double dy) {
		x += dx;
		y += dy;
		// x = FigureMath.constrain(x + dx, 0, 500);
		// y = FigureMath.constrain(y + dy, 0, 500);
	}

	@Override
	public void computeMinSize() {
		minSize.set(figure.minSize.getX(), figure.minSize.getY());
	}

	@Override
	public void resizeElement(Rectangle view) {
		localLocation.set(0, 0);
		
		figure.localLocation.set(
				localLocation.getX() + x - figure.minSize.getX() / 2,
				localLocation.getY() + y - figure.minSize.getY() / 2);
		computeMinSize();
	}

	// ---------------------

	private void print(String msg) {
		double ix = oldImpulse == null ? 0 : oldImpulse.getX();
		double iy = oldImpulse == null ? 0 : oldImpulse.getY();
		System.err.printf("%s: %s: (%3.2f,%3.2f), imp (%3.2f,%3.2f), temp %4.1f, skew %2.1f\n",
						  msg, name, x, y, ix, iy, temperature, skew);
	}

	Vector2D oldImpulse = new Vector2D();

	public void update() {
		if(debug) print("update, before");
		Vector2D impulse = computeNodeImpulse();
		double angle = oldImpulse.angle(impulse);
		adjustTemperatureAndSkew(angle);
		double dx = G.UPDATE_STEP * impulse.getX() * temperature;
		double dy = G.UPDATE_STEP * impulse.getY() * temperature;

		// adjust local position
		moveBy(dx, dy);

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
		double phi = 1 + degree() / 2;
		return thisVector.sub(otherVector).mul(distance2)
				.div(G.EDGE_LENGTH_2 * phi) .mul(G.ATTRACT);
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
			return thisVector.sub(otherVector).mul(G.EDGE_LENGTH_2)
					.div(distance2).mul(G.REPEL);
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
	 * G = (barycenter - thisVector) * PHI * Gravity
	 * where PHI is the mass of the node
	 */
	public Vector2D gravitionalForce() {
		// compute the mass with the node degree
		double phi = 1 + degree() / 2;
		Vector2D barycenter = new Vector2D(G.getBaryCenter());
		Vector2D thisVector = new Vector2D(getCenter());
		return barycenter.sub(thisVector).mul(phi).mul(G.GRAVITY);
	}
}