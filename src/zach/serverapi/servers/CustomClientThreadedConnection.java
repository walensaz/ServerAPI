package zach.serverapi.servers;

import zach.serverapi.ClientConnection;
import zach.serverapi.Logger;
import zach.serverapi.servers.ClientThreadedConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public class CustomClientThreadedConnection extends ClientThreadedConnection {

    public CustomClientThreadedConnection(Socket socket, Future future) throws IOException {
        super(socket, future);
    }

    public CustomClientThreadedConnection(ClientConnection client, Future future) throws IOException {
        super(client, future);
    }

    public CustomClientThreadedConnection(ClientConnection client) throws IOException {
        super(client);
    }

    @Override
    public void onConnect(ClientConnection connection) {
        super.onConnect(connection);
    }

    @Override
    public void onDisconnect(ClientConnection connection) {
        super.onDisconnect(connection);
    }

    @Override
    public void onMessage(ClientThreadedConnection connection, String message) {
        super.onMessage(connection, message);
    }


    protected void run() {
        if (super.isTerminated()) {
            this.shutdown();
        } else {
            try {
                String tempLine = "";
                if ((tempLine = super.readLine()) != null) {
                    onMessage(this, tempLine);
                }
            } catch (IOException e) {
                if (e.getMessage().contains("reset")) {
                    this.shutdown();
                } else {
                    Logger.log("We have encountered an error in the updater!");
                }
            }
        }
    }

}
