// automatically generated, do not modify

package org.kyrillos.flattzdb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class ZoneRule extends Table {
  public static ZoneRule getRootAsZoneRule(ByteBuffer _bb) { return getRootAsZoneRule(_bb, new ZoneRule()); }
  public static ZoneRule getRootAsZoneRule(ByteBuffer _bb, ZoneRule obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public ZoneRule __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public DateTime from() { return from(new DateTime()); }
  public DateTime from(DateTime obj) { int o = __offset(6); return o != 0 ? obj.__init(o + bb_pos, bb) : null; }
  public boolean adjustForward() { int o = __offset(8); return o != 0 ? 0!=bb.get(o + bb_pos) : true; }
  public int timeDef() { int o = __offset(10); return o != 0 ? bb.get(o + bb_pos) & 0xFF : 0; }
  public int toYear() { int o = __offset(12); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public long save() { int o = __offset(14); return o != 0 ? (long)bb.getInt(o + bb_pos) & 0xFFFFFFFFL : 0; }
  public String text() { int o = __offset(16); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer textAsByteBuffer() { return __vector_as_bytebuffer(16, 1); }

  public static void startZoneRule(FlatBufferBuilder builder) { builder.startObject(7); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addFrom(FlatBufferBuilder builder, int fromOffset) { builder.addStruct(1, fromOffset, 0); }
  public static void addAdjustForward(FlatBufferBuilder builder, boolean adjustForward) { builder.addBoolean(2, adjustForward, true); }
  public static void addTimeDef(FlatBufferBuilder builder, int timeDef) { builder.addByte(3, (byte)(timeDef & 0xFF), 0); }
  public static void addToYear(FlatBufferBuilder builder, int toYear) { builder.addInt(4, toYear, 0); }
  public static void addSave(FlatBufferBuilder builder, long save) { builder.addInt(5, (int)(save & 0xFFFFFFFFL), 0); }
  public static void addText(FlatBufferBuilder builder, int textOffset) { builder.addOffset(6, textOffset, 0); }
  public static int endZoneRule(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 4);  // name
    builder.required(o, 6);  // from
    return o;
  }
};

