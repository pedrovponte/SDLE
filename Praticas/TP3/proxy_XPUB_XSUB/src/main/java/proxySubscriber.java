import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class proxySubscriber {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Socket to talk to server
            System.out.println("Collecting updates from weather server");
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://*:5556");

            // Subscribe to zipcode, default is NYC, 10001
            //String filter = (args.length > 0) ? args[0] : "10001 ";
            subscriber.subscribe(args[0].getBytes());

            int total_messages = 0;

            while (total_messages < 100) {
                // Use trim to remove the tailing '0' character
                String string = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(string, " ");
                sscanf.nextToken();
                int temperature = Integer.valueOf(sscanf.nextToken());
                int relhumidity = Integer.valueOf(sscanf.nextToken());

                System.out.println("ZipCode: " + args[0] + "; Temperature: " + temperature + "; Humidity: " + relhumidity);
                total_messages++;
            }
        }
    }
}
