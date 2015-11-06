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

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;
import org.threeten.bp.Year;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project : flattzdb-parent. Created by Cyrille Sondag on 06/11/2015.
 */
public class TestCompiler {

    private Compiler compiler;

    @Before
    public void before() {
        compiler = new Compiler("2015g", new ArrayList<File>(), false);
    }


    //-----------------------------------------------------------------------
    // parseYear()
    //-----------------------------------------------------------------------
    @Test
    public void test_parseYear_specific() throws Exception {
        assertEquals(Compiler.parseYear("2010", 2000), 2010);
    }

    @Test
    public void test_parseYear_min() throws Exception {
        assertEquals(Compiler.parseYear("min", 2000), Year.MIN_VALUE);
    }

    @Test
    public void test_parseYear_mini() throws Exception {
        assertEquals(Compiler.parseYear("mini", 2000), Year.MIN_VALUE);
    }

    @Test
    public void test_parseYear_minim() throws Exception {
        assertEquals(Compiler.parseYear("minim", 2000), Year.MIN_VALUE);
    }

    @Test
    public void test_parseYear_minimu() throws Exception {
        assertEquals(Compiler.parseYear("minimu", 2000), Year.MIN_VALUE);
    }

    @Test
    public void test_parseYear_minimum() throws Exception {
        assertEquals(Compiler.parseYear("minimum", 2000), Year.MIN_VALUE);
    }


    @Test(expected = NumberFormatException.class)
    public void test_parseYear_minTooShort() throws Exception {
        Compiler.parseYear("mi", 2000);
    }

    @Test(expected = NumberFormatException.class)
    public void test_parseYear_minTooLong() throws Exception {
        Compiler.parseYear("minimuma", 2000);
    }

    @Test
    public void test_parseYear_max() throws Exception {
        assertEquals(Compiler.parseYear("max", 2000), Year.MAX_VALUE);
    }

    @Test
    public void test_parseYear_maxi() throws Exception {
        assertEquals(Compiler.parseYear("maxi", 2000), Year.MAX_VALUE);
    }

    @Test
    public void test_parseYear_maxim() throws Exception {
        assertEquals(Compiler.parseYear("maxim", 2000), Year.MAX_VALUE);
    }

    @Test
    public void test_parseYear_maximu() throws Exception {
        assertEquals(Compiler.parseYear("maximu", 2000), Year.MAX_VALUE);
    }

    @Test
    public void test_parseYear_maximum() throws Exception {
        assertEquals(Compiler.parseYear("maximum", 2000), Year.MAX_VALUE);
    }

    @Test(expected = NumberFormatException.class)
    public void test_parseYear_maxTooShort() throws Exception {
        Compiler.parseYear("ma", 2000);
    }

    @Test(expected = NumberFormatException.class)
    public void test_parseYear_maxTooLong() throws Exception {
        Compiler.parseYear("maximuma", 2000);
    }

    @Test
    public void test_parseYear_only() throws Exception {
        assertEquals(Compiler.parseYear("only", 2000), 2000);
    }

    @Test
    public void test_parseYear_only_uppercase() throws Exception {
        assertEquals(Compiler.parseYear("ONLY", 2000), 2000);
    }

    @Test(expected = NumberFormatException.class)
    public void test_parseYear_invalidYear() throws Exception {
        Compiler.parseYear("ABC", 2000);
    }

