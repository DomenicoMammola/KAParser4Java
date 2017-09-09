/*
 *  This is part of the KaParser4Java Library
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This software is distributed without any warranty.
 *
 * @author Domenico Mammola (mimmo71@gmail.com - www.mammola.net)
 *
 */
package it.mammola.kaparser;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class KADateTimeUtilities {
public static Double instantToDouble (final Instant value)
    {
        return (double)((int)Math.floorDiv(value.getEpochSecond(), 3600));
    }
    
    public static Instant doubleToInstant (final Double value)
    {
        return Instant.ofEpochSecond((long)Math.floor(value * 3600));
    }
    
    public static Integer getMonth(final Instant value, ZoneId zone){
        LocalDateTime td = LocalDateTime.ofInstant(value, zone);
        return td.getMonthValue();
    }
    
    public static Integer getYear(final Instant value, ZoneId zone){
        LocalDateTime td = LocalDateTime.ofInstant(value, zone);
        return td.getYear();
    }
    
    public static Integer getDayOfMonth(final Instant value, ZoneId zone){
        LocalDateTime td = LocalDateTime.ofInstant(value, zone);
        return td.getDayOfMonth();
    }
    
    public static Integer getHour(final Instant value, ZoneId zone){
        LocalDateTime td = LocalDateTime.ofInstant(value, zone);
        return td.getHour();
    }

    public static Integer getMinute(final Instant value, ZoneId zone){
        LocalDateTime td = LocalDateTime.ofInstant(value, zone);
        return td.getMinute();
    }
    
    public static Integer getSecond(final Instant value, ZoneId zone){
        LocalDateTime td = LocalDateTime.ofInstant(value, zone);
        return td.getSecond();
    }
    
    public static Instant today(){
        return Instant.now().truncatedTo(ChronoUnit.DAYS);
    }
    
    public static Instant previousMonday(final Instant value, ZoneId zone)
    {
        LocalDateTime td = LocalDateTime.ofInstant(value, zone).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        return td.toLocalDate().atStartOfDay(zone).toInstant();
    }

    public static Instant nextSunday(final Instant value, ZoneId zone)
    {
        LocalDateTime td = LocalDateTime.ofInstant(value, zone).with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        return td.toLocalDate().atStartOfDay(zone).toInstant();
    }
    
    public static Instant startOfTheMonth(final Instant value, ZoneId zone)
    {
        LocalDateTime td = LocalDateTime.ofInstant(value, zone).with(TemporalAdjusters.firstDayOfMonth());
        return td.toLocalDate().atStartOfDay(zone).toInstant();
    }

    public static Instant endOfTheMonth(final Instant value, ZoneId zone)
    {
        LocalDateTime td = LocalDateTime.ofInstant(value, zone).with(TemporalAdjusters.lastDayOfMonth());
        return td.toLocalDate().atStartOfDay(zone).toInstant();
    }    
}
