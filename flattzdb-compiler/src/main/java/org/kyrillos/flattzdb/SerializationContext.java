/*
 * Copyright 2015 Cyrille Sondag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kyrillos.flattzdb;

import com.google.flatbuffers.FlatBufferBuilder;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Project : flattzdb-parent. Created by Cyrille Sondag on 04/11/2015.
 */
final class SerializationContext {

    private final HashMap<Object, Integer> context = new HashMap<>();


    public int resolveOffset(FlatBufferBuilder builder, Object o){
        Integer offset = resolveOptionalOffset(builder, o);
        if(offset == null){
            throw new IllegalStateException();
        }
        return offset;
    }

    public Integer resolveOptionalOffset(FlatBufferBuilder builder, Object o){
        if (o == null){
            return null;
        }
        if (context.containsKey(o)){
            return context.get(o);
        }
        if (o instanceof String){
            context.put(o , builder.createString((String) o));
        } else if (o instanceof FlatBuffersTable){
            context.put(o, ((FlatBuffersTable) o).writeToFlatBuffer(builder, this));
        }
        return context.get(o);
    }

    public Integer resolveVectorOffset(Class clazz, int[] vectorOffsets){
        return context.get(new VectorKey(clazz, vectorOffsets));
    }

    public void storeVectorOffset(Class clazz, int[] vectorOffsets, int offset){
        context.put(new VectorKey(clazz, vectorOffsets), offset);
    }

    private final class VectorKey {

        private final Class vectorClass;
        private final int[] vectorOffsets;

        private VectorKey(Class vectorClass, int[] vectorOffsets) {
            this.vectorClass = vectorClass;
            this.vectorOffsets = vectorOffsets;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VectorKey vectorKey = (VectorKey) o;

            if (!vectorClass.equals(vectorKey.vectorClass)) return false;
            return Arrays.equals(vectorOffsets, vectorKey.vectorOffsets);

        }

        @Override
        public int hashCode() {
            int result = vectorClass.hashCode();
            result = 31 * result + Arrays.hashCode(vectorOffsets);
            return result;
        }
    }
}
