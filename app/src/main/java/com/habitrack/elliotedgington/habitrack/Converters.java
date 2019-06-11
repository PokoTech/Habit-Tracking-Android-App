package com.habitrack.elliotedgington.habitrack;

import java.util.Date;

import androidx.room.TypeConverter;

// Adapted from https://developer.android.com/training/data-storage/room/referencing-data
// Used as a converter from a complex Date object to a long and vice versa.
public class Converters {
    @TypeConverter
    public static Date longToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }

}
