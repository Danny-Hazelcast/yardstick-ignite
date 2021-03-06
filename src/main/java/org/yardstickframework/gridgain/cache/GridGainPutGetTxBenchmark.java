/*
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.yardstickframework.gridgain.cache;

import org.gridgain.grid.cache.*;
import org.yardstickframework.gridgain.cache.model.*;

import java.util.*;

/**
 * GridGain benchmark that performs transactional put and get operations.
 */
public class GridGainPutGetTxBenchmark extends GridGainCacheAbstractBenchmark {
    /** {@inheritDoc} */
    @Override public boolean test(Map<Object, Object> ctx) throws Exception {
        int key = nextRandom(0, args.range() / 2);

        try (GridCacheTx tx = cache.txStart()) {
            Object val = cache.get(key);

            if (val != null)
                key = nextRandom(args.range() / 2, args.range());

            cache.putx(key, new SampleValue(key));

            tx.commit();
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override protected GridCache<Integer, Object> cache() {
        return grid().cache("tx");
    }
}
