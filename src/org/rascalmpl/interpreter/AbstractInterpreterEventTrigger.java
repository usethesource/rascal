/*******************************************************************************
 * Copyright (c) 2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.interpreter;

import java.util.Collection;
import java.util.EventObject;

/**
 * Interpreter event trigger template that does not 
 * specify yet how an event is fired.
 */
public abstract class AbstractInterpreterEventTrigger implements IInterpreterEventTrigger {

	private Object source;

	/**
	 * Creates a new event trigger with a given source.
	 * 
	 * @param source
	 *            origin of events, see {@link EventObject#getSource()}
	 */
	public AbstractInterpreterEventTrigger(Object source) {
		this.source = source;
	}

	/**
	 * Fires a interpreter event.
	 * 
	 * @param event
	 *            interpreter event to fire
	 */
	protected abstract void fireEvent(InterpreterEvent event);

	/**
	 * Fires a creation event for this interpreter.
	 */
	public void fireCreationEvent() {
		fireEvent(new InterpreterEvent(source, InterpreterEvent.Kind.CREATE));
	}

	/**
	 * Fires a terminate event for this interpreter.
	 */
	public void fireTerminateEvent() {
		fireEvent(new InterpreterEvent(source, InterpreterEvent.Kind.TERMINATE));
	}

	/**
	 * Fires a resume for this debug element with the specified detail code.
	 * 
	 * @param detail
	 *            detail code for the resume event, such as
	 *            <code>InterpreterEvent.Detail.STEP_OVER</code>
	 */
	public void fireResumeEvent(InterpreterEvent.Detail detail) {
		fireEvent(new InterpreterEvent(source, InterpreterEvent.Kind.RESUME,
				detail));
	}

	/**
	 * Fires a resume event for this debug element with detail
	 * <code>InterpreterEvent.Detail.STEP_INTO</code>.
	 */
	public void fireResumeByStepIntoEvent() {
		fireResumeEvent(InterpreterEvent.Detail.STEP_INTO);
	}

	/**
	 * Fires a resume event for this debug element with detail
	 * <code>InterpreterEvent.Detail.STEP_OVER</code>.
	 */
	public void fireResumeByStepOverEvent() {
		fireResumeEvent(InterpreterEvent.Detail.STEP_OVER);
	}

	/**
	 * Fires a resume event for this debug element with detail
	 * <code>InterpreterEvent.Detail.CLIENT_REQUEST</code>.
	 */
	public void fireResumeByClientRequestEvent() {
		fireResumeEvent(InterpreterEvent.Detail.CLIENT_REQUEST);
	}

	/**
	 * Fires a suspend event for this debug element with the specified detail
	 * code.
	 * 
	 * @param detail
	 *            detail code for the suspend event, such as
	 *            <code>InterpreterEvent.Detail.BREAKPOINT</code>
	 */
	public void fireSuspendEvent(InterpreterEvent.Detail detail) {
		fireEvent(new InterpreterEvent(source, InterpreterEvent.Kind.SUSPEND,
				detail));
	}

	/**
	 * Fires a suspend event for this debug element with detail
	 * <code>InterpreterEvent.Detail.CLIENT_REQUEST</code>.
	 */
	public void fireSuspendByClientRequestEvent() {
		fireSuspendEvent(InterpreterEvent.Detail.CLIENT_REQUEST);
	}

	/**
	 * Fires a suspend event for this debug element with detail
	 * <code>InterpreterEvent.Detail.STEP_END</code>.
	 */
	public void fireSuspendByStepEndEvent() {
		fireSuspendEvent(InterpreterEvent.Detail.STEP_END);
	}

	/**
	 * Fires a suspend event for this debug element with detail
	 * <code>InterpreterEvent.Detail.STEP_END</code>.
	 * 
	 * @param data
	 *            information about the breakpoint's location
	 */
	public void fireSuspendByBreakpointEvent(Object data) {
		InterpreterEvent event = new InterpreterEvent(source,
				InterpreterEvent.Kind.SUSPEND,
				InterpreterEvent.Detail.BREAKPOINT);
		event.setData(data);

		fireEvent(event);
	}

	/**
	 * Fires a idle event for this interpreter. E.g. this happens when the REPL
	 * is waiting for another command input.
	 */
	public void fireIdleEvent() {
		fireEvent(new InterpreterEvent(source, InterpreterEvent.Kind.IDLE));
	}

	/**
	 * Creates an "null" event trigger that does not react upon "fire" actions.
	 * 
	 * @return new trigger instance
	 */
	public static AbstractInterpreterEventTrigger newNullEventTrigger() {
		return new NullEventTrigger();
	}

	/**
	 * Creates an "null" event trigger that does not react upon "fire" actions.
	 * 
	 * @param source
	 *            a fixed event source of the trigger, i.e. event sender
	 * @param eventListeners
	 *            collection of listeners to be notified upon "fire" actions
	 * @return new trigger instance
	 */
	public static AbstractInterpreterEventTrigger newInterpreterEventTrigger(
			Object source, Collection<IInterpreterEventListener> eventListeners) {
		return new InterpreterEventTrigger(source, eventListeners);
	}

	protected static class NullEventTrigger extends
			AbstractInterpreterEventTrigger {

		public NullEventTrigger() {
			super(new Object()); // with anonymous new source object
		}

		@Override
		protected void fireEvent(InterpreterEvent event) {
			/* empty */
		}

		@Override
		public void addInterpreterEventListener(
				IInterpreterEventListener listener) {
			/* empty */
		}

		@Override
		public void removeInterpreterEventListener(
				IInterpreterEventListener listener) {
			/* empty */
		}
	}

	protected static class InterpreterEventTrigger extends
			AbstractInterpreterEventTrigger {

		private final Collection<IInterpreterEventListener> eventListeners;

		public InterpreterEventTrigger(Object source,
				Collection<IInterpreterEventListener> eventListeners) {
			super(source);
			this.eventListeners = eventListeners;
		}

		@Override
		protected void fireEvent(InterpreterEvent event) {
			for (IInterpreterEventListener listener : eventListeners) {
				listener.handleInterpreterEvent(event);
			}
		}

		@Override
		public void addInterpreterEventListener(
				IInterpreterEventListener listener) {
			if (!eventListeners.contains(listener)) {
				eventListeners.add(listener);
			}
		}

		@Override
		public void removeInterpreterEventListener(
				IInterpreterEventListener listener) {
			eventListeners.remove(listener);
		}

	}

}
