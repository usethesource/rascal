/*
 * @(#)Animator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.javadraw;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.Animatable;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class Animator extends Thread {

	private DrawingView     fView;
	private Animatable      fAnimatable;

	private volatile boolean             fIsRunning;
	private static final int    DELAY = 1000 / 16;

	public Animator(Animatable animatable, DrawingView view) {
		super("Animator");
		fView = view;
		fAnimatable = animatable;
	}

	public void start() {
		super.start();
		fIsRunning = true;
	}

	public void end() {
		fIsRunning = false;
	}

	public void run() {
		while (fIsRunning) {
			long tm = System.currentTimeMillis();
			fView.freezeView();
			fAnimatable.animationStep();
			fView.checkDamage();
			fView.unfreezeView();

			// Delay for a while
			try {
				tm += DELAY;
				Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				break;
			}
		}
	}
}

