package commands;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import model.Report;

import java.io.IOException;
import java.util.HashMap;

public class RetrieveReport extends ConcreteCommand {

    public void execute() {
        this.consume("r1");
        HashMap<String, Object> props = parameters;

        Channel channel = (Channel) props.get("channel");
        JSONParser parser = new JSONParser();
        int id = 0;
        int pageNumber = 0;
        int pageSize = 0;
        boolean list = false;
        try {
            JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
            System.out.println(body.toString());
            JSONObject params = (JSONObject) parser.parse(body.get("parameters").toString());
            if(params.containsKey("id")) {
                id = Integer.parseInt(params.get("id").toString());
            }
            else{
                list = true;
                pageNumber = Integer.parseInt(params.get("pageNumber").toString());
                pageSize = Integer.parseInt(params.get("pageSize").toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
        Envelope envelope = (Envelope) props.get("envelope");
        String response = "";
        if(list){
            response = Report.getReportsPaginated(pageNumber,pageSize);
        }else{
            response = Report.getReportById(id);
        }
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
