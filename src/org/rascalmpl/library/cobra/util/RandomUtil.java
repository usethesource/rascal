package org.rascalmpl.library.cobra.util;

import java.util.Random;

public class RandomUtil {
	
	private interface StringGen {
		public void generate(Random rand, int length, StringBuilder result);
	}
	
	private static class CharRanges implements StringGen {
		private int[] start;
		private int[] stop;

		public CharRanges(int[] start, int[] stop) {
			assert start.length == stop.length;
			this.start = start;
			this.stop = stop;
		}
		
		public void generate(Random rand, int length, StringBuilder result) {
			for (int c = 0; c < length; c++) {
				int r = rand.nextInt(start.length);
				result.appendCodePoint(generateCodePoint(rand, start[r], stop[r]));
			}
		}

		private int generateCodePoint(Random rand, int start, int stop) {
			int range = stop - start;
			int result = 0;
			do 
				result = start + rand.nextInt(range + 1);
			while (!validCodePoint(result));
			return result;
		}
	}

	private static class CharSets implements StringGen {
		private int[] chars;

		public CharSets(int... chars) {
			this.chars = chars;
		}

		@Override
		public void generate(Random rand, int length, StringBuilder result) {
			for (int c = 0; c < length; c++)
				result.appendCodePoint(chars[rand.nextInt(chars.length)]);
		}
	}
	
	private static class MixGenerators implements StringGen {
		private StringGen[] generators;

		public MixGenerators(StringGen... generators) {
			this.generators = generators;
		}
		@Override
		public void generate(Random rand, int length, StringBuilder result) {
			int left = length;
			while (left > 0) {
				int chunk = 1 + rand.nextInt(left);
				generators[rand.nextInt(generators.length)].generate(rand, chunk, result);
				left -= chunk;
			}
		}
	}

	private static boolean validCodePoint(int cp) {
		return Character.isDefined(cp) 
			&& Character.isValidCodePoint(cp) 
			&& Character.getType(cp) != Character.UNASSIGNED
			;
	}

	private static String sanitize(String unclean) {
		// let's avoid testing with invalid codepoints
		int i = 0;
		char [] chars = unclean.toCharArray();
		while (i < chars.length) {
			char c = chars[i];
			if (Character.isHighSurrogate(c)) {
				i++;
				if (i < chars.length) {
					int cp = Character.toCodePoint(c, chars[i]);
					if (!validCodePoint(cp) || !Character.isSurrogatePair(c, chars[i])) {
						chars[i-1]	= '_';
						chars[i]	= '_';
					}
				}
				else {
					chars[i-1] = '_';
				}
			}
			else if (Character.isLowSurrogate(c)) {
				// this means the previous was not high
				chars[i] = '_';
			}
			else if (!validCodePoint(c)) {
				chars[i] = '_';
			}
			i++;
		}
		return new String(chars);
	}

	
	private final static StringGen alphaOnly = new CharRanges(new int[]{'a','A'}, new int[]{'z','Z'});
	private final static StringGen normalStrings = new CharRanges(new int[]{'a','A','0'}, new int[]{'z','Z','9'});
	private final static StringGen generalStrangeChars = new CharRanges(new int[]{0x00, 0x21,0xA1}, new int[]{0x09,0x2F,0xAC});
	private final static StringGen normalUnicode = new CharRanges(new int[]{0x0100,0x3400,0xD000}, new int[]{0x0200,0x4D00,0xD700});
	private final static StringGen strangeUnicode = new CharRanges(new int[]{0x12000, 0x20000}, new int[]{0x1247F, 0x215FF});
	private final static StringGen whiteSpace = new CharSets(' ','\t','\n','\t');
	private final static StringGen strangeWhiteSpace = new CharSets(0x85, 0xA0, 0x1680, 0x2000, 0x2028, 0x2029,0x205F,0x3000);
	private final static StringGen rascalEscapes = new CharSets('\"','\'','>','\\','<','@','`');
	
	private final static StringGen[] generators = new StringGen[] {
		normalStrings,
		normalStrings,
		normalUnicode,
		new MixGenerators(normalStrings, generalStrangeChars),
		new MixGenerators(normalStrings, whiteSpace),
		new MixGenerators(strangeWhiteSpace, whiteSpace),
		new MixGenerators(normalUnicode, strangeUnicode),
		new MixGenerators(normalStrings, rascalEscapes),
		new MixGenerators(normalStrings, generalStrangeChars, normalUnicode, whiteSpace, rascalEscapes)
	};
	
	public static String string(Random rand, int depth) {
		StringGen randomGenerator = generators[rand.nextInt(generators.length)];
		StringBuilder result = new StringBuilder(depth * 2);
		randomGenerator.generate(rand, depth, result);
		return sanitize(result.toString());
	}
	public static String stringAlphaNumeric(Random rand, int depth) {
		StringBuilder result = new StringBuilder(depth);
		normalStrings.generate(rand, depth, result);
		return sanitize(result.toString());
	}

	public static String stringAlpha(Random rand, int depth) {
		StringBuilder result = new StringBuilder(depth);
		alphaOnly.generate(rand, depth, result);
		return sanitize(result.toString());
	}

}
