/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value.impl.util.sharing;

import java.lang.ref.WeakReference;

/**
 * This is a 'weak' constant pool for uniquely represented objects.
 * <br />
 * <br />
 * This implementation is thread-safe, but tries to avoid (contended) locking as much as is
 * reasonably achievable. As a result this implementation scales fairly well on multi-core /
 * processor systems.
 * <br />
 * <br />
 * NOTE: It is highly recommended to leave this class alone; concurrency bugs can be very subtle,
 * so please don't edit this class unless you know exactly what you're doing.
 * 
 * @author Arnold Lankamp
 */
public final class ShareableValuesFactory<E extends IShareable>{
	private final static int DEFAULT_LOG_NR_OF_SEGMENTS = 5;
	
	private final int logNrOfSegments;
	private final Segment<E>[] segments;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unchecked")
	public ShareableValuesFactory(){
		super();
		
		logNrOfSegments = DEFAULT_LOG_NR_OF_SEGMENTS;
		
		segments = (Segment<E>[]) new Segment[1 << logNrOfSegments];
		for(int i = segments.length - 1; i >= 0; i--){
			segments[i] = new Segment<>(logNrOfSegments);
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param logNrOfSegments
	 *            Sets the amount of stripping to: (2 ^ logNrOfSegments).
	 * @throws IllegalArgumentException
	 *            Thrown when logNrOfSegments is to high (logNrOfSegments > 32 - 5).
	 */
	@SuppressWarnings("unchecked")
	public ShareableValuesFactory(int logNrOfSegments){
		super();
		
		if((32 - logNrOfSegments) <= 5) throw new IllegalArgumentException("logNrOfSegments can not be larger then (32 - 5).");
		
		this.logNrOfSegments = logNrOfSegments;
		
		segments = (Segment<E>[]) new Segment[1 << logNrOfSegments];
		for(int i = segments.length - 1; i >= 0; i--){
			segments[i] = new Segment<>(logNrOfSegments);
		}
	}
	
	/**
	 * Removes stale entries from the set (if any).
	 */
	public void cleanup(){
		int nrOfSegments = segments.length;
		for(int i = 0; i < nrOfSegments; i++){
			Segment<E> segment = segments[i];
			synchronized(segment){
				segment.cleanup();
			}
		}
	}
	
	/**
	 * Returns statistics.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		int nrOfSegments = segments.length;
		for(int i = 0; i < nrOfSegments; i++){
			Segment<E> segment = segments[i];
			int maxSegmentBitSize = segment.maxSegmentBitSize;
			
			int startHash = i << maxSegmentBitSize;
			int endHash = ((i + 1) << maxSegmentBitSize) - 1;
			
			sb.append("Segment hash range: ");
			sb.append(startHash);
			sb.append(" till ");
			sb.append(endHash);
			sb.append(" | ");
			sb.append(segment.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Returns a unique version of the given shareable.
	 * 
	 * @param shareable
	 *            The shareable we want the unique reference too.
	 * @return The reference to the unique version of the given shareable.
	 */
	public E build(E shareable){
		int hash = shareable.hashCode();
		int segmentNr = hash >>> (32 - DEFAULT_LOG_NR_OF_SEGMENTS);
		
		return segments[segmentNr].get(shareable, hash);
	}
	
	/**
	 * Segments are hashsets, which are responsible for elements in a certain hash-range.
	 * 
	 * @author Arnold Lankamp
	 */
	private final static class Segment<E extends IShareable>{
		private final static int DEFAULT_LOG_SEGMENT_SIZE = 5;
		private final static float DEFAULT_LOAD_FACTOR = 2f;
		
		private final int maxSegmentBitSize;
		
		private volatile Entry<E>[] entries;

		private volatile int hashMask;
		private int bitSize;
		
		private int threshold;
		private int load;
		
		private volatile boolean flaggedForCleanup;
		private volatile WeakReference<GarbageCollectionDetector<E>> garbageCollectionDetector;
		private int cleanupScaler;
		private int cleanupThreshold;
		
		/**
		 * Constructor.
		 * 
		 * @param logNrOfSegments
		 *            Specifies the maximal size this segment can grow to.
		 */
		@SuppressWarnings("unchecked")
		public Segment(int logNrOfSegments){
			super();
			
			maxSegmentBitSize = 32 - logNrOfSegments;
			
			bitSize = DEFAULT_LOG_SEGMENT_SIZE;
			int nrOfEntries = 1 << bitSize;
			
			hashMask = nrOfEntries - 1;
			
			entries = (Entry<E>[]) new Entry[nrOfEntries];
			
			threshold = (int) (nrOfEntries * DEFAULT_LOAD_FACTOR);
			load = 0;
			
			flaggedForCleanup = false;
			garbageCollectionDetector = new WeakReference<>(new GarbageCollectionDetector<>(this)); // Allocate a (unreachable) GC detector.
			cleanupScaler = 50; // Initially we set the average cleanup percentage to 50%, to make sure the cleanup can and will be executed the first time.
			cleanupThreshold = cleanupScaler;
		}
		
		/**
		 * Removes stale entries from this segment (if any).
		 */
		private void cleanup(){
			Entry<E>[] table = entries;
			int newLoad = load;
			
			for(int i = hashMask; i >= 0; i--){
				Entry<E> e = table[i];
				if(e != null){
					Entry<E> previous = null;
					do{
						Entry<E> next = e.next;
						
						if(e.get() == null){
							if(previous == null){
								table[i] = next;
							}else{
								previous.next = next;
							}
							
							newLoad--;
						}else{
							previous = e;
						}
						
						e = next;
					}while(e != null);
				}
			}
			
			load = newLoad;
			
			entries = table; // Volatile write.
		}
		
		/**
		 * Rehashes this set. All the entries will remain in the same order as they are in; this
		 * should improve look-up time in the general case and is more garbage collector friendly
		 * (you'll prevent old -> young references, contrary to the 'default' rehashing algorithm,
		 * which makes a complete mess of it).
		 */
		@SuppressWarnings("unchecked")
		private void rehash(){
			int nrOfEntries = 1 << (++bitSize);
			int newHashMask = nrOfEntries - 1;
			
			Entry<E>[] oldEntries = entries;
			Entry<E>[] newEntries = (Entry<E>[]) new Entry[nrOfEntries];
			
			// Construct temporary entries that function as roots for the entries that remain in
			// the current bucket and those that are being shifted.
			Entry<E> currentEntryRoot = new Entry<>(null, 0);
			Entry<E> shiftedEntryRoot = new Entry<>(null, 0);
			
			int newLoad = load;
			int oldSize = oldEntries.length;
			for(int i = oldSize - 1; i >= 0; i--){
				Entry<E> e = oldEntries[i];
				if(e != null){
					Entry<E> lastCurrentEntry = currentEntryRoot;
					Entry<E> lastShiftedEntry = shiftedEntryRoot;
					do{
						if(e.get() != null){ // Cleared entries should not be copied.
							int position = e.hash & newHashMask;
							
							if(position == i){
								lastCurrentEntry.next = e;
								lastCurrentEntry = e;
							}else{
								lastShiftedEntry.next = e;
								lastShiftedEntry = e;
							}
						}else{
							newLoad --;
						}
						
						e = e.next;
					}while(e != null);
					
					// Set the next pointers of the last entries in the buckets to null.
					lastCurrentEntry.next = null;
					lastShiftedEntry.next = null;
					
					newEntries[i] = currentEntryRoot.next;
					newEntries[i | oldSize] = shiftedEntryRoot.next; // The entries got shifted by the size of the old table.
				}
			}
			
			load = newLoad;
			
			threshold <<= 1;
			entries = newEntries; // Volatile write.
			hashMask = newHashMask; // Volatile write.
		}
		
		/**
		 * Ensures the load in this segment will not exceed a certain threshold.
		 */
		private void ensureCapacity(){
			// Rehash if the load exceeds the threshold, unless the segment is already stretched to
			// it's maximum (since that would be a useless thing to do).
			if(load > threshold && bitSize < maxSegmentBitSize){
				rehash();
			}
		}
		
		/**
		 * Attempts to run a cleanup if the garbage collector ran before the invocation of this
		 * function. This ensures that, in most cases, the buckets will contain no (or very little)
		 * cleared entries. This speeds up lookups significantly.
		 * <br />
		 * <br />
		 * Note that we automatically throttle the frequency of the cleanups. In case we hardly
		 * every collect anything (either because there is no garbage or collections occur very
		 * frequently) cleanups will be slowed down to as little as once every four minor garbage
		 * collections. When a lot of entries are being cleared the cleanup will run after every
		 * collection.
		 * <br />
		 * This is done in an attempt to balance the overhead a cleanup induces with the
		 * performance gains it has on lookups; while keeping the probability of long-lived stale
		 * entries relatively low. Additionally, linking the cleanup 'trigger' to garbage
		 * collections ensures that we clean the segment exactly when it is both needed and
		 * possible.
		 * <br />
		 * <br />
		 * NOTE: This may seem a bit 'dodgy' to some, but this does work on (at least) every major
		 * JVM since 1.2, and will remain working properly while weak-references keep functioning
		 * as intended. Apart from that, this way of handling it is significantly faster then any
		 * of the alternatives (i.e. using reference queues is slow as *bleep* in comparison).
		 * Additionally, it releases us from synchronizing during every single call.
		 */
		private void tryCleanup(){
			if(flaggedForCleanup){
				flaggedForCleanup = false;
				
				synchronized(this){
					if(garbageCollectionDetector == null){ // This being 'null' indicates the same thing as 'flaggedForCleanup' being 'true' (but we do it like this to prevent potential optimizations the VM probably doesn't, but could apply); and yes DCL like stuff works on volatiles.
						if(cleanupThreshold > 8){ // The 'magic' number 8 is chosen, so the cleanup will be done at least once after every four garbage collections.
							int oldLoad = load;
							
							cleanup();
							
							int cleanupPercentate;
							if(oldLoad == 0) cleanupPercentate = 50; // This prevents division by zero errors in case the table is empty (keep the cleanup percentage at 50% in this case).
							else cleanupPercentate = 100 - ((load * 100) / oldLoad); // Calculate the percentage of entries that has been cleaned.
							
							cleanupScaler = (((cleanupScaler * 25) + (cleanupPercentate * 7)) >> 5); // Modify the scaler, depending on the history (weight = 25) and how much we cleaned up this time (weight = 7).
							if(cleanupScaler > 0){
								cleanupThreshold = cleanupScaler;
							}else{
								cleanupThreshold = 1; // If the scaler value became 0 (when we hardly every collect something), set the threshold to 1, so we only skip the next three garbage collections.
							}
						}else{
							cleanupThreshold <<= 1;
						}
						
						garbageCollectionDetector = new WeakReference<>(new GarbageCollectionDetector<>(this)); // Allocate a new (unreachable) GC detector.
					}
				}
			}
		}
		
		/**
		 * Inserts the given shareable into this set.
		 * 
		 * @param shareable
		 *            The shareable to insert.
		 * @param hash
		 *            The hash code that is associated with the given shareable.
		 */
		private void put(E shareable, int hash){
			Entry<E> e = new Entry<>(shareable, hash);
			
			Entry<E>[] table = entries;
			int position = hash & hashMask;
			e.next = table[position];
			table[position] = e;
			
			load++;
			
			table = entries; // Volatile write.
		}
		
		/**
		 * Returns a reference to the unique version of the given shareable.
		 * 
		 * @param shareable
		 *            The shareable of which we want to obtain the reference to the unique version.
		 * @param hash
		 *            The hash code that is associated with the given shareable.
		 * @return The reference to the unique version of the given shareable.
		 */
		public final E get(E shareable, int hash){
			// Cleanup if necessary.
			tryCleanup();
			
			// Find the object (lock free).
			int position = hash & hashMask;
			Entry<E> e = entries[position]; // Volatile read.
			if(e != null){
				do{
					if(hash == e.hash){
						E object = e.get();
						if(object != null){
							if(shareable.equivalent(object)){
								return object;
							}
						}
					}
					e = e.next;
				}while(e != null);
			}
			
			synchronized(this){
				// Try again while holding the global lock for this segment.
				position = hash & hashMask;
				e = entries[position];
				if(e != null){
					do{
						if(hash == e.hash){
							E object = e.get();
							if(object != null){
								if(shareable.equivalent(object)){
									return object;
								}
							}
						}
						e = e.next;
					}while(e != null);
				}
				
				// If we still can't find it, add it.
				ensureCapacity();
				E result = shareable;
				put(result, hash);
				return result;
			}
		}
		
		/**
		 * Returns statistics.
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			StringBuilder sb = new StringBuilder();
			
			synchronized(this){
				Entry<E>[] table = entries;
				
				int tableSize = table.length;
				
				sb.append("Table size: ");
				sb.append(tableSize);
				sb.append(", ");
				
				sb.append("Number of entries: ");
				sb.append(load);
				sb.append(", ");
				
				sb.append("Threshold: ");
				sb.append(threshold);
				sb.append(", ");
				
				int nrOfFilledBuckets = 0;
				int totalNrOfCollisions = 0;
				int maxBucketLength = 0;
				for(int i = 0; i < tableSize; i++){
					Entry<E> e = table[i];
					if(e != null){
						nrOfFilledBuckets++;
						int bucketLength = 1;
						while((e = e.next) != null){
							bucketLength++;
						}
						if(bucketLength > maxBucketLength) maxBucketLength = bucketLength;
						totalNrOfCollisions += bucketLength - 1;
					}
				}
				// Do some voodoo to round the results on a certain amount of decimals (3 and 1
				// respectively); or at least attempt to do so ....
				double averageBucketLength = 0;
				double distribution = 100;
				if(nrOfFilledBuckets != 0){
					averageBucketLength = (((double) ((totalNrOfCollisions * 1000) / nrOfFilledBuckets)) / 1000) + 1;
					distribution = 100 - (((double) (((totalNrOfCollisions * 1000) / nrOfFilledBuckets) / DEFAULT_LOAD_FACTOR)) / 10);
				}
				
				sb.append("Number of filled buckets: ");
				sb.append(nrOfFilledBuckets);
				sb.append(", ");
				
				sb.append("Load factor: ");
				sb.append(DEFAULT_LOAD_FACTOR);
				sb.append(", ");
				
				sb.append("Distribution (collisions vs filled buckets): "); // Total number of collisions vs number of filled buckets.
				sb.append(distribution);
				sb.append("%, ");
				
				sb.append("Total number of collisions: ");
				sb.append(totalNrOfCollisions);
				sb.append(", ");
				
				sb.append("Average (filled) bucket length: ");
				sb.append(averageBucketLength);
				sb.append(", ");
				
				sb.append("Maximal bucket length: ");
				sb.append(maxBucketLength);
				sb.append(", ");
				
				sb.append("Cleanup scaler: ");
				sb.append(cleanupScaler);
				sb.append("%");
			}
			
			return sb.toString();
		}
		
		/**
		 * An entry used for storing shareables in this segment.
		 * 
		 * @author Arnold Lankamp
		 */
		private static class Entry<E extends IShareable> extends WeakReference<E>{
			public final int hash;
			public Entry<E> next;
			
			/**
			 * Constructor.
			 * 
			 * @param shareable
			 *            The shareable.
			 * @param hash
			 *            The hashcode that is associated with the given shareable.
			 */
			public Entry(E shareable, int hash){
				super(shareable);
				
				this.hash = hash;
			}
		}
		
		/**
		 * An object that can be used to detect when a garbage collection has been executed.
		 * Instances of this object must be made weakly reachable for this to work.
		 * 
		 * @author Arnold Lankamp
		 */
		private static class GarbageCollectionDetector<E extends IShareable>{
			private final Segment<E> segment;
			
			/**
			 * Constructor.
			 * 
			 * @param segment
			 *            The segment we need to flag after a garbage collection has occurred.
			 */
			public GarbageCollectionDetector(Segment<E> segment){
				this.segment = segment;
			}
			
			/**
			 * Executed after the garbage collector detects that this object is eligible for
			 * reclamation. When this happens it will flag the associated segment for cleanup.
			 * 
			 * @see java.lang.Object#finalize
			 */
			protected void finalize(){
				segment.garbageCollectionDetector = null;
				segment.flaggedForCleanup = true;
			}
		}
	}
}
