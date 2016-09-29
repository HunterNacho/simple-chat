import org.jgroups.JChannel;
import org.jgroups.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleChat  {
    JChannel channel;
    String user_name=System.getProperty("user.name", "n/a");


    private void start() throws Exception {
        channel=new JChannel("relay.xml");
        //channel.getProtocolStack().addProtocol(new RELAY());
        channel.setReceiver(new Receiver());
        /*channel.getProtocolStack().findProtocol(TP.class)
                .setValue("bind_addr", InetAddress.getByName("192.168.16.38"))
                .setValue("bind_port", 8205);*/
        channel.connect("tcp.xml");
        channel.getState(null, 10000);
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                line="[" + user_name + "] " + line;
                Message msg=new Message(null, null, line);
                channel.send(msg);
            }
            catch(Exception e) {
            }
        }
    }


    public static void main(String[] args) throws Exception {
        new SimpleChat().start();
    }
}