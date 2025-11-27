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

import io.usethesource.vallang.ISourceLocation;

/**
 * Factory to support the creation of {@link IDebugMessage} instances.
 */
public class DebugMessageFactory {
	
	/*
	 * Interpreter requests.
	 */
	public static IDebugMessage requestSuspension() {
		return new DebugMessage(IDebugMessage.Action.SUSPEND, IDebugMessage.Subject.INTERPRETER, IDebugMessage.Detail.CLIENT_REQUEST);
	}
	
	public static IDebugMessage requestResumption() {
		return new DebugMessage(IDebugMessage.Action.RESUME, IDebugMessage.Subject.INTERPRETER, IDebugMessage.Detail.CLIENT_REQUEST);
	}
	
	public static IDebugMessage requestStepInto() {
		return new DebugMessage(IDebugMessage.Action.RESUME, IDebugMessage.Subject.INTERPRETER, IDebugMessage.Detail.STEP_INTO);
	}	
	
	public static IDebugMessage requestStepOver() {
		return new DebugMessage(IDebugMessage.Action.RESUME, IDebugMessage.Subject.INTERPRETER, IDebugMessage.Detail.STEP_OVER);
	}

	public static IDebugMessage requestStepOut(){
		return new DebugMessage(IDebugMessage.Action.RESUME, IDebugMessage.Subject.INTERPRETER, IDebugMessage.Detail.STEP_OUT);
	}
	
	public static IDebugMessage requestTermination() {
		return new DebugMessage(IDebugMessage.Action.TERMINATE, IDebugMessage.Subject.INTERPRETER, IDebugMessage.Detail.UNKNOWN);
	}
	
	/*
	 * Breakpoint requests.
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
}
