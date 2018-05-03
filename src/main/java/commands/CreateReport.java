package commands;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import model.Report;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class CreateReport extends ConcreteCommand {

    public void execute() {
        HashMap<String, Object> props = parameters;

        Channel channel = (Channel) props.get("channel");
        JSONParser parser = new JSONParser();
        int id = 0;
        String submitterid = "";
        String againstid = "";
        String content = "";
        try {
            JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
            JSONObject params = (JSONObject) parser.parse(body.get("body").toString());
            submitterid = params.get("submitterid").toString();
            againstid = params.get("againstid").toString();
            content = params.get("content").toString();
            System.out.println("submitterid: " + submitterid + ", againstid: " + againstid + ", content: " + content);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
        Envelope envelope = (Envelope) props.get("envelope");
        String response = Report.createReport(submitterid,againstid,content);
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleApi(HashMap<String, Object> service_parameters) {

    }
}
