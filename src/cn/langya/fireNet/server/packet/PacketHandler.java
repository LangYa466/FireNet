package cn.langya.fireNet.server.packet;

import java.io.IOException;

/**
 * @author LangYa
 * @since 2024/12/7 17:58
 */
public interface PacketHandler {
    void handle(Packet packet) throws IOException;
}
