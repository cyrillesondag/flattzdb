// automatically generated, do not modify

package org.kyrillos.flattzdb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class DateTime extends Struct {
  public DateTime __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int year() { return bb.getInt(bb_pos + 0); }
  public int month() { return bb.get(bb_pos + 4) & 0xFF; }
  public int dayOfMonth() { return bb.get(bb_pos + 5) & 0xFF; }
  public long at() { return (long)bb.getInt(bb_pos + 8) & 0xFFFFFFFFL; }

  public static int createDateTime(FlatBufferBuilder builder, int year, int month, int dayOfMonth, long at) {
    builder.prep(4, 12);
    builder.putInt((int)(at & 0xFFFFFFFFL));
    builder.pad(2);
    builder.putByte((byte)(dayOfMonth & 0xFF));
    builder.putByte((byte)(month & 0xFF));
    builder.putInt(year);
    return builder.offset();
  }
};

