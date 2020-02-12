package com.myhome.play.serivce;

import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import com.myhome.play.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageProducerService {

    @Value("${topic.encode}")
    public String topic;

    private RabbitTemplate rabbitTemplate;

    public MessageProducerService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTo(String category, String name, String title){
        EncodeRequestDTO request = EncodeRequestDTO.builder().title(title).category(category).name(name).build();
        log.info("전송>>...");
        rabbitTemplate.convertAndSend(topic, JsonMapper.toJson(request));
    }
}
