package zach.serverapi.servers;

import zach.serverapi.Logger;
import zach.serverapi.LoggerLevel;
import zach.serverapi.ClientConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleConnectionServer extends BaseServer {

    private final ScheduledExecutorService gameThreads = Executors.newScheduledThreadPool(1);


    private long timeOutMilis;
    private boolean currentlyOccupied;
    private int updateTimer;

    public SingleConnectionServer(int port) throws IOException {
        super(port);
        this.updateTimer = 200;
    }

    public SingleConnectionServer(int port, int updateTimer) throws IOException {
        super(port);
        this.updateTimer = updateTimer;
    }

    public ClientConnection getCurrentSocket() {
        return this.getConnections().get(0);
    }

    public void start() {
        this.onServerStart();
        currentlyOccupied = false;
        timeOutMilis = 500;
        gameThreads.scheduleAtFixedRate(this::listen, 10L, updateTimer, TimeUnit.MILLISECONDS);
    }

    private void listen() {
        Socket tempSocket = null;
        String tempLine = "";

        try {
            if(currentlyOccupied) {
                if((tempLine = this.getCurrentSocket().readLine()) != null) {
                    onMessage(this.getCurrentSocket(), tempLine);
                }
            } else if ((tempSocket = this.getServerSocket().accept()) != null) {
                currentlyOccupied = true;
                ClientConnection connection = new ClientConnection(tempSocket);
                onConnect(connection);
                this.getConnections().add(connection);
            }
        } catch(IOException e) {
            if(e.getMessage().contains("reset")) {
                onDisconnect(this.getCurrentSocket());
                this.getConnections().remove(0);
                currentlyOccupied = false;
                return;
            }
            Logger.log("Error while listening for sockets!", LoggerLevel.SEVERE);
        }
    }


    public void onConnect(ClientConnection connection) {
        Logger.log("Accepted socket " + connection.getSocket().getInetAddress());
    }

    public void onDisconnect(ClientConnection connection) {
        Logger.log("Socket disconnected on address: " + connection.getSocket().getInetAddress());
    }

    public void onMessage(ClientConnection connection, String message) {
        Logger.log("From " + this.getCurrentSocket().getSocket().getInetAddress() + ": " + message);
    }

    @Override
    public void onServerStart() {
        Logger.log("Starting Single Connection Server on port " + this.getPort());
    }


    @Deprecated
    public void setTimeOut(long miliseconds) {
        this.timeOutMilis = miliseconds;
    }


}
