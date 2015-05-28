/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.debug;

import static org.rascalmpl.interpreter.AbstractInterpreterEventTrigger.newNullEventTrigger;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Module;
import org.rascalmpl.interpreter.AbstractInterpreterEventTrigger;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.debug.IDebugMessage.Detail;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.uptr.ITree;

public final class DebugHandler implements IDebugHandler {

	private AbstractInterpreterEventTrigger eventTrigger;

	private final Set<String> breakpoints = new java.util.HashSet<String>();
	
	/**
	 * An evaluator heap isto query the loaded modules for proper break point locations
	 */
	private final Evaluator evaluator;
	
	/**
	 * Indicates a manual suspend request from the debugger, e.g. caused by a pause action in the GUI.
	 */
	private boolean suspendRequested;

	/**
	 * Indicates that the evaluator is suspended. Also used for suspending / blocking the evaluator.
	 */
	private boolean suspended;
	
	private enum DebugStepMode {
		NO_STEP, STEP_INTO, STEP_OVER 
	}
	
	private DebugStepMode stepMode = DebugStepMode.NO_STEP;
	
	/**
	 * Referring to {@link AbstractAST} responsible for last suspension.
	 */
	private AbstractAST referenceAST = null;	

	/**
	 * Referring to the {@link Environment} stack depth at last suspension suspension.
	 * This information is used to determine if stepping enters a function call.
	 * {@see #suspend(IEvaluator, AbstractAST)}
	 */	
	private Integer referenceEnvironmentStackSize = null;

	/**
	 * Action to execute on termination request, or <code>null</code> if none.
	 */
	private Runnable terminateAction = null;

	

	/**
	 * Create a new debug handler with its own interpreter event trigger.
	 */
	public DebugHandler(Evaluator evaluator) {
		this.evaluator = evaluator;
		setEventTrigger(newNullEventTrigger());
	}
	
	private boolean hasBreakpoint(ISourceLocation b) {
		return breakpoints.contains(b.toString());
	}
	
