# FireNet🔥

FireNet🔥 是一个 基于 Java NIO 的 Socket 网络框架，它与传统的 Socket 有本质上的不同
- 项目仓库里面有客户端与服务端代码示例

## 特别鸣谢:
- [@曼珠沙華](https://github.com/ImFl0wow)
- [@Baier](https://github.com/baier233)

## 添加到你的项目
### [跳转到 Jitpack 云储存库](https://jitpack.io/#LangYa466/FireNet/-SNAPSHOT)

## 特点
- 高性能(Java NIO)
- 可扩展性高

## 用法
使用 `broadcast()` 向所有客户端广播消息：
```java
MessagePacket broadcastPacket = new MessagePacket("Hello, all clients!");
server.broadcast(broadcastPacket);
```

使用 `sendToClient()` 向指定客户端发送数据包：
```java
Packet directPacket = new Packet(2, "Hello, specific client!".getBytes());
server.sendToClient(clientChannel, directPacket);
```
