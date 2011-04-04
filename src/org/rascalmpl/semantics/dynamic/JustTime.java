/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.joda.time.DateTime;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.DateTimeParseError;

public abstract class JustTime extends org.rascalmpl.ast.JustTime {

	static public class Lexical extends org.rascalmpl.ast.JustTime.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			// Time is of the form $T<time>
			String timePart = this.getString().substring(2);
			return createVisitedTime(__eval, timePart, this);
		}
		
		private Result<IValue> createVisitedTime(Evaluator eval, String timePart, org.rascalmpl.ast.JustTime.Lexical x) {
			String isoTime = timePart;
			if (-1 == timePart.indexOf(":")) {
				isoTime = timePart.substring(0, 2) + ":" + timePart.substring(2, 4) + ":" + timePart.substring(4);
			}
			try {
				DateTime justTime = org.joda.time.format.ISODateTimeFormat.timeParser().parseDateTime(isoTime);
				int hourOffset = justTime.getZone().getOffset(justTime.getMillis()) / 3600000;
				int minuteOffset = (justTime.getZone().getOffset(justTime.getMillis()) / 60000) % 60;
				return makeResult(TF.dateTimeType(),
						VF.time(justTime.getHourOfDay(), justTime.getMinuteOfHour(), justTime.getSecondOfMinute(), justTime.getMillisOfSecond(), hourOffset, minuteOffset), eval);
			} catch (IllegalArgumentException iae) {
				throw new DateTimeParseError("$T" + timePart, x.getLocation());
			}
		}

	}

	public JustTime(INode __param1) {
		super(__param1);
	}

}
