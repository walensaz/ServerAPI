package zach.serverapi;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Future;

public class ClientConnection {

    boolean isConnected;

    private InputStream reader;
    private OutputStream writer;

    private Socket socket;

    public ClientConnection(Socket socket) throws IOException {
        if(socket != null) {
            this.socket = socket;
            this.reader = socket.getInputStream();
            this.writer = socket.getOutputStream();
            isConnected = true;
        } else {
            this.socket = new Socket();
            isConnected = true;
        }
    }

    public void sendMessage(String message) {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(this.writer));
        writer.println(message);
        writer.flush();
    }

    public String readLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.reader));
        return reader.readLine();
    }

    protected InputStream getReader() {
        return reader;
    }

    protected OutputStream getWriter() {
        return writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public void terminate() {
        try {
            this.getSocket().close();
            Logger.log("Terminated connection!");
        } catch(IOException e) {
            Logger.log("Could not terminate connection!", LoggerLevel.INFO);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    protected void setConnected(boolean connected) {
        isConnected = connected;
    }

    protected void setReader(InputStream reader) {
        this.reader = reader;
    }

    protected void setWriter(OutputStream writer) {
        this.writer = writer;
    }

    protected void setSocket(Socket socket) {
        this.socket = socket;
    }
}
