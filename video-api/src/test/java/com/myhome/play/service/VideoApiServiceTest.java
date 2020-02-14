package com.myhome.play.service;

import com.myhome.play.components.MyResourceHttpRequestHandler;
import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import com.myhome.play.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VideoApiServiceTest {

    private VideoApiService videoApiService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private MyResourceHttpRequestHandler handler;
    @Mock
    private ThumbnailService thumbnailService;
    @Mock
    private FileUtils fileUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        videoApiService = new VideoApiService(videoRepository, categoryRepository, handler, thumbnailService, fileUtils);
    }

    @Test(expected = CategoryNotFoundException.class)
    public void get_list_with_not_exist_category() {
        given(categoryRepository.findByName(any())).willReturn(Optional.empty());
        videoApiService.getList("카테고리", Pageable.unpaged());
    }

    @Test
    public void get_list_with_valid_request() {
        Category mockCategory = Category.builder()
                .name("런닝맨")
                .directoryPath("런닝맨")
                .build();

        List<Video> list = Arrays.asList(
                Video.builder()
                        .views(0L)
                        .fileName("런닝맨1화.mp4")
                        .title("런닝맨1화")
                        .imgUrl("/img/thumbnail/런닝맨1화.jpg")
                        .build(),
                Video.builder()
                        .views(10L)
                        .fileName("런닝맨2화.mp4")
                        .title("런닝맨2화")
                        .imgUrl("/img/thumbnail/런닝맨2화.jpg")
                        .build()
        );
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.of(mockCategory));

        given(videoRepository.findAllByCategory(any(), any())).willReturn(list);

        Header<List<VideoListResponse>> response
                = videoApiService.getList("런닝맨", PageRequest.of(0, 6));

        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getData().size() == 2);

    }

    @Test
    public void get_list_with_valid_request_no_data() {
        Category mockCategory = Category.builder()
                .name("런닝맨")
                .directoryPath("런닝맨")
                .build();
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.of(mockCategory));

        given(videoRepository.findAllByCategory(any(), any())).willReturn(Collections.emptyList());

        Header<List<VideoListResponse>> response
                = videoApiService.getList("런닝맨", PageRequest.of(0, 6));

        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getData().size() == 0);
    }

    @Test
    public void get_info_valid_data_test() {
        Long id = 1L;

        Video video = Video.builder().build();
        video.setId(id);
        video.setViews(0L);
        video.setCategory(Category.builder().name("테스트").build());

        given(videoRepository.findByIdForUpdate(id)).willReturn(Optional.of(video));

        Header<VideoInfoResponse> header = videoApiService.getInfo(id);

        assertEquals(header.getStatus(), "OK");
        assertEquals(header.getData().getId(), id);
    }

    @Test
    public void get_info_invalid_data_test() {
        Long id = 1L;

        given(videoRepository.findById(id)).willReturn(Optional.empty());

        Header<VideoInfoResponse> header = videoApiService.getInfo(id);

        assertEquals(header.getStatus(), "ERROR");
        assertEquals(header.getData(), null);
        assertEquals(header.getDescription(), "존재하지 않는 데이터");

    }

    @Test
    public void get_info_view_count_test() throws ExecutionException, InterruptedException {
        // INPUT
        Long id = 1L;

        // GIVEN
        Video video = Video.builder().views(0L).category(Category.builder().name("테스트").build()).build();
        video.setId(id);

        given(videoRepository.findByIdForUpdate(id)).willReturn(Optional.of(video));
        given(videoRepository.save(any())).will(invocation -> {
            return invocation.getArgument(0);
        });

        Header<VideoInfoResponse> result = videoApiService.getInfo(id);
        assertEquals(result.getStatus(), "OK");
        assertTrue(result.getData() != null);
        assertEquals(result.getData().getViews(), Long.valueOf(1L));
    }

    // TODO: 2020-01-09 최근 등록 메서드 테스트 추가하기

}