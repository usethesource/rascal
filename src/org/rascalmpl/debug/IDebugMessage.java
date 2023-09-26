/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
*******************************************************************************/
package org.rascalmpl.debug;

/**
 * Specification of messages that are exchanged between the debugger and the
 * runtime.
 */
public interface IDebugMessage {
	
	/**
	 * Verb describing the action to take after receiving a message, or
	 * <code>UNKNOWN</code> if not provided.
	 */
	enum Action {
		UNKNOWN, SET, DELETE, TERMINATE, SUSPEND, RESUME
	}

	/**
	 * Noun describing the subject of the messages, or <code>UNKNOWN</code> if
	 * not provided.
	 */
	enum Subject {
		UNKNOWN, BREAKPOINT, INTERPRETER
	}

	/**
	 * Additional information to the subject, or <code>UNKNOWN</code> if not
	 * provided.
	 */
	enum Detail {
		UNKNOWN,

		/**
		 * Indicates the debugger was suspended due to the completion of a step
		 * action.
		 */
		STEP_END,

		/**
		 * Indicates a thread was suspended by a breakpoint.
		 */
		BREAKPOINT,

		/**
		 * Indicates the debugger was suspended / resumed due to a client
		 * request.
		 */
		CLIENT_REQUEST,

		/**
		 * Indicates a continuation of the execution, caused by a step into
		 * request.
		 */
		STEP_INTO,

		/**
		 * Indicates a continuation of the execution, caused by a step over
		 * request.
		 */
		STEP_OVER,

		/**
		 * Indicates a continuation of the execution, caused by a step out
		 * request.
		 */
		STEP_OUT
	}
	
	Action getAction();

	Subject getSubject();

	Detail getDetail();

	Object getPayload();
	
}
