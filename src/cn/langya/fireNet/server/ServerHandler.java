package cn.langya.fireNet.server;

import cn.langya.fireNet.server.packet.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LangYa
 * @since 2024/12/7 16:01
 */
public class ServerHandler {
    private final int port;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private final PacketHandler packetHandler;
    private final int maxBytes;

    private final ConcurrentHashMap<SocketChannel, String> clients = new ConcurrentHashMap<>();

    public ServerHandler(int port, int maxBytes, PacketHandler packetHandler) {
        this.port = port;
        this.maxBytes = maxBytes;
        Packet.maxBytes = maxBytes;
        this.packetHandler = packetHandler;
    }

    public void start() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started on port " + port);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (key.isAcceptable()) {
                    acceptConnection();
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void acceptConnection() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        // 唯一标识aa
        String clientAddress = clientChannel.getRemoteAddress().toString();
        clients.put(clientChannel, clientAddress);

        System.out.println("Client connected: " + clientAddress);
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                Packet packet = Packet.decode(buffer);

                // 如果数据包解码失败 忽略且继续aa
                if (packet == null) {
                    System.err.println("Received invalid packet from " + clients.get(clientChannel));
                    return;
                }

                packetHandler.handle(packet);
                System.out.println("Received packet from " + clients.get(clientChannel) + ": ID=" + packet.getId());
            } else if (bytesRead == -1) {
                disconnectClient(clientChannel, key);
            }
        } catch (IOException e) {
            System.out.println("Error reading from client: " + clients.get(clientChannel));
            disconnectClient(clientChannel, key);
        }
    }

    private void disconnectClient(SocketChannel clientChannel, SelectionKey key) {
        try {
            String clientAddress = clients.remove(clientChannel);
            if (key != null) {
                key.cancel();
            }
            clientChannel.close();
            System.out.println("Client disconnected: " + clientAddress);
        } catch (IOException e) {
            System.err.println("Failed to disconnect client: " + clients.get(clientChannel) + " - " + e.getMessage());
        }
    }

    public void sendToClient(SocketChannel client, Packet packet) throws IOException {
        if (client.isConnected()) {
            ByteBuffer buffer = packet.encode();
            client.write(buffer);
            System.out.println("Sent packet to " + clients.get(client) + ": ID=" + packet.getId());
        }
    }

    public void broadcast(Packet packet) {
        for (SocketChannel client : clients.keySet()) {
            if (client.isConnected()) {
                try {
                    sendToClient(client, packet);
                } catch (IOException e) {
                    System.err.println("Failed to send packet to client: " + clients.get(client) + " - " + e.getMessage());
                }
            }
        }
        System.out.println("Broadcast packet: ID=" + packet.getId());
    }

    public void stop() throws IOException {
        for (SocketChannel client : clients.keySet()) {
            client.close();
        }

        clients.clear();

        if (serverChannel != null) serverChannel.close();
        if (selector != null) selector.close();
    }
}