    //-----------------------------------------------------------------------
    // parseMonth()
    //-----------------------------------------------------------------------
    @Test
    public void test_parseMonth() throws Exception {
        assertEquals(Compiler.parseMonth("Jan"), Month.JANUARY.getValue());
        assertEquals(Compiler.parseMonth("Feb"), Month.FEBRUARY.getValue());
        assertEquals(Compiler.parseMonth("Mar"), Month.MARCH.getValue());
        assertEquals(Compiler.parseMonth("Apr"), Month.APRIL.getValue());
        assertEquals(Compiler.parseMonth("May"), Month.MAY.getValue());
        assertEquals(Compiler.parseMonth("Jun"), Month.JUNE.getValue());
        assertEquals(Compiler.parseMonth("Jul"), Month.JULY.getValue());
        assertEquals(Compiler.parseMonth("Aug"), Month.AUGUST.getValue());
        assertEquals(Compiler.parseMonth("Sep"), Month.SEPTEMBER.getValue());
        assertEquals(Compiler.parseMonth("Oct"), Month.OCTOBER.getValue());
        assertEquals(Compiler.parseMonth("Nov"), Month.NOVEMBER.getValue());
        assertEquals(Compiler.parseMonth("Dec"), Month.DECEMBER.getValue());
        assertEquals(Compiler.parseMonth("January"), Month.JANUARY.getValue());
        assertEquals(Compiler.parseMonth("February"), Month.FEBRUARY.getValue());
        assertEquals(Compiler.parseMonth("March"), Month.MARCH.getValue());
        assertEquals(Compiler.parseMonth("April"), Month.APRIL.getValue());
        assertEquals(Compiler.parseMonth("May"), Month.MAY.getValue());
        assertEquals(Compiler.parseMonth("June"), Month.JUNE.getValue());
        assertEquals(Compiler.parseMonth("July"), Month.JULY.getValue());
        assertEquals(Compiler.parseMonth("August"), Month.AUGUST.getValue());
        assertEquals(Compiler.parseMonth("September"), Month.SEPTEMBER.getValue());
        assertEquals(Compiler.parseMonth("October"), Month.OCTOBER.getValue());
        assertEquals(Compiler.parseMonth("November"), Month.NOVEMBER.getValue());
        assertEquals(Compiler.parseMonth("December"), Month.DECEMBER.getValue());
        assertEquals(Compiler.parseMonth("Janu"), Month.JANUARY.getValue());
        assertEquals(Compiler.parseMonth("Janua"), Month.JANUARY.getValue());
        assertEquals(Compiler.parseMonth("Januar"), Month.JANUARY.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_parseMonth_invalidMonth() throws Exception {
        Compiler.parseMonth("ABC");
    }

    //-----------------------------------------------------------------------
    // parseDayOfWeek()
    //-----------------------------------------------------------------------
    @Test
    public void test_parseDayOfWeek() throws Exception {
        assertEquals(Compiler.parseDayOfWeek("Mon"), DayOfWeek.MONDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Tue"), DayOfWeek.TUESDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Wed"), DayOfWeek.WEDNESDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Thu"), DayOfWeek.THURSDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Fri"), DayOfWeek.FRIDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Sat"), DayOfWeek.SATURDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Sun"), DayOfWeek.SUNDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Monday"), DayOfWeek.MONDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Tuesday"), DayOfWeek.TUESDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Wednesday"), DayOfWeek.WEDNESDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Thursday"), DayOfWeek.THURSDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Friday"), DayOfWeek.FRIDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Saturday"), DayOfWeek.SATURDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Sunday"), DayOfWeek.SUNDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Mond"), DayOfWeek.MONDAY.getValue());
        assertEquals(Compiler.parseDayOfWeek("Monda"), DayOfWeek.MONDAY.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_parseDayOfWeek_invalidMonth() throws Exception {
        Compiler.parseMonth("ABC");
    }

    //-----------------------------------------------------------------------
    // parseMonthDayTime()
    //-----------------------------------------------------------------------

    @Test
    public void test_parseMonthDayTime_2015_novLastSun0220() throws Exception {
        MonthDayTimeBuilderMock mdt = parseMonthDayTime(2015, "Nov lastTue 2:20");
        assertNotNull(mdt.localDateTime);
        assertEquals(mdt.localDateTime.getYear(), 2015);
        assertEquals(mdt.localDateTime.getMonth(), Month.NOVEMBER);
        assertEquals(mdt.localDateTime.getDayOfWeek(), DayOfWeek.TUESDAY);
        assertEquals(mdt.localDateTime.getDayOfMonth(), 24);

        assertEquals(mdt.localDateTime.getHour(), 2);
        assertEquals(mdt.localDateTime.getMinute(), 20);
        assertEquals(mdt.localDateTime.getSecond(), 0);
        assertEquals(mdt.localDateTime.getNano(), 0);
        assertEquals(mdt.timeDefinition, TimeDef.WALL);
    }

    @Test
    public void test_parseMonthDayTime_2015_jun50220s() throws Exception {
        MonthDayTimeBuilderMock mdt = parseMonthDayTime(2015, "Jun 5 2:20s");
        assertNotNull(mdt.localDateTime);
        assertEquals(mdt.localDateTime.getYear(), 2015);
        assertEquals(mdt.localDateTime.getMonth(), Month.JUNE);
        assertEquals(mdt.localDateTime.getDayOfMonth(), 5);

        assertEquals(mdt.localDateTime.getHour(), 2);
        assertEquals(mdt.localDateTime.getMinute(), 20);
        assertEquals(mdt.localDateTime.getSecond(), 0);
        assertEquals(mdt.localDateTime.getNano(), 0);
        assertEquals(mdt.timeDefinition, TimeDef.STANDARD);
    }

    @Test
    public void test_parseMonthDayTime_2015_maySatAfter50220u() throws Exception {
        MonthDayTimeBuilderMock mdt = parseMonthDayTime(2015, "May Sat>=5 2:20u");
        assertNotNull(mdt.localDateTime);
        assertEquals(mdt.localDateTime.getYear(), 2015);
        assertEquals(mdt.localDateTime.getMonth(), Month.MAY);
        assertEquals(mdt.localDateTime.getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(mdt.localDateTime.getDayOfMonth(), 9);

        assertEquals(mdt.localDateTime.getHour(), 2);
        assertEquals(mdt.localDateTime.getMinute(), 20);
        assertEquals(mdt.localDateTime.getSecond(), 0);
        assertEquals(mdt.localDateTime.getNano(), 0);
        assertEquals(mdt.timeDefinition, TimeDef.UTC);
    }

    @Test
    public void test_parseMonthDayTime_2015_maySatBefore50220u() throws Exception {
        MonthDayTimeBuilderMock mdt = parseMonthDayTime(2015, "May Sat<=5 2:20g");
        assertNotNull(mdt.localDateTime);
        assertEquals(mdt.localDateTime.getYear(), 2015);
        assertEquals(mdt.localDateTime.getMonth(), Month.MAY);
        assertEquals(mdt.localDateTime.getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(mdt.localDateTime.getDayOfMonth(), 2);

        assertEquals(mdt.localDateTime.getHour(), 2);
        assertEquals(mdt.localDateTime.getMinute(), 20);
        assertEquals(mdt.localDateTime.getSecond(), 0);
        assertEquals(mdt.localDateTime.getNano(), 0);
        assertEquals(mdt.timeDefinition, TimeDef.UTC);
    }

    @Test
    public void test_parseMonthDayTime_2015_maySatBefore15WithoutTime() throws Exception {
        MonthDayTimeBuilderMock mdt = parseMonthDayTime(2015, "May Sat<=15");
        assertNotNull(mdt.localDateTime);
        assertEquals(mdt.localDateTime.getYear(), 2015);
        assertEquals(mdt.localDateTime.getMonth(), Month.MAY);
        assertEquals(mdt.localDateTime.getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(mdt.localDateTime.getDayOfMonth(), 9);

        assertEquals(mdt.localDateTime.getHour(), 0);
        assertEquals(mdt.localDateTime.getMinute(), 0);
        assertEquals(mdt.localDateTime.getSecond(), 0);
        assertEquals(mdt.localDateTime.getNano(), 0);
        assertEquals(mdt.timeDefinition, TimeDef.WALL);
    }

    @Test
    public void test_parseMonthDayTime_2015_mayLastSunEndOfDay() throws Exception {
        MonthDayTimeBuilderMock mdt = parseMonthDayTime(2015, "May lastSun 24:00z");
        assertNotNull(mdt.localDateTime);
        assertEquals(mdt.localDateTime.getYear(), 2015);
        assertEquals(mdt.localDateTime.getMonth(), Month.JUNE);
        assertEquals(mdt.localDateTime.getDayOfWeek(), DayOfWeek.MONDAY);
        assertEquals(mdt.localDateTime.getDayOfMonth(), 1);
        assertEquals(mdt.timeDefinition, TimeDef.UTC);
    }

    private MonthDayTimeBuilderMock parseMonthDayTime(int year, String str) throws Exception {
        MonthDayTimeBuilderMock monthDayTimeBuilderMock = new MonthDayTimeBuilderMock();
        compiler.parseMonthDayTime(monthDayTimeBuilderMock, year, new StringTokenizer(str));
        return monthDayTimeBuilderMock;
    }


    private static class MonthDayTimeBuilderMock implements MonthDayTimeBuilder {

        private LocalDateTime localDateTime;
        private int timeDefinition;

        @Override
        public MonthDayTimeBuilder setDate(LocalDateTime date) {
            this.localDateTime = date;
            return this;
        }

        @Override
        public MonthDayTimeBuilder setTimeDefinition(int timeDefinition) {
            this.timeDefinition = timeDefinition;
            return this;
        }
    }


}
