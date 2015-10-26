package org.rascalmpl.value.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;


public class MixDistribution {
	private static final int[] BIT_OFFSET = new int[32];
	static {
		for (int i=0; i < 32; i++) {
			BIT_OFFSET[i] = 1 << i;
		}
	}
	
	private static void reportCollisions(String name, int[] data, Mixer mixer) {
		Set<Integer> used = new HashSet<Integer>();
		Set<Integer> seen = new HashSet<Integer>();
		Set<Integer> seen16 = new HashSet<Integer>();
		int collisions = 0;
		int collisions16 = 0;
		for (int d : data) {
			if (used.contains(d)) {
				continue;
			}
			used.add(d);
			int m = mixer.mix(d);
			if (seen.contains(m)) {
				collisions++;
				continue;
			}
			seen.add(m);
			int m16l = m & 0x0000FFFF;
			int m16h = m & 0xFFFF0000;
			if (seen16.contains(m16l) || seen16.contains(m16h)) {
				collisions16++;
			}
			seen16.add(m16l);
			seen16.add(m16h);
		}
		System.out.println(name + " full collisions: " + collisions);
		System.out.println(name + " 16-bit collisions: " + collisions16);
	}

	private static void reportHashDistribution(String name, int[] hashes) {
		int[] counts = new int[32];
		for (int h : hashes) {
			for (int bit =0; bit < 32; bit++) {
				if ((h & BIT_OFFSET[bit]) != 0) {
					counts[bit]++;
				}
			}
		}
		System.out.print(name);
		for (int c: counts) {
			System.out.print(',');
			System.out.print(c);
		}
		System.out.println();
		Double total = 0.0;
		int maxError = 0;
		System.out.print(name+"-offset");
		for (int c: counts) {
			System.out.print(',');
			int error = c - (hashes.length / 2);
			System.out.print(error);
			total += (error * error);
			maxError = Math.max(maxError, Math.abs(error));
		}
		System.out.println();
		System.out.println(name + " max absolute offset: " + maxError);

		
		System.out.println(name + " root mean error:" + Math.sqrt(total / hashes.length));
	}
	
