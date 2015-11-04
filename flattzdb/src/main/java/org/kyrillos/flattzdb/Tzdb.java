// automatically generated, do not modify

package org.kyrillos.flattzdb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Tzdb extends Table {
  public static Tzdb getRootAsTzdb(ByteBuffer _bb) { return getRootAsTzdb(_bb, new Tzdb()); }
  public static Tzdb getRootAsTzdb(ByteBuffer _bb, Tzdb obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public static boolean TzdbBufferHasIdentifier(ByteBuffer _bb) { return __has_identifier(_bb, "TZDB"); }
  public Tzdb __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public Zone zones(int j) { return zones(new Zone(), j); }
  public Zone zones(Zone obj, int j) { int o = __offset(4); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int zonesLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createTzdb(FlatBufferBuilder builder,
      int zones) {
    builder.startObject(1);
    Tzdb.addZones(builder, zones);
    return Tzdb.endTzdb(builder);
  }

  public static void startTzdb(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addZones(FlatBufferBuilder builder, int zonesOffset) { builder.addOffset(0, zonesOffset, 0); }
  public static int createZonesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startZonesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endTzdb(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 4);  // zones
    return o;
  }
  public static void finishTzdbBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset, "TZDB"); }
};

