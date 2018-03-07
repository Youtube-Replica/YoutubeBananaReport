

import com.rabbitmq.client.*;
import commands.Command;
import commands.delete.DeleteReport;
import commands.get.GetReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class ReportService {

    private static final String RPC_QUEUE_NAME = "report-request";

    public static void main(String [] argv) {

        //initialize thread pool of fixed size
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    System.out.println("Responding to corrID: "+ properties.getCorrelationId());

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        Command cmd = new DeleteReport();
                        HashMap<String, Object> props = new HashMap<String, Object>();
                        props.put("channel", channel);
                        props.put("properties", properties);
                        props.put("replyProps", replyProps);
                        props.put("envelope", envelope);
                        props.put("body", message);

                        cmd.init(props);
                        executor.submit(cmd);
                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
//        finally {
//            if (connection != null)
//                try {
//                    connection.close();
//                } catch (IOException _ignore) {
//                }
//        }

    }
}