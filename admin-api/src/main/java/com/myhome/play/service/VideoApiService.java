package com.myhome.play.service;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoModifyRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.video.VideoModifyResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import com.myhome.play.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoApiService {

    private VideoRepository videoRepository;
    private CategoryRepository categoryRepository;
    private FileUtils fileUtils;

    public VideoApiService(VideoRepository videoRepository,
                           CategoryRepository categoryRepository,
                           FileUtils fileUtils) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtils = fileUtils;
    }

    public Header delete(Long id) {
        Optional<Video> optional = videoRepository.findById(id);
        if(!optional.isPresent())
            return Header.ERROR("존재하지 않는 데이터");

        Video video = optional.get();

        String category = video.getCategory().getName();
        String fileName = video.getFileName();

        videoRepository.deleteById(id);
        boolean isDelete = fileUtils.delete(category,fileName);

        if(!isDelete){
            log.error("{}의 {}가 삭제되지 않았습니다.",category,fileName);
        }

        return Header.MESSAGE("삭제되었습니다");
    }


    public Header<List<VideoListResponse>> getList(String category, Pageable pageable) {

        Optional<Category> optionalCategory = categoryRepository.findByName(category);

        List<VideoListResponse> videoListResponses;

        if (optionalCategory.isPresent()) {
            videoListResponses = videoRepository.findAllByCategory(optionalCategory.get(), pageable)
                    .stream()
                    .map(VideoListResponse::of)
                    .collect(Collectors.toList());
            return Header.OK(videoListResponses);
        }

        return Header.ERROR("존재하지 않는 카테고리");
    }

    public Header<VideoModifyResponse> modify(VideoModifyRequest request) {

        Long searchId = request.getId();

        Optional<Video> optional = videoRepository.findById(searchId);
        if (!optional.isPresent())
            return Header.ERROR("존재하지 않는 데이터입니다.");

        Video video = optional.get();
        video.setFileName(request.getFileName());
        video.setImgUrl(request.getImgUrl());
        video.setTitle(request.getTitle());
        video.setViews(request.getViews());

        Video saved = videoRepository.save(video);

        return Header.OK(VideoModifyResponse.of(saved));
    }

}
