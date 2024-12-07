package cn.langya.fireNet.examples;

import cn.langya.fireNet.client.ClientHandler;
import cn.langya.fireNet.server.packet.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author LangYa
 * @since 2024/12/7 18:07
 */
public class ClientExample {
    public static void main(String[] args) throws IOException {
        ClientHandler client = new ClientHandler("127.0.0.1", 12345);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(System.in));
        client.connect();

        System.out.println("Enter message to send");
        String userInput;
        while ((userInput = stdInput.readLine()) != null) {
            Packet packet = new Packet(1, userInput.getBytes());
            client.send(packet);
            System.out.println("Send: " + userInput);
        }

        client.close();
    }
}
