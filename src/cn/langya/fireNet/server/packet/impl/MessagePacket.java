package cn.langya.fireNet.server.packet.impl;

import cn.langya.fireNet.server.packet.Packet;

import java.nio.charset.StandardCharsets;

/**
 * @author LangYa
 * @since 2024/12/9 09:58
 */
public class MessagePacket extends Packet {
    public MessagePacket(String message) {
        // 也可以不用中文 可以改aa
        super(1, message.getBytes(StandardCharsets.UTF_8));
    }
}
