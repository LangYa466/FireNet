package cn.langya.fireNet.examples;

import cn.langya.fireNet.server.ServerHandler;

import java.io.IOException;

/**
 * @author LangYa
 * @since 2024/12/7 18:07
 */
public class ServerExample {
    public static void main(String[] args) throws IOException {
        ServerHandler server = new ServerHandler(12345, packet -> System.out.printf("Received packet with ID: %s Data: %s%n",packet.getId(), packet.getDataWithString()));
        server.start();
    }
}
