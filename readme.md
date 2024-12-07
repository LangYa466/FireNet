# FireNet🔥

FireNet🔥 是一个基于 Java NIO 编写的简单网络框架。它的特点是能够管理多个客户端，支持向单个客户端发送消息或向所有客户端广播消息。框架设计简洁且易于扩展，适合用来构建各种高性能的网络应用。

## 特点
- **多客户端管理**：可以轻松管理多个客户端连接。
- **单客户端发送**：支持向单个客户端发送数据包。
- **广播功能**：支持向所有连接的客户端广播消息。
- **高性能**：基于 Java NIO 构建，性能优越，适合高并发应用。
- **易于扩展**：框架结构清晰，便于根据需求扩展功能。

## 使用说明

### 1. 启动服务器
在 `ServerExample` 类中，调用 `main()` 方法启动服务器：
```java
public class ServerExample {
    public static void main(String[] args) throws IOException {
        ServerHandler server = new ServerHandler(12345, packet -> System.out.printf("Received packet with ID: %s Data: %s%n",packet.getId(), packet.getDataWithString()));
        server.start();
    }
}
```

### 2. 广播消息
使用 `broadcast()` 向所有客户端广播消息：
```java
Packet broadcastPacket = new Packet(1, "Hello, all clients!".getBytes());
server.broadcast(broadcastPacket);
```

### 3. 发送给单个客户端
使用 `sendToClient()` 向指定客户端发送数据包：
```java
Packet directPacket = new Packet(2, "Hello, specific client!".getBytes());
server.sendToClient(clientChannel, directPacket);
```

### 4.客户端断开连接
```java
server.setClientDisconnectHandler(clientChannel -> {
        System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
        });
```

### 5.异常处理
```java
server.setExceptionHandler(exception -> {
        System.err.println("An error occurred: " + exception.getMessage());
        });
```