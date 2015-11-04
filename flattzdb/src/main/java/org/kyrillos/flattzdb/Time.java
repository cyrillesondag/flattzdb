// automatically generated, do not modify

package org.kyrillos.flattzdb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Time extends Struct {
  public Time __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int hours() { return bb.get(bb_pos + 0) & 0xFF; }
  public int minutes() { return bb.get(bb_pos + 1) & 0xFF; }
  public int seconds() { return bb.get(bb_pos + 2) & 0xFF; }

  public static int createTime(FlatBufferBuilder builder, int hours, int minutes, int seconds) {
    builder.prep(1, 3);
    builder.putByte((byte)(seconds & 0xFF));
    builder.putByte((byte)(minutes & 0xFF));
    builder.putByte((byte)(hours & 0xFF));
    return builder.offset();
  }
};

