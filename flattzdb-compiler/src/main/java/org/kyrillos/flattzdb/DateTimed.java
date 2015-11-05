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

import org.threeten.bp.LocalDateTime;

/**
 * Project : flattzdb-parent.
 * Created by Cyrille Sondag on 04/11/2015.
 */
abstract class DateTimed {

    protected final LocalDateTime date;
    protected final int timeDefinition;

    DateTimed(LocalDateTime date, int timeDefinition) {
        this.date = date;
        this.timeDefinition = timeDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateTimed dateTimed = (DateTimed) o;

        if (timeDefinition != dateTimed.timeDefinition) return false;
        return !(date != null ? !date.equals(dateTimed.date) : dateTimed.date != null);

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + timeDefinition;
        return result;
    }
}