	private static void createBitStatsPlot(String name, String category, int[] numbers, Mixer mixer) throws IOException {
		int[][] bits = new int[32][32];
		for (int input : numbers) {
			int A = mixer.mix(input);
			for (int sourceBit = 0; sourceBit < 32; sourceBit++) {
				int flipped = input ^ BIT_OFFSET[sourceBit];
				int B = mixer.mix(flipped);
				for (int bit =0; bit < 32; bit++) {
					int Abit = A & BIT_OFFSET[bit];
					int Bbit = B & BIT_OFFSET[bit];
					if (Abit != Bbit) {
						bits[sourceBit][bit]++;
					}
				}
			}
		}
		BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < 32; x++) {
			for (int y = 0; y < 32; y++) {
				img.setRGB(x, y, getColor(Math.abs((numbers.length/2)-bits[x][y])/((double)numbers.length/2)));
			}
		}
		ImageIO.write(img, "PNG", new File("avalanche-" + category +"-"+ name + ".png"));
	}
	
	private static int getColor(double errorRate) {
		double green = 0.0;
		double red = 0.0;
		if (0 <= errorRate && errorRate < 0.5 ) {
			green = 1.0;
			red = 2 * errorRate;
		}
		else {
			red = 1.0;
		    green = 1.0 - 2 * (errorRate-0.5);
		}
		return 
			  (((int)Math.floor(red * 255)) << 16) 
			| (((int)Math.floor(green * 255)) << 8)
			;
				
		
	}
	
	public static void main(String[] args) throws IOException {
		Map<String, Mixer> mixers = new LinkedHashMap<>();
		mixers.put("raw", new RawMix());
		mixers.put("xxhash", new XXHashMix());
		mixers.put("xxhash2", new XXHashMix2());
		mixers.put("lookup3", new Lookup3Mix());
		mixers.put("murmur2", new MurmurHash2Mix());
		mixers.put("murmur2-2", new MurmurHash2Mix2());
		mixers.put("murmur2-3", new MurmurHash2Mix3());
		mixers.put("murmur2-4", new MurmurHash2Mix4());
		mixers.put("murmur3", new MurmurHash3Mix());
		mixers.put("murmur3-2", new MurmurHash3Mix2());
		mixers.put("crapwow", new CrapWowMix());
		mixers.put("crapwow-2", new CrapWowMix2());
		mixers.put("crapwow-3", new CrapWowMix3());
		mixers.put("encode.ru-m2-2", new EncodeRUm2Mix());
		mixers.put("superfasthash", new SuperFastHashMix());
		mixers.put("superfasthash2", new SuperFastHashMix2());
		mixers.put("hashmap", new HashMapMix());
		mixers.put("scala-hashmap", new ScalaHashMapMix());
		
		int[] data = new int[10000];
		for (int i=0; i < data.length; i++) {
			data[i] = i;
		}
		
		System.out.println("Numbers from 1-10000");
		for (String m : mixers.keySet()) {
			reportHashDistribution(m, mix(data, mixers.get(m)));
			reportCollisions(m, data, mixers.get(m));
		}
		System.out.println("");
		System.out.println("");
		System.out.println("Numbers from 1-10000 << 16");
		for (int i=0; i < data.length; i++) {
			data[i] = i << 16;
		}
		for (String m : mixers.keySet()) {
			reportHashDistribution(m, mix(data, mixers.get(m)));
			reportCollisions(m, data, mixers.get(m));
		}
		
		
		Random rand = new Random();
		for (int i=0; i < data.length; i++) {
			data[i] = rand.nextInt();
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("Random numbers");
		for (String m : mixers.keySet()) {
			reportHashDistribution(m, mix(data, mixers.get(m)));
			reportCollisions(m, data, mixers.get(m));
			createBitStatsPlot(m, "random", data, mixers.get(m));
		}
		
		
		data = new int[512];
		for (int i=0; i < data.length; i++) {
			data[i] = i;
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("Random small numbers");
		for (String m : mixers.keySet()) {
			reportHashDistribution(m, mix(data, mixers.get(m)));
			reportCollisions(m, data, mixers.get(m));
		}
	}
	
	
	private static int[] mix(int[] data, Mixer mixer) {
		int[] result = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			result[i] = mixer.mix(data[i]);
		}
		return result;
	}


	private static interface Mixer {
		int mix(int n);
	}
	
	private static class RawMix implements Mixer {
		@Override
		public int mix(int n) {
			return n;
		}
	}
	
	private static class XXHashMix implements Mixer {
		@Override
		public int mix(int n) {
			int h32 = 0x165667b1;
			
			h32 += n * (int)3266489917L;
			h32 = Integer.rotateLeft(h32, 17) * 668265263;
			
			h32 ^= h32 >>> 15;
			h32 *= (int) 2246822519L;
			h32 ^= h32 >>> 13;
			h32 *= (int) 3266489917L;
			h32 ^= h32 >>> 16;

			return h32;
		}
	}
	private static class XXHashMix2 implements Mixer {
		@Override
		public int mix(int n) {
			int h32 = n + 0x165667b1;
			h32 ^= h32 >>> 15;
			h32 *= (int) 2246822519L;
			h32 ^= h32 >>> 13;
			h32 *= (int) 3266489917L;
			h32 ^= h32 >>> 16;
			return h32;
		}
	}
	private static class HashMapMix implements Mixer {
		@Override
		public int mix(int n) {
			int h = n;
			h ^= (h >>> 20) ^ (h >>> 12);
			return h ^ (h >>> 7) ^ (h >>> 4);
		}
	}
	
	private static class ScalaHashMapMix implements Mixer {
		@Override
		public int mix(int n) {
			int h = n + ~(n << 9);
			h = h ^ (h >>> 14);
			h = h + (h << 4);
			return h ^ (h >>> 10);
		}
	}
	
	private static class MurmurHash2Mix implements Mixer {
		@Override
		public int mix(int n) {
			int h = 1 ^ 4;

			int k = n;
			k = mixK(k);
			h = mixH(h, k);
			
			h *= m;
			h ^= h >>> 13;
			h *= m;
			h ^= h >>> 15;
			
			return h;
		}

		private final int m = 0x5bd1e995;
		private final int r = 24;

		private int mixK(int k) {
			k *= m;
			k ^= k >>> r;
			k *= m;
			return k;
		}
		
		private int mixH(int h, int k) {
			h *= m;
			h ^= k;
			return h;
		}
	}
	private static class MurmurHash2Mix2 implements Mixer {
		@Override
		public int mix(int n) {
			int h = n;
			
			h *= 0x5bd1e995;
			h ^= h >>> 13;
			h *= 0x5bd1e995;
			h ^= h >>> 15;
			
			return h;
		}

	}
	private static class MurmurHash2Mix3 implements Mixer {
		@Override
		public int mix(int n) {
			int h = n ^ 0x85ebca6b;
			
			h ^= h >>> 13;
			h *= 0x5bd1e995;
			h ^= h >>> 15;
			
			return h;
		}

	}
	private static class MurmurHash2Mix4 implements Mixer {
		@Override
		public int mix(int n) {
			int h = n ^ 0x85ebca6b;
			
			h ^= h >>> 13;
			h *= 0x5bd1e995;
			h ^= h >>> 15;

			h += ( h >> 22 ) ^ ( h << 4 );
			
			return h;
		}

	}
	
	private static class Lookup3Mix implements Mixer {
		@Override
		public int mix(int n) {
			int a,b,c;
			a = b = c = 0xdeadbeef + (1<<2);
			a += n;
			{
				c ^= b; c -= (b<<14)|(b>>>-14);
				a ^= c; a -= (c<<11)|(c>>>-11);
				b ^= a; b -= (a<<25)|(a>>>-25);
				c ^= b; c -= (b<<16)|(b>>>-16);
				a ^= c; a -= (c<<4)|(c>>>-4);
				b ^= a; b -= (a<<14)|(a>>>-14);
				c ^= b; c -= (b<<24)|(b>>>-24);
			}
			return c;
		}
	}
	
	private static class MurmurHash3Mix implements Mixer{
		
		@Override
		public int mix(int n) {
			int h = 1;

			int k = n;
			k = mixK(k);
			h = mixH(h, k);

			// finalizing
			h ^= 1;

			h ^= h >>> 16;
			h *= 0x85ebca6b;
			h ^= h >>> 13;
			h *= 0xc2b2ae35;
			h ^= h >>> 16;

			return h;
		}
		
		private final static int C1 = 0xcc9e2d51;
		private final static int C2 = 0x1b873593;
		private final static int M = 5;
		private final static int N = 0xe6546b64;


		private final static int mixK(int k) {
			k *= C1;
			k = Integer.rotateLeft(k, 15);
			k = k * C2;
			return k;
		}

		private final static int mixH(int h, int k) {
			h ^= k;
			h = Integer.rotateLeft(h, 13);
			h = h * M + N;
			return h;
		}
	}

	private static class MurmurHash3Mix2 implements Mixer{
		
		@Override
		public int mix(int n) {
			int h = n ^ 1;

			h ^= h >>> 16;
			h *= 0x85ebca6b;
			h ^= h >>> 13;
			h *= 0xc2b2ae35;
			h ^= h >>> 16;

			return h;
		}
	}
	
	private static class SuperFastHashMix implements Mixer {
		@Override
		public int mix(int n) {
			int hash = 0, tmp;
			
			hash += n & 0xFFFF;
			tmp = ((n & 0xFFFF0000) >> 5) ^ hash;
	        hash = (hash << 16) ^ tmp;

			hash += hash >> 11;
	        hash ^= hash << 3;
	        hash += hash >> 5;
	        hash ^= hash << 4;
	        hash += hash >> 17;
	        hash ^= hash << 25;
	        hash += hash >> 6;
			return hash;
		}
	}

	private static class SuperFastHashMix2 implements Mixer {
		@Override
		public int mix(int n) {
			int hash = n;

			hash += hash >> 11;
	        hash ^= hash << 3;
	        hash += hash >> 5;
	        hash ^= hash << 4;
	        hash += hash >> 17;
	        hash ^= hash << 25;
	        hash += hash >> 6;
			return hash;
		}
	}
	
	private static class CrapWowMix implements Mixer {

	  private final static int M = 0x57559429, N = 0x5052acdb;

	  @Override
	  public int mix(int n) {
	    int h = 1;
	    int k = 1 + n + N;

	    // "loop"
	    long p = n * (long)M;
	    h ^= (int)p;
	    k ^= (int)(p >> 32);
	    
	    // end mix
	    p = (h ^ (k + N)) * (long)N;
	    k ^= (int)p;
	    h ^= (int)(p >> 32);
	    return k ^ h;
	  }

	}

	private static class CrapWowMix2 implements Mixer {

	  private final static int M = 0x57559429, N = 0x5052acdb;

	  @Override
	  public int mix(int n) {
	    long p = n * (long)M;
	    p ^= (long)n + (long)N;
	    p *= N;
	    return ((int)p) ^ (int)(p >> 32);
	  }
	}
	
	private static class CrapWowMix3 implements Mixer {

	  private final static int M = 0x57559429, N = 0x5052acdb;

	  @Override
	  public int mix(int n) {
	    long p;
	    p = n * (long)M;
	    p ^= ((long)n) << 32;
	    p *= (long)N;
	    return ((int)p) ^ (int)(p >> 32);
	  }
	}
	
	private static class EncodeRUm2Mix implements Mixer {
	  
	  @Override
	  public int mix(int n) {
	    long k = n;
	    k ^= k >> 33;
	    k *= 0xff51afd7ed558ccdL;
	    k ^= k >> 33;
	    k *= 0xc4ceb9fe1a85ec53L;
	    k ^= k >> 33;
	   
	    return (int)k ^ (int)(k >> 32);
	  }
	}
}
