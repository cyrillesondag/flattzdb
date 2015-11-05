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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.jdk8.Jdk8Methods;

/**
 * Class representing a linked set of zone lines in the TZDB file.
 */
final class TZDBTimeWindows implements FlatBuffersTable {

    @Nonnull
    static Builder builder() {
        return new Builder();
    }

    final ZoneOffset gmtOffset;
    final List<TZDBRule> rules;
    final LocalTime fixedTime;
    final String format;
    final LocalDateTime until;
    final int timeDefinition;
    //final boolean adjustForward;

    private TZDBTimeWindows(@Nullable LocalDateTime date, int timeDefinition, @Nonnull ZoneOffset gmtOffset, @Nullable List<TZDBRule> rules, @Nullable LocalTime fixedTime, @Nonnull String format) {
        this.until = date;
        this.timeDefinition = timeDefinition;
        this.gmtOffset = gmtOffset;
        this.rules = rules;
        this.fixedTime = fixedTime;
        this.format = format;
    }

    public int writeToFlatBuffer(FlatBufferBuilder builder, SerializationContext context) {
        int formatOff = context.resolveOffset(builder, format);
        Integer rulesOffs = null;
        if (rules != null) {
            int[] rulesOffArray = new int[rules.size()];
            for (int i = 0; i < rules.size(); i++) {
                rulesOffArray[i] = context.resolveOffset(builder, rules.get(i));
            }
            rulesOffs = context.resolveVectorOffset(TZDBRule.class, rulesOffArray);
            if (rulesOffs == null){
                rulesOffs = ZoneTimeWindow.createRulesVector(builder, rulesOffArray);
                context.storeVectorOffset(TZDBRule.class, rulesOffArray, rulesOffs);
            }
        }

        ZoneTimeWindow.startZoneTimeWindow(builder);
        ZoneTimeWindow.addGmtOffset(builder, gmtOffset.getTotalSeconds());
        if (rulesOffs != null) {
            ZoneTimeWindow.addRules(builder, rulesOffs);
        } else if (fixedTime != null) {
            ZoneTimeWindow.addFixed(builder, fixedTime.getHour() * 60 * 60 + fixedTime.getMinute() * 60 + fixedTime.getSecond());
        }
        ZoneTimeWindow.addFormat(builder, formatOff);
        if (until != null) {
            int dateTimeOff = DateTime.createDateTime(builder, until.getYear(), until.getMonthValue(), until.getDayOfMonth(), until.getHour() * 60 * 60 + until.getMinute() * 60 + until.getSecond());
            ZoneTimeWindow.addUntil(builder, dateTimeOff);
            ZoneTimeWindow.addTimeDef(builder, timeDefinition);
        }

        return ZoneTimeWindow.endZoneTimeWindow(builder);
    }


    public static class Builder implements MonthDayTimeBuilder {
        private LocalDateTime date;
        private int timeDefinition;
        private ZoneOffset gmtOffset;
        private List<TZDBRule> rules;
        private LocalTime fixedTime;
        private String format;

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

        public Builder setGmtOffset(ZoneOffset gmtOffset) {
            this.gmtOffset = gmtOffset;
            return this;
        }

        public Builder setRules(List<TZDBRule> rules) {
            this.rules = rules;
            return this;
        }

        public Builder setFixedTime(LocalTime fixedTime) {
            this.fixedTime = fixedTime;
            return this;
        }

        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        public TZDBTimeWindows build() {
            Jdk8Methods.requireNonNull(gmtOffset);
            Jdk8Methods.requireNonNull(format);
            return new TZDBTimeWindows(date, timeDefinition, gmtOffset, rules, fixedTime, format);
        }
    }
}
