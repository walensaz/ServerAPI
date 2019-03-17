package zach.serverapi.servers;

import zach.serverapi.Logger;
import zach.serverapi.LoggerLevel;
import zach.serverapi.ClientConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FastMultiConnectionServer extends BaseServer {

    private final ScheduledExecutorService gameThreads = Executors.newScheduledThreadPool(2);
    private ScheduledExecutorService playerThreads = null;

    private long timeOutMilis;
    private int maxConnections;
    private int updateTimer;
    private ClientThreadedConnection myclient;

    public FastMultiConnectionServer(int port, ClientThreadedConnection myclient) throws IOException {
        super(port);
        updateTimer = 300;
        maxConnections = 100;
        this.myclient = myclient;
    }

    public FastMultiConnectionServer(int port, int updateTimer, ClientThreadedConnection myclient) throws IOException {
        this(port, myclient);
        this.updateTimer = updateTimer;
    }

    public FastMultiConnectionServer(int port, int updateTimer, int maxConnections, ClientThreadedConnection myclient) throws IOException {
        this(port, updateTimer, myclient);
        this.maxConnections = maxConnections;
    }

    public void start() {
        this.onServerStart();
        playerThreads = Executors.newScheduledThreadPool(maxConnections);
        timeOutMilis = 500;
        gameThreads.scheduleAtFixedRate(this::listener, 10L, updateTimer, TimeUnit.MILLISECONDS);
        gameThreads.scheduleAtFixedRate(this::updater, 10L, updateTimer * 3, TimeUnit.MILLISECONDS);
    }

    private void listener() {
        Socket tempSocket = null;
        try {
            if ((tempSocket = this.getServerSocket().accept()) != null && this.maxConnections >= this.getConnections().size()) {
                myclient.createNew(tempSocket);
                onConnect(myclient);
                myclient.setFuture(playerThreads.scheduleAtFixedRate(myclient::run, 10L, updateTimer, TimeUnit.MILLISECONDS));
                this.getConnections().add(myclient);
            }
        } catch (IOException e) {
            Logger.log("Error while listening for sockets!", LoggerLevel.SEVERE);
            e.printStackTrace();
        }
    }

    private void updater() {
        for (ClientConnection connection : this.getConnections()) {
            if(!connection.isConnected()) {
                connection.terminate();
                this.getConnections().remove(connection);
                onDisconnect(connection);
            }
        }
    }

    public void onConnect(ClientConnection connection) {
        Logger.log("Accepted socket " + connection.getSocket().getInetAddress());
    }

    public void onDisconnect(ClientConnection connection) {
        Logger.log("Socket disconnected on address: " + connection.getSocket().getInetAddress());
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
