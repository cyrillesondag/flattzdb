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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.jdk8.Jdk8Methods;

/**
 * Class representing a rule line in the TZDB file.
 */
final class TZDBRule implements FlatBuffersTable {

    @Nonnull
    static Builder builder() {
        return new Builder();
    }

    final String name;
    final int endYear;
    final LocalTime save;
    final String text;
    final LocalDateTime until;
    final int timeDefinition;
    //final boolean adjustForward;

    private TZDBRule(@Nonnull LocalDateTime date, int timeDefinition, @Nonnull String name, int endYear, @Nonnull LocalTime save, @Nullable String text) {
        this.until = date;
        this.timeDefinition = timeDefinition;
        this.name = name;
        this.endYear = endYear;
        this.save = save;
        this.text = text;
    }

    public int writeToFlatBuffer(FlatBufferBuilder builder, SerializationContext context) {
        int nameOff = context.resolveOffset(builder, name);
        Integer textOff = context.resolveOptionalOffset(builder, text);

        ZoneRule.startZoneRule(builder);
        ZoneRule.addName(builder, nameOff);

        int dateTimeOff = DateTime.createDateTime(builder, until.getYear(), until.getMonthValue(), until.getDayOfMonth(), until.getHour() * 60 * 60 + until.getMinute() * 60 + until.getSecond());
        ZoneRule.addFrom(builder, dateTimeOff);
        ZoneRule.addTimeDef(builder, timeDefinition);

        ZoneRule.addToYear(builder, endYear);
        ZoneRule.addSave(builder, save.getHour() * 60 * 60 + save.getMinute() * 60 + save.getSecond());
        if (textOff != null) {
            ZoneRule.addText(builder, textOff);
        }
        return ZoneRule.endZoneRule(builder);
    }

    public static class Builder implements MonthDayTimeBuilder {
        private LocalDateTime date;
        private int timeDefinition;
        private String name;
        private int endYear;
        private LocalTime save;
        private String text;

        private Builder() {
        }

        @Override
        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        @Override
        public Builder setTimeDefinition(int timeDefinition) {
            this.timeDefinition = timeDefinition;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEndYear(int endYear) {
            this.endYear = endYear;
            return this;
        }

        public Builder setSave(LocalTime save) {
            this.save = save;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public TZDBRule build() {
            Jdk8Methods.requireNonNull(date, "date");
            Jdk8Methods.requireNonNull(name, "name");
            Jdk8Methods.requireNonNull(save, "save");
            if (date.getYear() > endYear) {
                throw new IllegalArgumentException("Year order invalid: " + date.getYear() + " > " + endYear);
            }

            return new TZDBRule(date, timeDefinition, name, endYear, save, text);
        }
    }
}
