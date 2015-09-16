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

package org.apache.ignite.yardstick.cache.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Entity class for benchmark.
 */
public class BigValue implements Externalizable {
    private int id;
    private byte[] bytes;

    public BigValue() {
        // No-op.
    }

    public BigValue(int id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    public int id() {
        return id;
    }

    public byte[] bytes() {
        return bytes;
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readInt();
        int len = in.readInt();
        this.bytes = new byte[len];
        in.readFully(this.bytes);
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeInt(this.bytes.length);
        out.write(this.bytes);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Value [id=" + id + ", bytes=" + bytes.length + "]";
    }
}
