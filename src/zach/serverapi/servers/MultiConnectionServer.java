package zach.serverapi.servers;

import zach.serverapi.Logger;
import zach.serverapi.LoggerLevel;
import zach.serverapi.ClientConnection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultiConnectionServer extends BaseServer {

    private final ScheduledExecutorService gameThreads = Executors.newScheduledThreadPool(2);


    private long timeOutMilis;
    private int maxConnections;
    private int updateTimer;

    public MultiConnectionServer(int port) throws IOException {
        super(port);
        updateTimer = 300;
        maxConnections = 100;
    }

    public MultiConnectionServer(int port, int updateTimer) throws IOException {
        super(port);
        this.updateTimer = updateTimer;
    }

    public MultiConnectionServer(int port, int updateTimer, int maxConnections) throws IOException {
        this(port, updateTimer);
        this.maxConnections = maxConnections;
    }

    public void start() {
        this.onServerStart();
        timeOutMilis = 500;
        gameThreads.scheduleAtFixedRate(this::listener, 10L, updateTimer, TimeUnit.MILLISECONDS);
        gameThreads.scheduleAtFixedRate(this::updater, 10L, updateTimer, TimeUnit.MILLISECONDS);
    }

    private void listener() {
        Socket tempSocket = null;
        try {
            if ((tempSocket = this.getServerSocket().accept()) != null && this.maxConnections >= this.getConnections().size()) {
                ClientConnection connection = new ClientConnection(tempSocket);
                onConnect(connection);
                this.getConnections().add(connection);

            }
        } catch (IOException e) {
            Logger.log("Error while listening for sockets!", LoggerLevel.SEVERE);
            e.printStackTrace();
        }
    }

    private void updater() {
        for (ClientConnection connection : this.getConnections()) {
            try {
                String tempLine = "";
                if ((tempLine = connection.readLine()) != null) {
                    onMessage(connection, tempLine);
                }
            } catch(IOException e) {
                if(e.getMessage().contains("reset")) {
                    this.getConnections().remove(connection);
                    onDisconnect(connection);
                    connection.terminate();
                } else {
                    Logger.log("We have encountered an error in the updater!");
                }
            }
        }
    }

    public void onConnect(ClientConnection connection) {
        Logger.log("Accepted socket " + connection.getSocket().getInetAddress());
    }

    public void onDisconnect(ClientConnection connection) {
        Logger.log("Socket disconnected on address: " + connection.getSocket().getInetAddress());
    }

    public void onMessage(ClientConnection connection, String message) {
        Logger.log("From " + connection.getSocket().getInetAddress() + ": " + message);
    }

    @Override
    public void onServerStart() {
        Logger.log("Starting Multi Connection Server on port " + this.getPort());
    }


    @Deprecated
    public void setTimeOut(long miliseconds) {
        this.timeOutMilis = miliseconds;
    }


}
