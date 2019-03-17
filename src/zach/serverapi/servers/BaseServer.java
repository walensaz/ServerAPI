package zach.serverapi.servers;

import zach.serverapi.ClientConnection;
import zach.serverapi.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class BaseServer {

    private ArrayList<ClientConnection> connections;
    private ServerSocket serverSocket;
    private int port;

    public BaseServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.connections = new ArrayList<>();
    }

    protected ServerSocket getServerSocket() {
        return serverSocket;
    }

    protected int getPort() {
        return port;
    }

    protected ArrayList<ClientConnection> getConnections() {
        return connections;
    }

    public void onServerStart() {
        Logger.log("Starting Base Server on port " + this.getPort());
    }
}
