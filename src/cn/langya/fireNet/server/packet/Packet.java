package cn.langya.fireNet.server.packet;

import java.nio.ByteBuffer;

/**
 * @author LangYa
 * @since 2024/12/7 17:58
 */
public class Packet {
    private final int id;
    private final byte[] data;

    public Packet(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public String getDataWithString() {
        return new String(data);
    }

    public static Packet decode(ByteBuffer buffer) {
        int id = buffer.getInt();
        int length = buffer.getInt();
        byte[] data = new byte[length];
        buffer.get(data);
        return new Packet(id, data);
    }

    public ByteBuffer encode() {
        ByteBuffer buffer = ByteBuffer.allocate(8 + data.length);
        buffer.putInt(id);
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
