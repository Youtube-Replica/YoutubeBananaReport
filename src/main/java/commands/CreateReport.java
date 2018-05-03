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
        this.consume("r3");
        HashMap<String, Object> props = parameters;
        Channel channel = (Channel) props.get("channel");
        JSONParser parser = new JSONParser();
        int id = 0;
        int submitterid = 0;
        int againstid = 0;
        String content = "";
        try {
            JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
            JSONObject params = (JSONObject) parser.parse(body.get("body").toString());
            submitterid = Integer.parseInt(params.get("submitterid").toString());
            againstid = Integer.parseInt(params.get("againstid").toString());
            content = params.get("content").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
        Envelope envelope = (Envelope) props.get("envelope");
        String response = Report.createReport(submitterid,againstid,content);
        sendMessage("database",properties.getCorrelationId(),response);
    }

    @Override
    public void handleApi(HashMap<String, Object> service_parameters) {
        HashMap<String, Object> props = parameters;
        AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
        String serviceBody = service_parameters.get("body").toString();

        Envelope envelope = (Envelope) props.get("envelope");
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, serviceBody.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
