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
  public ZoneTimeWindow timeZones(int j) { return timeZones(new ZoneTimeWindow(), j); }
  public ZoneTimeWindow timeZones(ZoneTimeWindow obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int timeZonesLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public String version() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer versionAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public boolean alias() { int o = __offset(10); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createZone(FlatBufferBuilder builder,
      int name,
      int timeZones,
      int version,
      boolean alias) {
    builder.startObject(4);
    Zone.addVersion(builder, version);
    Zone.addTimeZones(builder, timeZones);
    Zone.addName(builder, name);
    Zone.addAlias(builder, alias);
    return Zone.endZone(builder);
  }

  public static void startZone(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addTimeZones(FlatBufferBuilder builder, int timeZonesOffset) { builder.addOffset(1, timeZonesOffset, 0); }
  public static int createTimeZonesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startTimeZonesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addVersion(FlatBufferBuilder builder, int versionOffset) { builder.addOffset(2, versionOffset, 0); }
  public static void addAlias(FlatBufferBuilder builder, boolean alias) { builder.addBoolean(3, alias, false); }
  public static int endZone(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 4);  // name
    builder.required(o, 6);  // timeZones
    builder.required(o, 8);  // version
    return o;
  }
};

