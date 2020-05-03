package guru.springframework.msscjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscjms.config.JmsConfig;
import guru.springframework.msscjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        System.out.println("I'm sending a message");

        HelloWorldMessage message =
                HelloWorldMessage.builder().id(UUID.randomUUID()).message("Hello World").build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
        System.out.println("Message sent");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {

        HelloWorldMessage message =
                HelloWorldMessage.builder().id(UUID.randomUUID()).message("Hello").build();
        Message receivedMessage =
                jmsTemplate.sendAndReceive(
                        JmsConfig.SEND_RECEIVE_QUEUE,
                        session -> {
                            TextMessage helloMessage;
                            try {
                                helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                                helloMessage.setStringProperty(
                                        "_type", "guru.springframework.msscjms.model.HelloWorldMessage");
                                System.out.println("Sending hello");
                                return helloMessage;
                            } catch (JsonProcessingException e) {
                                throw new JMSException("boom");
                            }
                        });
        System.out.println(receivedMessage.getBody(HelloWorldMessage.class));
    }
}
