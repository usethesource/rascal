package org.rascalmpl.library.cobra.util;

import java.util.Random;

public class RandomUtil {
	
	private static abstract class StringGen {
		public abstract String generate(Random rand, int length);
	}
	
	private static class CharRanges extends StringGen {
		private int[] start;
		private int[] stop;

		public CharRanges(int[] start, int[] stop) {
			assert start.length == stop.length;
			this.start = start;
			this.stop = stop;
		}
		
		public String generate(Random rand, int length) {
			StringBuilder result = new StringBuilder(length);
			for (int c = 0, r = 0; c < length; c++, r++) {
				if (r >= start.length) {
					r = 0;
				}
				result.appendCodePoint(generateCodePoint(rand, start[r], stop[r]));
			}
			return result.toString();
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

	private static boolean validCodePoint(int cp) {
		return Character.isDefined(cp) 
			&& Character.isValidCodePoint(cp) 
			&& Character.getType(cp) != Character.UNASSIGNED
			;
	}

	private static class CharSets extends StringGen {
		private int[] chars;

		public CharSets(int... chars) {
			this.chars = chars;
		}

		@Override
		public String generate(Random rand, int length) {
			StringBuilder result = new StringBuilder(length);
			for (int c = 0; c < length; c++)
				result.appendCodePoint(chars[rand.nextInt(chars.length)]);
			return result.toString();
		}
	}
	
	private static class MixGenerators extends StringGen {
		private StringGen[] generators;

		public MixGenerators(StringGen... generators) {
			this.generators = generators;
		}
		@Override
		public String generate(Random rand, int length) {
			StringBuilder result = new StringBuilder(length);
			int left = length;
			while (left > 0) {
				int chunk = 1 + rand.nextInt(left);
				result.append(generators[rand.nextInt(generators.length)].generate(rand, chunk));
				left -= chunk;
			}
			return result.toString();
		}
	}
	
	
	
	private static class NormalStrings extends CharRanges {
		public NormalStrings() {
			super(new int[]{'a','A','0'}, new int[]{'z','Z','9'});
		}
	}
	
	private static class NormalUnicode extends CharRanges {
		public NormalUnicode() {
			super(new int[]{0x0100,0x3400,0xD000}, new int[]{0x0200,0x4D00,0xD7000});
		}
	}
	private static class StrangeUnicode extends CharRanges {
		public StrangeUnicode() {
			super(new int[]{0x12000, 0x20000}, new int[]{0x1247F, 0x215FF});
		}
	}
	private static class WhiteSpace extends CharSets {
		public WhiteSpace() {
			super(new int[]{' ','\t','\n','\t'});
		}
	}
	private static class StrangeWhiteSpace extends CharSets {
		public StrangeWhiteSpace() {
			super(new int[]{0x85, 0xA0, 0x1680, 0x2000, 0x2028, 0x2029,0x205F,0x3000});
		}
	}
	private static class GeneralStrangeChars extends CharRanges {
		public GeneralStrangeChars() {
			super(new int[]{0x00, 0x21,0xA1}, new int[]{0x09,0x2F,0xAC});
		}
	}
	private static class RascalEscapes extends CharSets {
		public RascalEscapes() {
			super(new int[]{'\"','\'','>','\\','<','@','`'});
		}
	}

	public static String string(Random rand, int depth) {
		StringGen generator = null;
		switch (rand.nextInt(10)) {
		case 0:
			generator = new RandomUtil.MixGenerators(new RandomUtil.NormalStrings(), new RandomUtil.GeneralStrangeChars());
			break;
		case 1:
			generator = new RandomUtil.MixGenerators(new RandomUtil.NormalStrings(), new RandomUtil.WhiteSpace());
			break;
		case 2:
			generator = new RandomUtil.MixGenerators(new RandomUtil.StrangeWhiteSpace(), new RandomUtil.WhiteSpace());
			break;
		case 3:
			generator = new RandomUtil.NormalUnicode();
			break;
		case 4:
			generator = new RandomUtil.MixGenerators(new RandomUtil.NormalUnicode(), new RandomUtil.StrangeUnicode());
			break;
		case 5:
			generator = new RandomUtil.MixGenerators(new RandomUtil.NormalStrings(), new RandomUtil.RascalEscapes());
			break;
		case 6:
			// all of them
			generator = new RandomUtil.MixGenerators(
					new RandomUtil.NormalStrings(), 
					new RandomUtil.GeneralStrangeChars(), 
					new RandomUtil.NormalUnicode(), 
					new RandomUtil.WhiteSpace(), 
					new RandomUtil.RascalEscapes()
					);
			break;
		default:
			generator = new RandomUtil.NormalStrings();
			break;
		}
		return sanitize(generator.generate(rand, depth));
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
					if (!validCodePoint(cp)) {
						chars[i-1]	= '_';
						chars[i]	= '_';
					}
				}
				else {
					chars[i-1] = '_';
				}
			}
			else if (!validCodePoint(c)) {
				chars[i] = '_';
			}
			i++;
		}
		return new String(chars);
	}
}
