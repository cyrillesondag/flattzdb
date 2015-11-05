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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Project : flattzdb-parent. Created by Cyrille Sondag on 04/11/2015.
 */
final class TZDBZone implements FlatBuffersTable {

    @Nonnull
    static Builder builder(){
        return new Builder();
    }

    final String name;
    final List<TZDBTimeWindows> timeWindows;

    private TZDBZone(@Nonnull String name, @Nonnull List<TZDBTimeWindows> timeWindows) {
        this.name = name;
        this.timeWindows = timeWindows;
    }

    public TZDBZone createAlias(@Nonnull String alias) {
        return new TZDBZone(alias, timeWindows);
    }

    public int writeToFlatBuffer(FlatBufferBuilder builder, SerializationContext context) {
        int[] zoneTimeWindowsArray = new int[timeWindows.size()];
        for (int i = 0; i < zoneTimeWindowsArray.length; i++) {
            zoneTimeWindowsArray[i] = context.resolveOffset(builder, timeWindows.get(i));
        }
        Integer timeWindowsVector = context.resolveVectorOffset(TZDBTimeWindows.class, zoneTimeWindowsArray);
        if (timeWindowsVector == null){
            timeWindowsVector = Zone.createTimeWindowsVector(builder, zoneTimeWindowsArray);
            context.storeVectorOffset(TZDBTimeWindows.class, zoneTimeWindowsArray, timeWindowsVector);
        }
        int zoneIdOf = context.resolveOffset(builder, name);

        Zone.startZone(builder);
        Zone.addName(builder, zoneIdOf);
        Zone.addTimeWindows(builder, timeWindowsVector);
        return Zone.endZone(builder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TZDBZone zone = (TZDBZone) o;

        return name.equals(zone.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static class Builder {
        private String name;
        private List<TZDBTimeWindows> timeWindowses = new ArrayList<>();

        private Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder addTimeWindows(TZDBTimeWindows timeWindows){
            this.timeWindowses.add(timeWindows);
            return this;
        }

        public TZDBZone build() {
            return new TZDBZone(name, timeWindowses);
        }
    }
}
