package cn.langya.fireNet.server.packet;

/**
 * @author LangYa
 * @since 2024/12/7 17:58
 */
public interface PacketHandler {
    void handle(Packet packet);
}
