package zach.serverapi.examples.FastHandlingMultiConnectionServer;

public class Launcher {

    public static void main(String[] args) throws Exception {
        CustomClient client = new CustomClient(null, null);
        //port: port you want open listening on your network
        //update timer: How fast the threads are updated for listening for people to connect and updating players/connections with messages
        //maxConnection: Most connections you want connected to the server
        //myclient: is a custom object that you make by extending CustomClientThreadedConnection and overriding the onMessage, onConnection, etc.
        //methods and putting your own stuff in.
        FastMultiServer server = new FastMultiServer(555, 20, 300, client);
        server.start();



    }



}
