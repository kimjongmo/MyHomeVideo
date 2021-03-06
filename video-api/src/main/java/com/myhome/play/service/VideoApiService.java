package com.myhome.play.service;

import com.myhome.play.components.MyResourceHttpRequestHandler;
import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import com.myhome.play.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoApiService {

    private VideoRepository videoRepository;
    private CategoryRepository categoryRepository;
    private MyResourceHttpRequestHandler handler;
    private ThumbnailService thumbnailService;
    private FileUtils fileUtils;

    public VideoApiService(VideoRepository videoRepository,
                           CategoryRepository categoryRepository,
                           MyResourceHttpRequestHandler handler,
                           ThumbnailService thumbnailService,
                           FileUtils fileUtils) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.handler = handler;
        this.thumbnailService = thumbnailService;
        this.fileUtils = fileUtils;
    }


    public Header<List<VideoListResponse>> getList(String category, Pageable pageable) {
        Optional<Category> optionalCategory = categoryRepository.findByName(category);

        List<VideoListResponse> videoListResponses;

        if (optionalCategory.isPresent()) {
            videoListResponses = videoRepository.findAllByCategory(optionalCategory.get(), pageable)
                    .stream()
                    .map(this::response)
                    .collect(Collectors.toList());
            return Header.OK(videoListResponses);
        } else {
            throw new CategoryNotFoundException(category);
        }
    }

    public Header<List<VideoListResponse>> getRecentRegistered(String categoryName) {
        if (Strings.isEmpty(categoryName)) {
            List<Video> list = videoRepository.findTop5ByOrderByCreatedAtDesc();
            return Header.OK(
                    list.stream()
                            .map(this::response)
                            .collect(Collectors.toList())
            );
        } else {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                return Header.OK(videoRepository.findTop5ByCategoryOrderByCreatedAtDesc(category)
                        .stream().map(this::response).collect(Collectors.toList()));
            }
            throw new CategoryNotFoundException(categoryName);
        }


    }

    public void play(HttpServletRequest req, HttpServletResponse res, Long id) throws ServletException, IOException {
        Optional<Video> optionalVideo = videoRepository.findById(id);

        if (!optionalVideo.isPresent()) {
            insertScript(res);
            return;
        }

        Video video = optionalVideo.get();

        File file = fileUtils.getFile(video.getCategory().getName(), video.getFileName());
        if (file.exists()) {
            insertScript(res);
            return;
        }
        req.setAttribute("file", file);
        handler.handleRequest(req, res);

    }

    public Video insert(VideoInsertRequest data) {

        String fileName = data.getFileName();
        String categoryName = data.getCategoryName();

        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        if (!optionalCategory.isPresent())
            throw new CategoryNotFoundException(categoryName);

        String pureName = data.getFileName().substring(0, data.getFileName().lastIndexOf('.'));
        Video video = Video.builder()
                .title(data.getTitle())
                .fileName(fileName)
                .category(categoryRepository.findByName(categoryName).get())
                .imgUrl("/img/thumbnail/" + pureName + ".jpg")
                .views(0L)
                .build();

        // TODO: 2020-02-04 썸네일 생성을 비동기로 생성하는 방식으로 바꾸기
        boolean result = thumbnailService.create(fileUtils.getFile(data.getCategoryName(), data.getFileName()));
        log.info("{} 썸네일 생성 : {}",video.getFileName(),result);
        return videoRepository.save(video);
    }

    public VideoListResponse response(Video video) {
        return VideoListResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .thumbnailUrl(video.getImgUrl())
                .view(video.getViews())
                .description(video.getFileName())
                .build();
    }

    @Transactional
    public Header<VideoInfoResponse> getInfo(Long id) {

        Optional<Video> optionalVideo = videoRepository.findByIdForUpdate(id);
        if (!optionalVideo.isPresent()) {
            return Header.ERROR("존재하지 않는 데이터");
        }

        Video video = optionalVideo.get();
        video.setViews(video.getViews() + 1);
        videoRepository.save(video);


        VideoInfoResponse videoInfoResponse = VideoInfoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .categoryName(video.getCategory().getName())
                .imgUrl(video.getImgUrl())
                .fileName(video.getFileName())
                .views(video.getViews())
                .build();

        return Header.OK(videoInfoResponse);
    }

    public void insertScript(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print("<script>alert('존재하지 않는 비디오입니다');history.back();</script>");
        writer.flush();
    }
}
