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
import java.time.ZoneOffset;
import java.util.List;
import org.threeten.bp.LocalTime;

/**
 * Class representing a linked set of zone lines in the TZDB file.
 */
final class TZDBZoneTimeWindows extends DateTimed implements FlatBuffersTable{

    ZoneOffset gmtOffset;
    List<TZDBRule> rules;
    LocalTime fixedTime;
    String format;

    public int writeToFlatBuffer(FlatBufferBuilder builder, SerializationContext context) {
        int formatOff = context.resolveOffset(builder, format);
        Integer rulesOffs = null;
        if (rules != null) {
            int[] rulesOffArray = new int[rules.size()];
            for (int i = 0; i < rules.size(); i++) {
                rulesOffArray[i] = context.resolveOffset(builder, rules.get(i));
            }
            rulesOffs = ZoneTimeWindow.createRulesVector(builder, rulesOffArray);
        }

        ZoneTimeWindow.startZoneTimeWindow(builder);
        ZoneTimeWindow.addGmtOffset(builder, gmtOffset.getTotalSeconds());
        if (rulesOffs != null) {
            ZoneTimeWindow.addRules(builder, rulesOffs);
        } else if (fixedTime != null) {
            ZoneTimeWindow.addFixed(builder, Time.createTime(builder, fixedTime.getHour(), fixedTime.getMinute(), fixedTime.getSecond()));
        }
        ZoneTimeWindow.addFormat(builder, formatOff);
        if (date != null) {
            int dateTimeOff = DateTime.createDateTime(builder, date.getYear(), (byte) date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
            ZoneTimeWindow.addUntil(builder, dateTimeOff);
            ZoneTimeWindow.addTimeDef(builder, timeDefinition);
        }
        return ZoneTimeWindow.endZoneTimeWindow(builder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TZDBZoneTimeWindows that = (TZDBZoneTimeWindows) o;

        if (gmtOffset != null ? !gmtOffset.equals(that.gmtOffset) : that.gmtOffset != null)
            return false;
        if (rules != null ? !rules.equals(that.rules) : that.rules != null) return false;
        if (fixedTime != null ? !fixedTime.equals(that.fixedTime) : that.fixedTime != null)
            return false;
        return !(format != null ? !format.equals(that.format) : that.format != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (gmtOffset != null ? gmtOffset.hashCode() : 0);
        result = 31 * result + (rules != null ? rules.hashCode() : 0);
        result = 31 * result + (fixedTime != null ? fixedTime.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        return result;
    }
}
