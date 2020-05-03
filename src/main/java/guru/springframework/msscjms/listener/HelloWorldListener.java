package guru.springframework.msscjms.listener;

import guru.springframework.msscjms.config.JmsConfig;
import guru.springframework.msscjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloWorldListener {

  private final JmsTemplate jmsTemplate;

  @JmsListener(destination = JmsConfig.MY_QUEUE)
  public void listen(
          @Payload HelloWorldMessage helloWorldMessage,
          @Headers MessageHeaders headers,
          Message message) {
    System.out.println("I got a message!");
    System.out.println(helloWorldMessage);
  }

  @JmsListener(destination = JmsConfig.SEND_RECEIVE_QUEUE)
  public void listenForHello(
          @Payload HelloWorldMessage helloWorldMessage,
          @Headers MessageHeaders headers,
          javax.jms.Message message)
          throws JMSException {
    HelloWorldMessage payload =
            HelloWorldMessage.builder().id(UUID.randomUUID()).message("World").build();
    jmsTemplate.convertAndSend(message.getJMSDestination(), payload);
  }
}
