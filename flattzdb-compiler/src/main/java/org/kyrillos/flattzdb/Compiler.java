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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.Year;
import org.threeten.bp.temporal.TemporalAdjusters;

/**
 * Project : flattzdb-parent. Created by Cyrille Sondag on 03/11/2015.
 */
public class Compiler {

    /**
     * Reads a set of TZDB files and builds a single combined data file.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            outputHelp();
            return;
        }

        // parse args
        String version = null;
        File baseSrcDir = null;
        File dstDir = null;
        boolean unpacked = false;
        boolean verbose = false;

        // parse options
        int i;
        for (i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-") == false) {
                break;
            }
            if ("-srcdir".equals(arg)) {
                if (baseSrcDir == null && ++i < args.length) {
                    baseSrcDir = new File(args[i]);
                    continue;
                }
            } else if ("-dstdir".equals(arg)) {
                if (dstDir == null && ++i < args.length) {
                    dstDir = new File(args[i]);
                    continue;
                }
            } else if ("-version".equals(arg)) {
                if (version == null && ++i < args.length) {
                    version = args[i];
                    continue;
                }
            } else if ("-unpacked".equals(arg)) {
                if (unpacked == false) {
                    unpacked = true;
                    continue;
                }
            } else if ("-verbose".equals(arg)) {
                if (verbose == false) {
                    verbose = true;
                    continue;
                }
            } else if ("-help".equals(arg) == false) {
                System.out.println("Unrecognised option: " + arg);
            }
            outputHelp();
            return;
        }

        // check source directory
        if (baseSrcDir == null) {
            System.out.println("Source directory must be specified using -srcdir: " + baseSrcDir);
            return;
        }
        if (baseSrcDir.isDirectory() == false) {
            System.out.println("Source does not exist or is not a directory: " + baseSrcDir);
            return;
        }
        dstDir = (dstDir != null ? dstDir : baseSrcDir);

        // parse source file names
        List<String> srcFileNames = Arrays.asList(Arrays.copyOfRange(args, i, args.length));
        if (srcFileNames.isEmpty()) {
            System.out.println("Source filenames not specified, using default set");
            System.out.println("(africa antarctica asia australasia backward etcetera europe northamerica southamerica)");
            srcFileNames = Arrays.asList("africa", "antarctica", "asia", "australasia", "backward",
                    "etcetera", "europe", "northamerica", "southamerica");
        }

        // find source directories to process
        List<File> srcDirs = new ArrayList<File>();
        if (version != null) {
            File srcDir = new File(baseSrcDir, version);
            if (srcDir.isDirectory() == false) {
                System.out.println("Version does not represent a valid source directory : " + srcDir);
                return;
            }
            srcDirs.add(srcDir);
        } else {
            File[] dirs = baseSrcDir.listFiles();
            for (File dir : dirs) {
                if (dir.isDirectory() && dir.getName().matches("[12][0-9][0-9][0-9][A-Za-z0-9._-]+")) {
                    srcDirs.add(dir);
                }
            }
        }
        if (srcDirs.isEmpty()) {
            System.out.println("Source directory contains no valid source folders: " + baseSrcDir);
            return;
        }

        // check destination directory
        if (dstDir.exists() == false && dstDir.mkdirs() == false) {
            System.out.println("Destination directory could not be created: " + dstDir);
            return;
        }
        if (dstDir.isDirectory() == false) {
            System.out.println("Destination is not a directory: " + dstDir);
            return;
        }
        process(srcDirs, srcFileNames, dstDir, unpacked, verbose);
    }

    /**
     * Output usage text for the command line.
     */
    private static void outputHelp() {
        System.out.println("Usage: TzdbZoneRulesCompiler <options> <tzdb source filenames>");
        System.out.println("where options include:");
        System.out.println("   -srcdir <directory>   Where to find source directories (required)");
        System.out.println("   -dstdir <directory>   Where to output generated files (default srcdir)");
        System.out.println("   -version <version>    Specify the version, such as 2009a (optional)");
        System.out.println("   -unpacked             Generate dat files without jar files");
        System.out.println("   -help                 Print this usage message");
        System.out.println("   -verbose              Output verbose information during compilation");
        System.out.println(" There must be one directory for each version in srcdir");
        System.out.println(" Each directory must have the name of the version, such as 2009a");
        System.out.println(" Each directory must contain the unpacked tzdb files, such as asia or europe");
        System.out.println(" Directories must match the regex [12][0-9][0-9][0-9][A-Za-z0-9._-]+");
        System.out.println(" There will be one jar file for each version and one combined jar in dstdir");
        System.out.println(" If the version is specified, only that version is processed");
    }

