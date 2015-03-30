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

package org.apache.ignite.yardstick.cache.model;

import java.io.*;

/**
 * Entity class for benchmark.
 */
public class SampleValue implements Externalizable {
    /** */
    private int id;

    /** */
    public SampleValue() {
        // No-op.
    }

    /**
     * @param id Id.
     */
    public SampleValue(int id) {
        this.id = id;
    }

    /**
     * @param id Id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Id.
     */
    public int getId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readInt();
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Value [id=" + id + ']';
    }
}