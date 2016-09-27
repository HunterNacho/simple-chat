import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.protocols.TP;

import java.net.Inet4Address;
import java.net.InetAddress;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleChat  {
    JChannel channel;
    String user_name=System.getProperty("user.name", "n/a");


    private void start() throws Exception {
        channel=new JChannel();
        channel.setReceiver(new Receiver());
        channel.getProtocolStack().findProtocol(TP.class).setValue("bind_addr", Inet4Address.getByName("192.168.16.38"));
        channel.connect("ChatCluster");
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