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

/**
 * Project : flattzdb-parent. Created by Cyrille Sondag on 04/11/2015.
 */
final class TZDBZone implements Cloneable, FlatBuffersTable {

    String name;
    String version;
    boolean alias;

    List<TZDBZoneTimeWindows> timeWindowses = new ArrayList<>();

    @Override
    protected TZDBZone clone() throws CloneNotSupportedException {
        final TZDBZone clone = (TZDBZone) super.clone();
        clone.name = name;
        clone.version = version;
        clone.alias = alias;
        clone.timeWindowses = new ArrayList<>(timeWindowses);
        return clone;
    }

    public int writeToFlatBuffer(FlatBufferBuilder builder, SerializationContext context) {
        int versionOf = context.resolveOffset(builder, version);
        int[] zoneTimeWindowsArray = new int[timeWindowses.size()];
        for (int i = 0; i < zoneTimeWindowsArray.length; i++) {
            zoneTimeWindowsArray[i] = context.resolveOffset(builder, timeWindowses.get(i));
        }
        int timeZoneOf = Zone.createTimeZonesVector(builder, zoneTimeWindowsArray);
        int zoneIdOf = context.resolveOffset(builder, name);

        Zone.startZone(builder);
        Zone.addName(builder, zoneIdOf);
        Zone.addVersion(builder, versionOf);
        Zone.addAlias(builder, false);
        Zone.addTimeZones(builder, timeZoneOf);
        return Zone.endZone(builder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TZDBZone tzdbZone = (TZDBZone) o;

        if (alias != tzdbZone.alias) return false;
        if (name != null ? !name.equals(tzdbZone.name) : tzdbZone.name != null) return false;
        if (version != null ? !version.equals(tzdbZone.version) : tzdbZone.version != null)
            return false;
        if (timeWindowses != null ? !timeWindowses.equals(tzdbZone.timeWindowses) : tzdbZone.timeWindowses != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (alias ? 1 : 0);
        result = 31 * result + (timeWindowses != null ? timeWindowses.hashCode() : 0);
        return result;
    }
}
