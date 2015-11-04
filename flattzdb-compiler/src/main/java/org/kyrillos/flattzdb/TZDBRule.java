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
import javax.annotation.Nullable;
import org.threeten.bp.LocalTime;

/**
 * Class representing a rule line in the TZDB file.
 */
final class TZDBRule extends DateTimed implements FlatBuffersTable {

    String name;
    int endYear;
    @Nullable
    String type;
    LocalTime save;
    @Nullable
    String text;

    public int writeToFlatBuffer(FlatBufferBuilder builder, SerializationContext context) {
        int nameOff = context.resolveOffset(builder, name);
        Integer typeOff = context.resolveOptionalOffset(builder, type);
        Integer textOff = context.resolveOptionalOffset(builder, text);

        ZoneRule.startZoneRule(builder);
        ZoneRule.addName(builder, nameOff);
        int dateTimeOff = DateTime.createDateTime(builder, date.getYear(), (byte) date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
        ZoneRule.addFrom(builder, dateTimeOff);
        ZoneRule.addTimeDef(builder, timeDefinition);

        ZoneRule.addToYear(builder, endYear);
        if (typeOff != null) {
            ZoneRule.addType(builder, typeOff);
        }

        int saveOff = Time.createTime(builder, (byte) save.getHour(), save.getMinute(), save.getSecond());
        ZoneRule.addSave(builder, saveOff);
        if (textOff != null) {
            ZoneRule.addText(builder, textOff);
        }

        return ZoneRule.endZoneRule(builder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TZDBRule rule = (TZDBRule) o;

        if (endYear != rule.endYear) return false;
        if (name != null ? !name.equals(rule.name) : rule.name != null) return false;
        if (type != null ? !type.equals(rule.type) : rule.type != null) return false;
        if (save != null ? !save.equals(rule.save) : rule.save != null) return false;
        return !(text != null ? !text.equals(rule.text) : rule.text != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + endYear;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (save != null ? save.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