	private void addBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.addAll(getBreakpointsForLocation(breakpointLocation));
	}

	private Module getModuleAST(ISourceLocation breakpointLocation) {
		try {
			ITree parseModule = evaluator.parseModule(null, evaluator.getValueFactory().sourceLocation(breakpointLocation.getURI()));
			Module buildModule = new ASTBuilder().buildModule(parseModule);
			return buildModule;
		}
		catch (Throwable e) {
			return null;
		}
	}
	
	private List<String> getBreakpointsForLocation(ISourceLocation breakpointLocation) {
		Module mod = getModuleAST(breakpointLocation);
		if (mod != null) {
			return mod.breakpoints(breakpointLocation.getBeginLine()).stream().map(
					p -> p.getLocation().toString()
					).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private void removeBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.removeAll(getBreakpointsForLocation(breakpointLocation));
	}
	
	protected void clearSuspensionState() {
		setReferenceAST(null);
		setReferenceEnvironmentStackSize(null);
		setSuspended(false);
	}	
	
	protected void updateSuspensionState(IEvaluator<?> evaluator, AbstractAST currentAST) {
		setReferenceAST(currentAST);
		
		// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
		setReferenceEnvironmentStackSize(((Evaluator) evaluator).getCallStack().size());
		setSuspended(true);
	}
	
	@Override
	public void suspended(IEvaluator<?> evaluator, AbstractAST currentAST) {
		if (Display.getDefault().getThread().equals(Thread.currentThread())) {
			return;
		}
		if(isSuspendRequested()) {
			updateSuspensionState(evaluator, currentAST);
			getEventTrigger().fireSuspendByClientRequestEvent();			
		
			setSuspendRequested(false);
			
		} else {

			ISourceLocation location = currentAST.getLocation();
      switch (getStepMode()) {
			
			case STEP_INTO:
				updateSuspensionState(evaluator, currentAST);
				getEventTrigger().fireSuspendByStepEndEvent();
				break;
				
			case STEP_OVER:
				// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
				// TODO: optimize {@link Evaluator.getCallStack()}; currently it is expensive because of environment traversal
				Integer currentEnvironmentStackSize = ((Evaluator) evaluator).getCallStack().size();

				/*
				 * Stepping over implies:
				 * * either there is a next statement in the same environment stack frame (which might
				 *   equal the reference statement in case of recursion or single statement loops)
				 * * or there is no next statement in the same stack frame and thus the stack frame 
				 *   eventually gets popped from the stack. As long the calls in deeper nesting levels 
				 *   are executed, no action needs to be taken.
				 */
				switch (currentEnvironmentStackSize.compareTo(getReferenceEnvironmentStackSize())) {
				case 0:
				  /*
				   * For the case that we are still within the same stack
				   * frame, positions are compared to ensure that the
				   * statement was finished executing.
				   */
				  int referenceStart = getReferenceAST().getLocation().getOffset();
				  int referenceAfter = getReferenceAST().getLocation().getOffset() + getReferenceAST().getLocation().getLength();
				  int currentStart = location.getOffset();
				  int currentAfter = location.getOffset() + location.getLength();

				  if (currentStart < referenceStart
				      || currentStart >= referenceAfter
				      || currentStart == referenceStart
				      && currentAfter == referenceAfter) {
				    updateSuspensionState(evaluator, currentAST);
				    getEventTrigger().fireSuspendByStepEndEvent();
				  }
				  break;

				case -1:
				  // lower stack size: left scope, thus over
				  updateSuspensionState(evaluator, currentAST);
				  getEventTrigger().fireSuspendByStepEndEvent();
				  break;

				case +1:
				  // higher stack size: not over yet
				  break;

				default:
				  throw new RuntimeException(
				      "Requires compareTo() to return exactly either -1, 0, or +1.");
				}
				break;

			case NO_STEP:
			  if (hasBreakpoint(location)) {
			    updateSuspensionState(evaluator, currentAST);
			    getEventTrigger().fireSuspendByBreakpointEvent(location);
			  }
			  break;

      }
		}

		/*
		 * Waiting until GUI triggers end of suspension.
		 */
		while (isSuspended()) {
		  try {
		    evaluator.wait(50);
		  } catch (InterruptedException e) {
		    // Ignore
		  }
		}		

	}

	protected AbstractAST getReferenceAST() {
	  return referenceAST;
	}

	protected void setReferenceAST(AbstractAST referenceAST) {
	  this.referenceAST = referenceAST;
	}

	protected Integer getReferenceEnvironmentStackSize() {
	  return referenceEnvironmentStackSize;
	}

	protected void setReferenceEnvironmentStackSize(Integer referenceEnvironmentStackSize) {
	  this.referenceEnvironmentStackSize = referenceEnvironmentStackSize;
	}

	protected boolean isSuspendRequested() {
	  return suspendRequested;
	}

	protected void setSuspendRequested(boolean suspendRequested) {
	  this.suspendRequested = suspendRequested;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void processMessage(IDebugMessage message) {
	  switch (message.getSubject()) {
	  case BREAKPOINT:
	    ISourceLocation breakpointLocation = (ISourceLocation) message.getPayload();

	    switch (message.getAction()) {
	    case SET:
	      addBreakpoint(breakpointLocation);
	      break;

	    case DELETE:
	      removeBreakpoint(breakpointLocation);
	      break;
	    }
	    break;

	  case INTERPRETER:	
	    switch (message.getAction()) {
	    case SUSPEND:
	      if (message.getDetail() == Detail.CLIENT_REQUEST) {
	        setSuspendRequested(true);
	      }
	      break;

	    case RESUME:
	      setSuspended(false);

	      switch (message.getDetail()) {
	      case STEP_INTO:
	        setStepMode(DebugStepMode.STEP_INTO);
	        getEventTrigger().fireResumeByStepIntoEvent();
	        break;

	      case STEP_OVER:
	        setStepMode(DebugStepMode.STEP_OVER);
	        getEventTrigger().fireResumeByStepOverEvent();
	        break;

	      case CLIENT_REQUEST:
	        setStepMode(DebugStepMode.NO_STEP);
	        getEventTrigger().fireResumeByClientRequestEvent();
	        break;
	      }
	      break;

	    case TERMINATE:
	      if (terminateAction != null) {
	        terminateAction.run();
	      }
	      break;
	    }
	    break;
	  }
	}

	public void setTerminateAction(Runnable terminateAction) {
	  this.terminateAction = terminateAction;
	}

	protected synchronized boolean isSuspended() {
	  return suspended;
	}

	protected synchronized void setSuspended(boolean suspended) {
	  this.suspended = suspended;
	}

	protected DebugStepMode getStepMode() {
	  return stepMode;
	}

	protected void setStepMode(DebugStepMode stepMode) {
	  this.stepMode = stepMode;
	}

	public AbstractInterpreterEventTrigger getEventTrigger() {
	  return eventTrigger;
	}

	public void setEventTrigger(AbstractInterpreterEventTrigger eventTrigger) {
	  this.eventTrigger = eventTrigger;
	}	

}
