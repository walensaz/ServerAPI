package zach.serverapi.examples.FastHandlingMultiConnectionServer;

import zach.serverapi.ClientConnection;
import zach.serverapi.servers.ClientThreadedConnection;
import zach.serverapi.servers.FastMultiConnectionServer;

import java.io.IOException;

public class FastMultiServer extends FastMultiConnectionServer {

    public FastMultiServer(int port, int updateTimer, int maxConnections, ClientThreadedConnection myclient) throws IOException {
        super(port, updateTimer, maxConnections, myclient);
    }

    @Override
    public void onConnect(ClientConnection connection) {
        System.out.println("Connected this client to the server");
    }

    @Override
    public void onDisconnect(ClientConnection connection) {
        System.out.println("DisConnected this client to the server");
    }

    @Override
    public void onServerStart() {
        System.out.println("Started the server!");
    }
}
