import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Random;

public class proxyPublisher {
    public static void main(String[] args) throws Exception {
        // Prepare our context and publisher for
        try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            System.out.println("Publisher Connecting to Proxy...");
            publisher.bind("tcp://*:5557");

            // Initialize random number generator
            Random srandom = new Random(System.currentTimeMillis());
            while (!Thread.currentThread().isInterrupted()) {
                // Get values that will fool the boss
                int temperature, relhumidity;
                temperature = srandom.nextInt(215) - 80 + 1;
                relhumidity = srandom.nextInt(50) + 10 + 1;

                System.out.println(args[0] + "; Temperature: " + temperature + "; Humidity: " + relhumidity);

                // Send message to all subscribers
                String update = String.format(args[0] + " " + temperature + " " + relhumidity);
                publisher.send(update, 0);

                Thread.sleep(100);
            }
        }
    }
}