    /**
     * Process to create the jar files.
     */
    private static void process(List<File> srcDirs, List<String> srcFileNames, File dstDir, boolean unpacked, boolean verbose) {
        // build actual jar files
        for (File srcDir : srcDirs) {
            // source files in this directory
            List<File> srcFiles = new ArrayList<File>();
            for (String srcFileName : srcFileNames) {
                File file = new File(srcDir, srcFileName);
                if (file.exists()) {
                    srcFiles.add(file);
                }
            }
            if (srcFiles.isEmpty()) {
                continue;  // nothing to process
            }
            // compile
            String loopVersion = srcDir.getName();
            Compiler compiler = new Compiler(loopVersion, srcFiles, verbose);

            try {
                // compile
                compiler.compile();

                // Write flat file
                List<TZDBZone> zones = compiler.getZones();
                FlatBufferBuilder builder = new FlatBufferBuilder(1024 * 512);
                SerializationContext context = new SerializationContext();
                int[] builtZones = new int[zones.size()];
                for (int i = 0; i < builtZones.length; i++) {
                    builtZones[i] = zones.get(i).writeToFlatBuffer(builder, context);
                }
                int zonesVector = Tzdb.createZonesVector(builder, builtZones);
                int versionOf = builder.createString(loopVersion);

                Tzdb.startTzdb(builder);
                Tzdb.addZones(builder, zonesVector);
                Tzdb.addVersion(builder, versionOf);
                int offset = Tzdb.endTzdb(builder);
                Tzdb.finishTzdbBuffer(builder, offset);

                byte[] bytes = builder.sizedByteArray();
                FileUtils.writeByteArrayToFile(new File(dstDir, loopVersion), bytes);

            } catch (Exception ex) {
                System.out.println("Failed: " + ex.toString());
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    //-----------------------------------------------------------------------
    /** The TZDB rules. */
    private final Map<String, List<TZDBRule>> rules = new TreeMap<>();
    /** The TZDB zones. */
    private final Map<String, TZDBZone> zones = new TreeMap<>();
    /** The TZDB links. */
    private final Map<String, String> links = new TreeMap<String, String>();
    /** The version to produce. */
    private final String version;
    /** The source files. */
    private final List<File> sourceFiles;
    /** The version to produce. */
    private final boolean verbose;

    /**
     * Creates an instance if you want to invoke the compiler manually.
     *
     * @param version the version, such as 2009a, not null
     * @param sourceFiles the list of source files, not empty, not null
     * @param verbose whether to output verbose messages
     */
    public Compiler(String version, List<File> sourceFiles, boolean verbose) {
        this.version = version;
        this.sourceFiles = sourceFiles;
        this.verbose = verbose;
    }

    /**
     * Compile the rules file. <p> Use {@link #getZones()} to retrieve the parsed data.
     *
     * @throws Exception if an error occurs
     */
    public void compile() throws Exception {
        printVerbose("Compiling TZDB version " + version);
        parseFiles();
        buildZoneAlias();
        printVerbose("Compiled TZDB version " + version);
    }

    /**
     * Gets computed zones.
     *
     * @return zones.
     */
    public ArrayList<TZDBZone> getZones() {
        return new ArrayList<>(zones.values());
    }

    //-----------------------------------------------------------------------

    /**
     * Parses the source files.
     *
     * @throws Exception if an error occurs
     */
    private void parseFiles() throws Exception {
        for (File file : sourceFiles) {
            printVerbose("Parsing file: " + file);
            parseFile(file);
        }
    }

    /**
     * Parses a source file.
     *
     * @param file the file being read, not null
     * @throws Exception if an error occurs
     */
    private void parseFile(File file) throws Exception {
        int lineNumber = 1;
        String line = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            TZDBZone.Builder openZoneBuilder = null;
            for (; (line = in.readLine()) != null; lineNumber++) {
                int index = line.indexOf('#');  // remove comments (doesn't handle # in quotes)
                if (index >= 0) {
                    line = line.substring(0, index);
                }
                if (line.trim().length() == 0) {  // ignore blank lines
                    continue;
                }
                StringTokenizer st = new StringTokenizer(line, " \t");
                if (openZoneBuilder != null && Character.isWhitespace(line.charAt(0)) && st.hasMoreTokens()) {
                    if (parseZoneLine(st, openZoneBuilder)) {
                        TZDBZone zone = openZoneBuilder.build();
                        zones.put(zone.name, zone);
                        openZoneBuilder = null;
                    }
                } else {
                    if (st.hasMoreTokens()) {
                        String first = st.nextToken();
                        if (first.equals("Zone")) {
                            if (st.countTokens() < 3) {
                                printVerbose("Invalid Zone line in file: " + file + ", line: " + line);
                                throw new IllegalArgumentException("Invalid Zone line");
                            }

                            openZoneBuilder = TZDBZone.builder().setName(st.nextToken());
                            if (parseZoneLine(st, openZoneBuilder)) {
                                TZDBZone zone = openZoneBuilder.build();
                                zones.put(zone.name, zone);
                                openZoneBuilder = null;
                            }
                        } else {
                            if (openZoneBuilder != null) {
                                TZDBZone zone = openZoneBuilder.build();
                                zones.put(zone.name, zone);
                                openZoneBuilder = null;
                            }
                            if (first.equals("Rule")) {
                                if (st.countTokens() < 9) {
                                    printVerbose("Invalid Rule line in file: " + file + ", line: " + line);
                                    throw new IllegalArgumentException("Invalid Rule line");
                                }
                                parseRuleLine(st);

                            } else if (first.equals("Link")) {
                                if (st.countTokens() < 2) {
                                    printVerbose("Invalid Link line in file: " + file + ", line: " + line);
                                    throw new IllegalArgumentException("Invalid Link line");
                                }
                                String realId = st.nextToken();
                                String aliasId = st.nextToken();
                                links.put(aliasId, realId);

                            } else {
                                throw new IllegalArgumentException("Unknown line");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new Exception("Failed while processing file '" + file + "' on line " + lineNumber + " '" + line + "'", ex);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Parses a Rule line.
     *
     * @param st the tokenizer, not null
     */
    void parseRuleLine(StringTokenizer st) {
        final String name = st.nextToken();
        if (!rules.containsKey(name)) {
            rules.put(name, new ArrayList<TZDBRule>());
        }

        final TZDBRule.Builder builder = TZDBRule.builder();
        builder.setName(name);
        final int startYear = parseInt(st.nextToken());
        builder.setEndYear(parseYear(st.nextToken(), startYear));
        parseOptional(st.nextToken()); //ignore type.
        parseMonthDayTime(builder, startYear, st);
        builder.setSave(parseLocalTime(st.nextToken()));
        builder.setText(parseOptional(st.nextToken()));
        rules.get(name).add(builder.build());
    }

    /**
     * Parses a Zone line.
     *
     * @param st the tokenizer, not null
     * @return true if the zone is complete
     */
    boolean parseZoneLine(StringTokenizer st, TZDBZone.Builder zoneBuilder) {
        final TZDBTimeWindows.Builder builder = TZDBTimeWindows.builder();
        builder.setGmtOffset(parseOffsetTime(st.nextToken()));
        final String savingsRule = parseOptional(st.nextToken());
        if (savingsRule != null) {
            if (savingsRule.matches("[0-9]{1,2}:[0-9]{2}(:[0-9]{2})?")) {
                builder.setFixedTime(parseLocalTime(savingsRule));
            } else {
                builder.setRules(this.rules.get(savingsRule));
            }
        }
        builder.setFormat(st.nextToken());
        boolean isLast = !st.hasMoreTokens();
        if (!isLast) {
            int year = parseInt(st.nextToken());
            if(st.hasMoreTokens()) {
                parseMonthDayTime(builder, year, st);
            }
        }
        zoneBuilder.addTimeWindows(builder.build());
        return isLast;
    }

    /**
     * Parse month and time constraints.
     *  @param object the date timed object to feed.
     * @param year the date year.
     * @param st the tokenizer, not null
     */
    void parseMonthDayTime(MonthDayTimeBuilder object, int year, StringTokenizer st) {
        int month = parseMonth(st.nextToken());
        int dayOfMonth = -1;
        int dayOfWeek = -1;
        boolean adjustForwards = true;
        if (st.hasMoreTokens()) {
            String dayRule = st.nextToken();
            if (dayRule.startsWith("last")) {
                dayOfWeek = parseDayOfWeek(dayRule.substring(4));
                adjustForwards = false;
            } else {
                int index = dayRule.indexOf(">=");
                if (index > 0) {
                    dayOfWeek = parseDayOfWeek(dayRule.substring(0, index));
                    dayRule = dayRule.substring(index + 2);
                } else {
                    index = dayRule.indexOf("<=");
                    if (index > 0) {
                        dayOfWeek = parseDayOfWeek(dayRule.substring(0, index));
                        adjustForwards = false;
                        dayRule = dayRule.substring(index + 2);
                    }
                }
                dayOfMonth = Integer.parseInt(dayRule);
            }
            LocalTime time = null;
            if (st.hasMoreTokens()) {
                String timeString = st.nextToken();
                time = parseLocalTime(timeString);
                object.setTimeDefinition(parseTimeDefinition(timeString));
            }
            LocalDateTime date = createLocalDateTime(year, month, dayOfMonth, dayOfWeek, adjustForwards, time);
            object.setDate(date);
        }
    }

    /**
     * Build the rules, zones and links into real zones.
     *
     * @throws Exception if an error occurs
     */
    private void buildZoneAlias() throws Exception {
        // build aliases
        for (Map.Entry<String, String> entry : links.entrySet()) {
            String aliasId = entry.getKey();
            String realId = entry.getValue();
            printVerbose("Linking alias " + aliasId + " to " + realId);
            TZDBZone origin = zones.get(realId);
            if (origin == null) {
                throw new IllegalStateException("Cound't find link " + realId + " for alias " + aliasId);
            }
            zones.put(aliasId, origin.createAlias(aliasId));
        }
    }


    //-----------------------------------------------------------------------

    /**
     * Prints a verbose message.
     *
     * @param message the message, not null
     */
    private void printVerbose(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Parse day of week from string.
     *
     * @param s input string (can be null).
     * @return the day of week index  or -1 if null of not found.
     */
    static int parseDayOfWeek(String s) {
        if (s != null) {
            String dow = s.toLowerCase();
            if (dow.length() > 3) {
                dow = dow.substring(0, 3);
            }
            if (matches(dow, "monday")) {
                return DayOfWeek.MONDAY.getValue();
            }
            if (matches(dow, "tuesday")) {
                return DayOfWeek.TUESDAY.getValue();
            }
            if (matches(dow, "wednesday")) {
                return DayOfWeek.WEDNESDAY.getValue();
            }
            if (matches(dow, "thursday")) {
                return DayOfWeek.THURSDAY.getValue();
            }
            if (matches(dow, "friday")) {
                return DayOfWeek.FRIDAY.getValue();
            }
            if (matches(dow, "saturday")) {
                return DayOfWeek.SATURDAY.getValue();
            }
            if (matches(dow, "sunday")) {
                return DayOfWeek.SUNDAY.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid month " + s);
    }

    /**
     * Parse month from string.
     *
     * @param s the input string (can be null).
     * @return the month index or -1 if null of not found.
     */
    static int parseMonth(String s) {
        if (s != null) {
            String dow = s.toLowerCase();
            if (matches(dow, "january")) {
                return Month.JANUARY.getValue();
            }
            if (matches(dow, "february")) {
                return Month.FEBRUARY.getValue();
            }
            if (matches(dow, "march")) {
                return Month.MARCH.getValue();
            }
            if (matches(dow, "april")) {
                return Month.APRIL.getValue();
            }
            if (matches(dow, "may")) {
                return Month.MAY.getValue();
            }
            if (matches(dow, "june")) {
                return Month.JUNE.getValue();
            }
            if (matches(dow, "july")) {
                return Month.JULY.getValue();
            }
            if (matches(dow, "august")) {
                return Month.AUGUST.getValue();
            }
            if (matches(dow, "september")) {
                return Month.SEPTEMBER.getValue();
            }
            if (matches(dow, "october")) {
                return Month.OCTOBER.getValue();
            }
            if (matches(dow, "november")) {
                return Month.NOVEMBER.getValue();
            }
            if (matches(dow, "december")) {
                return Month.DECEMBER.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid month " + s);
    }

    /**
     * Parse time definition.
     *
     * @param s the input string (can be null)
     * @return time definition (or default WALL time definition if missing)
     */
    int parseTimeDefinition(String s) {
        char c;
        if (s != null && Character.isAlphabetic(c = s.charAt(s.length() - 1))) {
            switch (Character.toLowerCase(c)) {
                case 's':
                    // standard time
                    return TimeDef.STANDARD;
                case 'u':
                case 'g':
                case 'z':
                    // UTC
                    return TimeDef.UTC;
            }
        }
        return TimeDef.WALL;
    }

    /**
     * Parse time from string.
     *
     * @param s the input string (can be null).
     * @return Local date time or LocalTime.MIDNIGHT is missing.
     */
    static LocalTime parseLocalTime(String s) {
        if (s != null) {
            String[] parts = s.split(":");
            int hours = parseInt(parts[0]);
            int minutes = 0;
            if (parts.length >= 2) {
                minutes = parseInt(parts[1].length() > 2 ? parts[1].substring(0, 2) : parts[1]);
            }
            int seconds = 0;
            if (parts.length == 3) {
                seconds = parseInt(parts[1].length() > 2 ? parts[2].substring(0, 2) : parts[2]);
            }
            int sec = hours * 60 * 60 + minutes * 60 + seconds;
            if (sec == 86400){
                sec -= 1;
            }
            return LocalTime.ofSecondOfDay(sec);
        }
        return LocalTime.MIDNIGHT;
    }

    /**
     * Parse time from string.
     *
     * @param s the input string (can be null).
     * @return Local date time or LocalTime.MIDNIGHT is missing.
     */
    static ZoneOffset parseOffsetTime(String s) {
        if (s == null) {
            return ZoneOffset.ofHours(0);
        }
        String[] parts = s.split(":");
        int hours = parseInt(parts[0]);
        int minutes =  parseInt(parts[1]);
        int seconds = parts.length == 3 ? parseInt(parts[2]) : 0;
        return ZoneOffset.ofHoursMinutesSeconds(hours, minutes,seconds);
    }

    /**
     * Parse integer from string.
     *
     * @param s the input string (can be null).
     * @return int.
     */
    private static int parseInt(String s) {
        return Integer.valueOf(s);
    }

    /**
     * Parse local date time. day will be shifted to day + 1 if local time == end of day (23:59:59)
     *
     * @param year the year.
     * @param month the month index.
     * @param dayOfMonth the day of month index.
     * @param dayOfWeek the day of week index
     * @param adjustForwards
     *@param localTime local time.  @return LocalDateTime
     */
    static LocalDateTime createLocalDateTime(int year, int month, int dayOfMonth, int dayOfWeek, boolean adjustForwards, LocalTime localTime) {
        LocalDate date;
        int dom = dayOfMonth;
        if (dom == -1) {
            dom = Month.of(month).length(Year.isLeap(year));
        }
        date = LocalDate.of(year, month, dom);
        if (dayOfWeek != -1) {
            DayOfWeek dow = DayOfWeek.of(dayOfWeek);
            date = date.with(adjustForwards ? TemporalAdjusters.nextOrSame(dow) : TemporalAdjusters.previousOrSame(dow));
        }
        LocalDateTime ldt = LocalDateTime.of(date, localTime != null ? localTime : LocalTime.MIDNIGHT);
        if (ldt.getHour() == 23 && ldt.getSecond() == 59 && ldt.getMinute() == 59) {
            ldt = ldt.plusDays(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);
        }
        return ldt;
    }

    /**
     * Checks if the 3 first characters match the searched string.
     *
     * @param s the input string (can be null).
     * @param search searched string.
     * @return true if match
     */
    private static boolean matches(String s, @Nonnull String search) {
        return s != null && s.startsWith(search.substring(0, 3)) && search.startsWith(s) && s.length() <= search.length();
    }

    /**
     * Parse year with min/max/only constants.
     *
     * @param s the input string (can be null).
     * @param defaultYear the only value.
     * @return year.
     */
    static int parseYear(String s, int defaultYear) {
        s = s.toLowerCase();
        if (matches(s, "minimum")) {
            return Year.MIN_VALUE;
        } else if (matches(s, "maximum")) {
            return Year.MAX_VALUE;
        } else if (s.equals("only")) {
            return defaultYear;
        }
        return Integer.parseInt(s);
    }

    /**
     * Parse optional string.
     *
     * @param s the input string (can be null).
     * @return null if string equal '-' or string value.
     */
    private static String parseOptional(String s) {
        return "-".equals(s) ? null : s;
    }
}
