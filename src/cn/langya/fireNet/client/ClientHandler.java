package cn.langya.fireNet.client;

import cn.langya.fireNet.server.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author LangYa
 * @since 2024/12/7 16:06
 */
public class ClientHandler {
    private final String host;
    private final int port;
    private SocketChannel channel;
    private boolean running = true;

    public ClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(host, port));
        while (!channel.finishConnect()) {
            // Waiting for connection
        }
        System.out.println("Connected to server");
        new Thread(this::listenForMessages).start();
    }

    public void send(Packet packet) throws IOException {
        if (channel != null && channel.isConnected()) {
            channel.write(packet.encode());
        }
    }

    private void listenForMessages() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (running) {
            try {
                buffer.clear();
                int bytesRead = channel.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    Packet packet = Packet.decode(buffer);
                    handleIncomingPacket(packet);
                } else if (bytesRead == -1) {
                    close();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                break;
            }
        }
    }

    private void handleIncomingPacket(Packet packet) {
        // message-packet
        if (packet.getId() == 1) {
            System.out.println("Received message packet: " + packet.getDataWithString());
        } else {
            System.out.println("Received packet: ID=" + packet.getId() + ", Content=" + packet.getDataWithString());
        }
    }

    public void close() throws IOException {
        running = false;
        if (channel != null) {
            channel.close();
        }
        System.out.println("Disconnected from server");
    }
}
