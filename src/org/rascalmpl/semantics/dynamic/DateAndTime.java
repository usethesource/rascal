package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.joda.time.DateTime;
import org.rascalmpl.ast.DateAndTime.Lexical;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.DateTimeParseError;

public abstract class DateAndTime extends org.rascalmpl.ast.DateAndTime {

	static public class Lexical extends org.rascalmpl.ast.DateAndTime.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			// Split into date and time components; of the form $<date>T<time>
			String dtPart = this.getString().substring(1);
			String datePart = dtPart.substring(0, dtPart.indexOf("T"));
			String timePart = dtPart.substring(dtPart.indexOf("T") + 1);

			return createVisitedDateTime(__eval, datePart, timePart, this);
		}

		@Override
		public Type typeOf(Environment env) {
			return TF.dateTimeType();
		}
		
		public Result<IValue> createVisitedDateTime(Evaluator eval, String datePart, String timePart, Lexical x) {
			String isoDate = datePart;
			if (-1 == datePart.indexOf("-")) {
				isoDate = datePart.substring(0, 4) + "-" + datePart.substring(4, 6) + "-" + datePart.substring(6);
			}
			String isoTime = timePart;
			if (-1 == timePart.indexOf(":")) {
				isoTime = timePart.substring(0, 2) + ":" + timePart.substring(2, 4) + ":" + timePart.substring(4);
			}
			String isoDateTime = isoDate + "T" + isoTime;
			try {
				DateTime dateAndTime = org.joda.time.format.ISODateTimeFormat.dateTimeParser().parseDateTime(isoDateTime);
				int hourOffset = dateAndTime.getZone().getOffset(dateAndTime.getMillis()) / 3600000;
				int minuteOffset = (dateAndTime.getZone().getOffset(dateAndTime.getMillis()) / 60000) % 60;
				return makeResult(
						TF.dateTimeType(),
						VF.datetime(dateAndTime.getYear(), dateAndTime.getMonthOfYear(), dateAndTime.getDayOfMonth(), dateAndTime.getHourOfDay(), dateAndTime.getMinuteOfHour(),
								dateAndTime.getSecondOfMinute(), dateAndTime.getMillisOfSecond(), hourOffset, minuteOffset), eval);
			} catch (IllegalArgumentException iae) {
				throw new DateTimeParseError("$" + datePart + "T" + timePart, x.getLocation());
			}
		}

	}

	public DateAndTime(INode __param1) {
		super(__param1);
	}
}
