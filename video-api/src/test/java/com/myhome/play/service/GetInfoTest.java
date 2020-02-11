package com.myhome.play.service;

import com.myhome.play.repo.VideoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GetInfoTest {

    @Autowired
    private VideoApiService videoApiService;
    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void context(){

    }
//    @Test
    public void multi_request_get_info_test(){

        List<Long> results = new ArrayList<>();
        Collections.synchronizedList(results);

        Long before = videoRepository.findById(7L).get().getViews();

        int requestTime = 30;
        for(int i = 0; i<requestTime; i++)
            CompletableFuture.runAsync(()->{
               results.add(videoApiService.getInfo(7L).getData().getViews());
            });

        while(!(results.size()==requestTime)){
            try {
                System.out.println("현재 완료 요청 수 : "+results.size());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };


        Long after = videoRepository.findById(7L).get().getViews();

        System.out.println(Arrays.toString(results.toArray()));
        System.out.println(after-before);
    }

}
