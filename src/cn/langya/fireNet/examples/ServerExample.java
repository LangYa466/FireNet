package cn.langya.fireNet.examples;

import cn.langya.fireNet.server.ServerHandler;

import java.io.IOException;

/**
 * @author LangYa
 * @since 2024/12/7 18:07
 */
public class ServerExample {
    public static void main(String[] args) throws IOException {
        final ServerHandler[] serverHolder = new ServerHandler[1];
        // 根据具体业务要求可以改maxBytes 在 UTF-8 编码中，一个英文字为一个字节，一个中文为三个字节1
        serverHolder[0] = new ServerHandler(12345,512, packet -> {
            System.out.printf("Received packet with ID: %s Data: %s%n", packet.getId(), packet.getDataWithString());
            // message-packet广播 根据具体业务要求可以改aa
            if (packet.getId() == 1) serverHolder[0].broadcast(packet);
        });

        serverHolder[0].start();
    }
}
