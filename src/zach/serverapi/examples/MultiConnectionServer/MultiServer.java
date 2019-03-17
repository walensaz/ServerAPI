package zach.serverapi.examples.MultiConnectionServer;

import zach.serverapi.ClientConnection;
import zach.serverapi.servers.MultiConnectionServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiServer extends MultiConnectionServer {

    static List<ClientConnection> connections;

    public MultiServer(int port) throws IOException {
        super(port);
        connections = super.getConnections();
    }

    @Override
    public void onConnect(ClientConnection connection) {
        System.out.println(connection.getSocket().getInetAddress() + " has joined the server!");
        connections = super.getConnections(); //To update our connection list from the main server, you can also just call it.
    }

    @Override
    public void onDisconnect(ClientConnection connection) {
        System.out.println(connection.getSocket().getInetAddress() + " has disconnected from the server!");
    }

    @Override
    public void onMessage(ClientConnection connection, String message) {
        System.out.println("The connection " + connection.getSocket().getInetAddress() + " has sent " + message);
    }

    @Override
    public void onServerStart() {
        System.out.println("Server started up on port " + super.getServerSocket().getLocalPort());
    }
}
