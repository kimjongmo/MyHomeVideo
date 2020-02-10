package com.myhome.play.serivce;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageProducerServiceTest {

    @Autowired
    private MessageProducerService messageProducerService;

    @Test
    public void send_to_message_test(){
        for(int i=0;i<5;i++)
            messageProducerService.sendTo("TEST","test.avi");
    }
}