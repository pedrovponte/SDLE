import java.util.StringTokenizer;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

//
//  Weather update client in Java
//  Connects SUB socket to tcp://localhost:5556
//  Collects weather updates and finds avg temp in zipcode
//

public class wuClient {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Socket to talk to server
            System.out.println("Collecting updates from weather server");
            ZMQ.Socket subscriberUS = context.createSocket(SocketType.SUB);
            subscriberUS.connect("tcp://localhost:5556");
            ZMQ.Socket subscriberPT = context.createSocket(SocketType.SUB);
            subscriberPT.connect("tcp://localhost:5557");

            // Subscribe to zipcode, default is NYC, 10001
            String filter = (args.length > 0) ? args[0] : "4710 ";
            subscriberUS.subscribe(filter.getBytes(ZMQ.CHARSET));
            subscriberPT.subscribe(filter.getBytes(ZMQ.CHARSET));

            // zmq_pollitem_t items[] = {
            //     {subscriberUS, 0, ZMQ_POLLIN, 0},
            //     {subscriberPT, 0, ZMQ_POLLIN, 0}
            // };

            // Initialize poll set
            Poller items = context.createPoller(2);
            items.register(subscriberUS, Poller.POLLIN);
            items.register(subscriberPT, Poller.POLLIN);

            // Process 100 updates
            int update_nbr;
            long total_temp = 0;
            for (update_nbr = 0; update_nbr < 100; update_nbr++) {
                zmq_poll(items, 2, -1);

                if(items[1].revents & Poller.POLLIN) { // PT 
                    // Use trim to remove the tailing '0' character
                    String string = subscriberPT.recvStr(0).trim();

                    StringTokenizer sscanf = new StringTokenizer(string, " ");
                    int zipcode = Integer.valueOf(sscanf.nextToken());
                    int temperature = Integer.valueOf(sscanf.nextToken());
                    int relhumidity = Integer.valueOf(sscanf.nextToken());

                    total_temp += temperature;

                    System.out.println("PT");
                }
                if(items[0].revents & Poller.POLLIN) { // US
                    // Use trim to remove the tailing '0' character
                    String string = subscriberUS.recvStr(0).trim();
    
                    StringTokenizer sscanf = new StringTokenizer(string, " ");
                    int zipcode = Integer.valueOf(sscanf.nextToken());
                    int temperature = Integer.valueOf(sscanf.nextToken());
                    int relhumidity = Integer.valueOf(sscanf.nextToken());
    
                    total_temp += temperature;

                    System.out.println("US");
                }
            }

            System.out.println(
                String.format("Average temperature for zipcode '%s' was %d." , filter, (int) (total_temp / update_nbr))
            );
        }
    }    
}
