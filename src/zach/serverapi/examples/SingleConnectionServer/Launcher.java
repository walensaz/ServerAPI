package zach.serverapi.examples.SingleConnectionServer;

import zach.serverapi.examples.MultiConnectionServer.MultiServer;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        SingleServer singleServer = new SingleServer(5555);
        singleServer.start();
    }


}
