/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.yardstick.cache;

import java.util.Map;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.yardstick.cache.model.BigValue;
import org.apache.ignite.yardstick.cache.model.Person;
import org.yardstickframework.BenchmarkConfiguration;

import static org.yardstickframework.BenchmarkUtils.println;

/**
 * Ignite benchmark that performs put and get operations.
 */
public class IgniteOffHeapPutGetBenchmark extends IgniteCacheAbstractBenchmark {

    private static final int MAX_BYTES = 75_000;
    private static final byte[][] byteArrays = new byte[10][];

    @Override public void setUp(BenchmarkConfiguration cfg) throws Exception {
        super.setUp(cfg);

        println(cfg, "Populating put values (byteArrays)");

        for (int i = 0, s = MAX_BYTES; i < byteArrays.length; i++, s /= 2) {
            byteArrays[i] = new byte[s];
        }
    }


    /** {@inheritDoc} */
    @Override
    public boolean test(Map<Object, Object> ctx) throws Exception {
        int key = nextRandom(args.range());

        Object val = cache.get(key);

        if (val != null)
            key = nextRandom(args.range());

        cache.put(key, new BigValue(key, byteArrays[nextRandom(byteArrays.length)]));

        return true;
    }

    /** {@inheritDoc} */
    @Override protected IgniteCache<Integer, Object> cache() {
        return ignite().cache("offHeap");
    }

}
