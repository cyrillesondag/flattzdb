// automatically generated, do not modify

package org.kyrillos.flattzdb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class ZoneTimeWindow extends Table {
  public static ZoneTimeWindow getRootAsZoneTimeWindow(ByteBuffer _bb) { return getRootAsZoneTimeWindow(_bb, new ZoneTimeWindow()); }
  public static ZoneTimeWindow getRootAsZoneTimeWindow(ByteBuffer _bb, ZoneTimeWindow obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public ZoneTimeWindow __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int gmtOffset() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public ZoneRule rules(int j) { return rules(new ZoneRule(), j); }
  public ZoneRule rules(ZoneRule obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int rulesLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public int fixed() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String format() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer formatAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public DateTime until() { return until(new DateTime()); }
  public DateTime until(DateTime obj) { int o = __offset(12); return o != 0 ? obj.__init(o + bb_pos, bb) : null; }
  public int timeDef() { int o = __offset(14); return o != 0 ? bb.get(o + bb_pos) & 0xFF : 0; }
  public boolean adjustForward() { int o = __offset(16); return o != 0 ? 0!=bb.get(o + bb_pos) : true; }

  public static void startZoneTimeWindow(FlatBufferBuilder builder) { builder.startObject(7); }
  public static void addGmtOffset(FlatBufferBuilder builder, int gmtOffset) { builder.addInt(0, gmtOffset, 0); }
  public static void addRules(FlatBufferBuilder builder, int rulesOffset) { builder.addOffset(1, rulesOffset, 0); }
  public static int createRulesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startRulesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addFixed(FlatBufferBuilder builder, int fixed) { builder.addInt(2, fixed, 0); }
  public static void addFormat(FlatBufferBuilder builder, int formatOffset) { builder.addOffset(3, formatOffset, 0); }
  public static void addUntil(FlatBufferBuilder builder, int untilOffset) { builder.addStruct(4, untilOffset, 0); }
  public static void addTimeDef(FlatBufferBuilder builder, int timeDef) { builder.addByte(5, (byte)(timeDef & 0xFF), 0); }
  public static void addAdjustForward(FlatBufferBuilder builder, boolean adjustForward) { builder.addBoolean(6, adjustForward, true); }
  public static int endZoneTimeWindow(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 10);  // format
    return o;
  }
};

