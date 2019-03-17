package zach.serverapi.examples.FastHandlingMultiConnectionServer;

import zach.serverapi.ClientConnection;
import zach.serverapi.servers.ClientThreadedConnection;
import zach.serverapi.servers.CustomClientThreadedConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public class CustomClient extends CustomClientThreadedConnection {

    public CustomClient(Socket socket, Future future) throws IOException {
        super(socket, future);
    }


    @Override
    public void onConnect(ClientConnection connection) {
        System.out.println("Connected this client");
    }

    @Override
    public void onDisconnect(ClientConnection connection) {
        System.out.println("Disconnected this client");
    }

    @Override
    public void onMessage(ClientThreadedConnection connection, String message) {
        System.out.println("Received this message " + message);
    }
}
