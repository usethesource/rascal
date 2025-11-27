/** 
 * Copyright (c) 2018, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;
import org.rascalmpl.util.ConcurrentSoftReferenceObjectPool;

public class ObjectPoolTest {
    private static interface TestConcurrentAccess {
        Boolean tryRun();
    }

    @Test
    public void testObjectsAreOnlyReleasedOnce() throws InterruptedException, BrokenBarrierException {
        ConcurrentSoftReferenceObjectPool<TestConcurrentAccess> target = createExamplePool(100, 3, Integer.MAX_VALUE);
        final AtomicBoolean result = new AtomicBoolean(true);
        
        int numberOfThreads = 20;
        int numberOfTries = 2000;
        CyclicBarrier waitToStartRunning = new CyclicBarrier(numberOfThreads + 1); // start at the same time
        CyclicBarrier finishedRunning = new CyclicBarrier(numberOfThreads + 1); // signal finished
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    waitToStartRunning.await();
                    for (int j = 0; j < numberOfTries; j++) {
                        if (!target.useAndReturn(t -> t.tryRun())) {
                            result.set(false);
                        }
                    }
                    finishedRunning.await();
                }
                catch (RuntimeException e) {
                    result.set(false);
                }
                catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        waitToStartRunning.await();
        finishedRunning.await();
        assertTrue(result.get());
        assertTrue(target.healthCheck());
    }
    
    @Test
    public void testMaxIsRespected() throws InterruptedException, BrokenBarrierException {
        ConcurrentSoftReferenceObjectPool<TestConcurrentAccess> target = createExamplePool(100, 1, 5);

        int numberOfThreads = 20;
        int numberOfTries = 2000;
        CyclicBarrier waitToStartRunning = new CyclicBarrier(numberOfThreads + 1); // start at the same time
        CyclicBarrier finishedRunning = new CyclicBarrier(numberOfThreads + 1); // signal finished
        
        Set<Integer> instancesSeen = new HashSet<>();
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    waitToStartRunning.await();
                    for (int j = 0; j < numberOfTries; j++) {
                        target.useAndReturn(f -> instancesSeen.add(f.hashCode()));
                    }
                    finishedRunning.await();
                }
                catch (RuntimeException | InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        waitToStartRunning.await();
        finishedRunning.await();
        assertTrue(instancesSeen.size() >= 5);
        assertTrue(instancesSeen.size() < 8); // there can be a bit of a race on the condition of constructing two, so that is fine
    }

    @Test
    public void timeoutClears() throws InterruptedException  {
        ConcurrentSoftReferenceObjectPool<TestConcurrentAccess> target = createExamplePool(1, 0, 10);
        List<TestConcurrentAccess> leakInternal = new ArrayList<>();
        target.useAndReturn((t) -> { leakInternal.add(t); return t.tryRun(); } );
        Thread.sleep(10);
        target.useAndReturn((t) -> { leakInternal.add(t); return t.tryRun(); } );
        assertNotSame(leakInternal.get(0), leakInternal.get(1));
        assertTrue(target.healthCheck());
    }

    @Test
    public void keepAroundEvenAfterTimeout() throws InterruptedException, BrokenBarrierException  {
        ConcurrentSoftReferenceObjectPool<TestConcurrentAccess> target = createExamplePool(1, 2, 10);
        Set<TestConcurrentAccess> beforeSleep = collectInternalObjects(target, 4);
        Thread.sleep(10); // now there should be only 2 left
        Set<TestConcurrentAccess> afterSleep = collectInternalObjects(target, 4);
        
        int remaining = 0;
        for (TestConcurrentAccess s : afterSleep) {
            if (beforeSleep.contains(s)) {
                remaining++;
            }
        }
        
        assertEquals(2, remaining);
        assertTrue(target.healthCheck());
    }

    private <T> Set<T> collectInternalObjects(ConcurrentSoftReferenceObjectPool<T> target, int collect) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier waitToStartRunning = new CyclicBarrier(collect + 1); // start at the same time
        CyclicBarrier doneRunning = new CyclicBarrier(collect + 1); // start at the same time
        Queue<T> seen = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < collect; i++) {
            new Thread(() ->  {
                try {
                    waitToStartRunning.await();
                    target.useAndReturn((t) -> {
                        try {
                            seen.add(t);
                            Thread.sleep(100);// make sure the other threads get to the use before we return it again
                        }
                        catch (InterruptedException e) { }
                        return null;
                    });
                    doneRunning.await();
                }
                catch (InterruptedException | BrokenBarrierException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }).start();
        }
        waitToStartRunning.await();
        doneRunning.await();

        Set<T> result = new HashSet<>();
        result.addAll(seen);
        return result;
    }
    
    

    private ConcurrentSoftReferenceObjectPool<TestConcurrentAccess> createExamplePool(int milliSecondTimeouts, int keepAround, int maxAlive) {
        return new ConcurrentSoftReferenceObjectPool<>(milliSecondTimeouts, TimeUnit.MILLISECONDS, keepAround, maxAlive, () -> 
            new TestConcurrentAccess() {
                private final Random random = new Random();
                private volatile Thread currentThread;

                @Override
                public Boolean tryRun() {
                    try {
                        if (currentThread == null) {
                            currentThread = Thread.currentThread();
                            Thread.sleep(random.nextInt(4));
                            currentThread = null;
                            return true;
                        }
                        else {
                            Assert.fail("Runner was already used by another thread");
                        }
                    }
                    catch (InterruptedException e) {
                    }
                    return false;
                }
            }
        );
    }

}
