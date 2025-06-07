package com.example.pageserver.webserver;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
public class NanoWebServer extends NanoHTTPD{

    public NanoWebServer(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("started server at port " + port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String html = "<html><body><h1>Hello from your Android server!</h1></body></html>";
        return newFixedLengthResponse(html);
    }
}
