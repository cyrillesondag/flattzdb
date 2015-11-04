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
  public byte month() { return bb.get(bb_pos + 4); }
  public int dayOfMonth() { return bb.get(bb_pos + 5) & 0xFF; }
  public int hours() { return bb.get(bb_pos + 6) & 0xFF; }
  public int minutes() { return bb.get(bb_pos + 7) & 0xFF; }
  public int seconds() { return bb.get(bb_pos + 8) & 0xFF; }

  public static int createDateTime(FlatBufferBuilder builder, int year, byte month, int dayOfMonth, int hours, int minutes, int seconds) {
    builder.prep(4, 12);
    builder.pad(3);
    builder.putByte((byte)(seconds & 0xFF));
    builder.putByte((byte)(minutes & 0xFF));
    builder.putByte((byte)(hours & 0xFF));
    builder.putByte((byte)(dayOfMonth & 0xFF));
    builder.putByte(month);
    builder.putInt(year);
    return builder.offset();
  }
};

