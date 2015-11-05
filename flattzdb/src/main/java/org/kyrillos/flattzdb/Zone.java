// automatically generated, do not modify

package org.kyrillos.flattzdb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Zone extends Table {
  public static Zone getRootAsZone(ByteBuffer _bb) { return getRootAsZone(_bb, new Zone()); }
  public static Zone getRootAsZone(ByteBuffer _bb, Zone obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public Zone __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ZoneTimeWindow timeWindows(int j) { return timeWindows(new ZoneTimeWindow(), j); }
  public ZoneTimeWindow timeWindows(ZoneTimeWindow obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int timeWindowsLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createZone(FlatBufferBuilder builder,
      int name,
      int timeWindows) {
    builder.startObject(2);
    Zone.addTimeWindows(builder, timeWindows);
    Zone.addName(builder, name);
    return Zone.endZone(builder);
  }

  public static void startZone(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addTimeWindows(FlatBufferBuilder builder, int timeWindowsOffset) { builder.addOffset(1, timeWindowsOffset, 0); }
  public static int createTimeWindowsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startTimeWindowsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endZone(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 4);  // name
    builder.required(o, 6);  // timeWindows
    return o;
  }
};

