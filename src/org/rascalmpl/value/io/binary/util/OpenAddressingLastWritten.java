/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.value.io.binary.util;

import java.util.Arrays;

/**
 * A track last written implementation that uses linear open addressing to implement the very specific hashmap
 * 
 * @author Davy Landman
 */
public abstract class OpenAddressingLastWritten<T> implements TrackLastWritten<T> {
    private final int maximumEntries;
    private final int tableSize;
    private final Object[] keys;
    private final long[] writtenAt;
    /**
     * We store the hashes at the cost of extra memory, however, this improves the speed of the remove method when the hash is not a simple System.identityHashCode
     */
    private final int[] hashes;
    /**
     * A circular buffer with index to the table (keys/writtenAt/hashes) in the order that the entry was written.
     */
    private final int[] oldest;
    /**
     * how many entries are already written
     */
    private long written;
    
    /**
     * Create a n OpenAddressingLastWritten container using reference equality and identiy hashcode.
     * @param maximumEntries larger than 0 and smaller than Integer.MAX_VALUE  / 2
     */
    public static <T> OpenAddressingLastWritten<T> referenceEquality(int maximumEntries) {
        return new OpenAddressingLastWritten<T>(maximumEntries) {
            @Override
            protected boolean equals(T a, T b) {
                return a == b;
            }

            @Override
            protected int hash(T obj) {
                return System.identityHashCode(obj);
            }
        };
    }

    /**
     * @param maximumEntries larger than 0 and smaller than Integer.MAX_VALUE  / 2
     */
    public OpenAddressingLastWritten(int maximumEntries) {
        if (maximumEntries <= 0) {
            throw new IllegalArgumentException("Maximum entries should be a positive number");
        }
        if (maximumEntries > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException("Maximum entries should be smaller than " + Integer.MAX_VALUE / 2);
        }
        this.maximumEntries = maximumEntries;
        this.tableSize = closestPrime(maximumEntries * 3);  // load factor of at least 33%
        keys = new Object[this.tableSize];
        writtenAt = new long[this.tableSize];
        hashes = new int[this.tableSize];
        oldest = new int[maximumEntries];
        Arrays.setAll(oldest, (i) -> -1);
        written = 0;
    }
    
    //http://stackoverflow.com/a/20798440/11098
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) return false;
        return true;
    }
    
    private static int closestPrime(int i) {
        if (i <= 2) {
            return 2;
        }
        if (i % 2 == 0) {
            i++; // only try odd numbers
        }
        while (i > 0 && !isPrime(i)) {
            i += 2; // next odd number
        }
        if (i <= 0) {
            // overflow, so let's return maxsize - 8 which appears to be the largest possible array;
            return Integer.MAX_VALUE - 8;
        }
        return i;
    }

    @Override
    public int howLongAgo(T obj) {
        int pos = locate(obj);
        if (pos != -1) {
            return (int) ((written - writtenAt[pos]) - 1);
        }
        return -1;
    }
    
    private int translateOldest(long index) {
        return (int) (index % maximumEntries);
    }
    
    @SuppressWarnings("unchecked")
    private int locate(T obj) {
        int pos = (hash(obj) & 0x7FFFFFFF) % tableSize;
        final Object[] keys = this.keys;
        Object current = keys[pos];
        if (current == null) {
            return -1;
        }
        while (!equals((T)current, obj)) {
            pos = (pos + 1) % tableSize;
            current = keys[pos];
            if (current == null) {
                return -1;
            }
        }
        return pos;
    }

    protected abstract boolean equals(T a, T b);
    protected abstract int hash(T obj);

    private int findSpace(int hash) {
        int pos = (hash & 0x7FFFFFFF) % tableSize;
        while (keys[pos] != null) {
            pos = (pos + 1) % tableSize; 
        }
        return pos;
    }

    @Override
    public void write(T obj) {
        int historyPos = translateOldest(written);
        int oldestEntry = oldest[historyPos];
        if (oldestEntry != -1) {
            remove(oldestEntry);
        }
        final int hash = hash(obj);
        int pos = findSpace(hash);
        keys[pos] = obj;
        writtenAt[pos] = this.written++;
        hashes[pos] = hash;
        oldest[historyPos] = pos;
    }

    /**
     * Implements the remove entry algorithm from wikipedia, which is based on Knuth's remark.
     * The simple solution would be to use thombstone values, but this quickly degrades the lookup performance to
     * O(n) since the array is now one big chain.
     * 
     * This algorithm reconnects the chain after a hole has been made somewhere in it.
     * The biggest challenge is that the chain can actually be part of 2 seperate chains (due to clusters in the mod factor)
     * @param oldestEntry index of the entry to remove
     */
    private void remove(int oldestEntry) {
        int space = oldestEntry;
        final Object[] keys = this.keys;
        while (true) {
           int candidate = space;
           Object curr;
           while (true) {
               candidate = (candidate + 1) % tableSize;
               curr = keys[candidate];
               if (curr == null) {
                   keys[space] = null;
                   return;
               }
               int k = (hashes[candidate] & 0x7FFFFFFF) % tableSize;
               if (space <= candidate ? ((space >= k) || (k > candidate)) : ((space >= k) && (k > candidate))) {
                   break;
               }
           }
           keys[space] = curr;
           writtenAt[space] = writtenAt[candidate];
           hashes[space] = hashes[candidate];
           //assert oldest[translateOldest(writtenAt[space])] == candidate;
           oldest[translateOldest(writtenAt[space])] = space;
           space = candidate;
        }
    }

}
