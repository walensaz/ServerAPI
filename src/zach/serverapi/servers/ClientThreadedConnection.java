package zach.serverapi.servers;

import zach.serverapi.ClientConnection;
import zach.serverapi.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public class ClientThreadedConnection extends ClientConnection {

    private Future future;

    private boolean isTerminated;

    public ClientThreadedConnection(Socket socket, Future future) throws IOException {
        super(socket);
    }

    public ClientThreadedConnection(ClientConnection client, Future future) throws IOException {
        this(client.getSocket(), future);
    }

    public ClientThreadedConnection(ClientConnection client) throws IOException {
        this(client.getSocket(), null);
    }


    public Future getFuture() {
        return future;
    }

    protected void setFuture(Future future) {
        this.future = future;
    }

    protected void shutdown() {
        this.isTerminated = true;
        super.terminate();
        this.getFuture().cancel(true);
        onDisconnect(this);
    }

    public void onConnect(ClientConnection connection) {
        Logger.log("Accepted socket " + connection.getSocket().getInetAddress());
    }

    public void onDisconnect(ClientConnection connection) {
        Logger.log("Socket disconnected on address: " + connection.getSocket().getInetAddress());
    }

    public void onMessage(ClientThreadedConnection connection, String message) {
        Logger.log("From " + connection.getSocket().getInetAddress() + ": " + message);
    }

    protected boolean isTerminated() {
        return isTerminated;
    }

    protected void run() {
        if (isTerminated()) {
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

    public void createNew(Socket socket) throws IOException {
        super.setSocket(socket);
        super.setReader(socket.getInputStream());
        super.setWriter(socket.getOutputStream());
        super.setConnected(true);
        onConnect(this);
    }


}
