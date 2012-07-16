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
package org.rascalmpl.interpreter.debug;

import org.eclipse.imp.pdb.facts.ISourceLocation;

/**
 * Factory to support the creation of {@link IDebugMessage} instances.
 */
public class DebugMessageFactory {
	
	/*
	 * Requests.
	 */
	
	public static IDebugMessage requestSuspension() {
		return new RequestMessage(IDebugMessage.Subject.SUSPENSION, IDebugMessage.Detail.CLIENT_REQUEST);
	}
	
	public static IDebugMessage requestResumption() {
		return new RequestMessage(IDebugMessage.Subject.RESUMPTION, IDebugMessage.Detail.CLIENT_REQUEST);
	}
	
	public static IDebugMessage requestStepInto() {
		return new RequestMessage(IDebugMessage.Subject.RESUMPTION, IDebugMessage.Detail.STEP_INTO);
	}	
	
	public static IDebugMessage requestStepOver() {
		return new RequestMessage(IDebugMessage.Subject.RESUMPTION, IDebugMessage.Detail.STEP_OVER);
	}
	
	public static IDebugMessage requestTermination() {
		return new RequestMessage(IDebugMessage.Subject.TERMINATION, IDebugMessage.Detail.UNKNOWN);
	}
	
	/*
	 * Breakpoints.
	 */
	
	public static IDebugMessage requestSetBreakpoint(ISourceLocation location) {
		return new DebugMessage(IDebugMessage.Action.SET, IDebugMessage.Subject.BREAKPOINT, IDebugMessage.Detail.UNKNOWN, location);
	}

	public static IDebugMessage requestDeleteBreakpoint(ISourceLocation location) {
		return new DebugMessage(IDebugMessage.Action.DELETE, IDebugMessage.Subject.BREAKPOINT, IDebugMessage.Detail.UNKNOWN, location);
	}
	
	/*
	 * Simple message implementation.
	 */	
	
	private static class DebugMessage implements IDebugMessage {

		private final Action action;
		private final Subject subject;
		private final Detail detail;
		private final Object payload;
	
		public DebugMessage(Action action, Subject subject, Detail detail) {
			this(action, subject, detail, null);
		}
		
		public DebugMessage(Action action, Subject subject, Detail detail, Object payload) {
			this.action = action;
			this.subject = subject;
			this.detail = detail;
			this.payload = payload;
		}
		
		@Override
		public Action getAction() {
			return action;
		}

		@Override
		public Subject getSubject() {
			return subject;
		}

		@Override
		public Detail getDetail() {
			return detail;
		}

		@Override
		public Object getPayload() {
			return payload;
		}
		
	}
	
	private static class RequestMessage extends DebugMessage {
		
		public RequestMessage(Subject subject, Detail detail) {
			super(IDebugMessage.Action.REQEUST, subject, detail);
		}
		
		public RequestMessage(Subject subject, Detail detail, Object payload) {
			super(IDebugMessage.Action.REQEUST, subject, detail, payload);
		}

	}	
	
}
