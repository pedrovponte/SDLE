import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZContext;

/**
 * Weather proxy device.
 */
public class wuproxy {
    public static void main(String[] args) {
        // Prepare our context and sockets
        try (ZContext context = new ZContext()){
            Socket frontend = context.createSocket(SocketType.XSUB);
            frontend.connect("");

            Socket backend = context.createSocket(SocketType.XPUB);
            backend.connect(""); // bind vs connect??

            frontend
        }
    }
}
