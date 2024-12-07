package cn.langya.fireNet.client;

import cn.langya.fireNet.server.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author LangYa
 * @since 2024/12/7 16:06
 */
public class ClientHandler {
    private final String host;
    private final int port;
    private SocketChannel channel;

    public ClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(host, port));
        while (!channel.finishConnect()) {
            // TODO: Wait until connection is finished
        }
    }

    public void send(Packet packet) throws IOException {
        if (channel != null && channel.isConnected()) {
            channel.write(packet.encode());
        }
    }

    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }
}
