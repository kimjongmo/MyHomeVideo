package com.myhome.play.service;

import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import com.myhome.play.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumerService {

    private EncodingService encodingService;

    public MessageConsumerService(EncodingService encodingService){
        this.encodingService = encodingService;
    }


    @RabbitListener(queues = "${topic.encode}")
    public void handler(String message) {
        log.info(">> {}", message);
        EncodeRequestDTO req = JsonMapper.strToObject(message, EncodeRequestDTO.class);
        encodingService.encodingToMPEG4(req);
    }

}
