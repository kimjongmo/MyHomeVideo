package com.myhome.play.service;

import com.myhome.play.components.MyResourceHttpRequestHandler;
import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
public class VideoApiService {

    @Value("${home.path}")
    public String HOME_PATH;
    private VideoRepository videoRepository;
    private CategoryRepository categoryRepository;
    private MyResourceHttpRequestHandler handler;
    private ThumbnailService thumbnailService;

    public VideoApiService(VideoRepository videoRepository,
                           CategoryRepository categoryRepository,
                           MyResourceHttpRequestHandler handler,
                           ThumbnailService thumbnailService) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.handler = handler;
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
        if(Strings.isEmpty(categoryName)) {
            List<Video> list = videoRepository.findTop5ByOrderByCreatedAtDesc();
            return Header.OK(
                    list.stream()
                            .map(this::response)
                            .collect(Collectors.toList())
            );
        }else{
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
            if(optionalCategory.isPresent()){
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
            PrintWriter writer = res.getWriter();
            writer.print("<script>alert('존재하지 않는 비디오입니다');history.back();</script>");
            writer.flush();
        }
        Video video = optionalVideo.get();

        File file = new File(HOME_PATH + "/" + video.getCategory().getDirectoryPath() + "/" + video.getFileName());
        req.setAttribute("file", file);
        handler.handleRequest(req, res);

    }

    public Video insert(VideoInsertRequest data) {

        String fileName = data.getFileName();
        String categoryName = data.getCategoryName();

        if (!fileNameCheck(fileName, categoryName))
            throw new FileDuplicateException(fileName);

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

        thumbnailService.create(new File(HOME_PATH + "/" + data.getCategoryName() + "/" + data.getFileName()));

        return videoRepository.save(video);
    }

    public boolean fileNameCheck(String fileName, String categoryName) {
        File file = new File(HOME_PATH + "/" + categoryName + "/" + fileName);
        return !file.exists();
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


}
