package com.myhome.play.service;

import com.myhome.play.domain.EncodingHistory;
import com.myhome.play.repo.EncodingHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class EncodingHistoryService {

    private EncodingHistoryRepository encodingHistoryRepository;

    public EncodingHistoryService(EncodingHistoryRepository encodingHistoryRepository){
        this.encodingHistoryRepository = encodingHistoryRepository;
    }

    public EncodingHistory save(EncodingHistory encodingHistory){
        return encodingHistoryRepository.save(encodingHistory);
    }
}
