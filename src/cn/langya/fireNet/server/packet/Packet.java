package cn.langya.fireNet.server.packet;

import java.nio.ByteBuffer;

/**
 * @author LangYa
 * @since 2024/12/7 17:58
 */
public class Packet {
    protected final int id;
    protected final byte[] data;

    public static int maxBytes = 256;

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
        try {
            int id = buffer.getInt();
            int length = buffer.getInt();

            // 低级错误 谢谢 @ImFl0wow 的指正
            // 本来写的 > 256 感谢 @baier233 的指正 ByteBuffer 的大小包含了数据包的 ID 和长度字段（共 8 字节）所以需要 ByteBuffer 最大大小 - 8
           if (length < 0 || length > 248) {
                System.err.println("Invalid packet length: " + length);
                return null;
            }

            byte[] data = new byte[length];
            buffer.get(data);
            return new Packet(id, data);
        } catch (Exception e) {
            System.err.println("Failed to decode packet: " + e.getMessage());
            return null;
        }
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
