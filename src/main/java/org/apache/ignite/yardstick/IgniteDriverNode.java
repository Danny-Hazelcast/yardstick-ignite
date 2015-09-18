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

package org.apache.ignite.yardstick;

import static org.apache.ignite.cache.CacheMemoryMode.OFFHEAP_VALUES;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.TransactionConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.yardstickframework.BenchmarkConfiguration;
import org.yardstickframework.BenchmarkUtils;

/**
 * Standalone Ignite node.
 */
public class IgniteDriverNode extends IgniteNode {
    /** Client mode. */
    private boolean clientMode;

    /** */
    public IgniteDriverNode(boolean clientMode) {
        this.clientMode = clientMode;
    }

    /** */
    public IgniteDriverNode(boolean clientMode, Ignite ignite) {
        this.clientMode = clientMode;
        this.ignite = ignite;
    }

    /** {@inheritDoc} */
    @Override
    public void start(BenchmarkConfiguration cfg) throws Exception {
        IgniteBenchmarkArguments args = new IgniteBenchmarkArguments();

        BenchmarkUtils.jcommander(cfg.commandLineArguments(), args, "<ignite-node>");

        IgniteConfiguration c = loadConfiguration(args.configuration());

        assert c != null;

        CacheConfiguration ccfg = null;

        for (CacheConfiguration cc : c.getCacheConfiguration()) {
            // Create cache only
            if (!cc.getName().equals(args.cacheName()))
                continue;

            // IgniteNode can not run in CLIENT_ONLY mode,
            // except the case when it's used inside
            // IgniteAbstractBenchmark.
            boolean cl = args.isClientOnly() && !args.isNearCache() && !clientMode ? false : args.isClientOnly();

            if (cl)
                c.setClientMode(true);

            cc.setWriteSynchronizationMode(args.syncMode());

            if (args.orderMode() != null)
                cc.setAtomicWriteOrderMode(args.orderMode());

            cc.setBackups(args.backups());

            if (args.restTcpPort() != 0) {
                ConnectorConfiguration ccc = new ConnectorConfiguration();

                ccc.setPort(args.restTcpPort());

                if (args.restTcpHost() != null)
                    ccc.setHost(args.restTcpHost());

                c.setConnectorConfiguration(ccc);
            }

            if (args.isOffHeap()) {
                if (args.isOffheapTiered()) {
                    cc.setOffHeapMaxMemory(args.offHeapTieredMaxMemorySize());
                } else {
                    cc.setOffHeapMaxMemory(0);
                }

                if (args.isOffheapValues()) {
                    cc.setMemoryMode(OFFHEAP_VALUES);
                } else {
                    LruEvictionPolicy lru = new LruEvictionPolicy(0);
                    lru.setMaxMemorySize((long) (args.offHeapTieredMaxMemorySize() * 0.95));
                    cc.setEvictionPolicy(lru);
                    cc.setSwapEnabled(false);
                }
            }

            cc.setReadThrough(args.isStoreEnabled());

            cc.setWriteThrough(args.isStoreEnabled());

            cc.setWriteBehindEnabled(args.isWriteBehind());

            ccfg = cc;

            break;
        }

        c.setCacheConfiguration(ccfg);

        TransactionConfiguration tc = c.getTransactionConfiguration();

        tc.setDefaultTxConcurrency(args.txConcurrency());
        tc.setDefaultTxIsolation(args.txIsolation());

        TcpCommunicationSpi commSpi = (TcpCommunicationSpi) c.getCommunicationSpi();

        if (commSpi == null)
            commSpi = new TcpCommunicationSpi();

        c.setCommunicationSpi(commSpi);

        ignite = Ignition.start(c);
    }
}
