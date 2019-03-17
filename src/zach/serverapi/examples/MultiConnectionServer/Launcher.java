package zach.serverapi.examples.MultiConnectionServer;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException {
        MultiServer multiServer = new MultiServer(5555);
        multiServer.start();
    }


}
