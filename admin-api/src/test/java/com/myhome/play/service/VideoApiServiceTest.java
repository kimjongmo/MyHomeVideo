package com.myhome.play.service;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoModifyRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.video.VideoModifyResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import com.myhome.play.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class VideoApiServiceTest {

    private VideoApiService videoApiService;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private FileUtils fileUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        videoApiService = new VideoApiService(videoRepository, categoryRepository,fileUtils);
    }

    @Test
    //삭제 테스트
    public void delete() {
        Long id = 1L;

        Category category = Category.builder().name("테스트").build();
        Video video = Video.builder().category(category).fileName("테스트.mp4").build();
        video.setId(1L);


        given(videoRepository.findById(1L)).willReturn(Optional.of(video));
        doNothing().when(videoRepository).deleteById(anyLong());
        given(fileUtils.delete(eq("테스트"),eq("테스트.mp4"))).willReturn(true);

        Header header = videoApiService.delete(id);

        assertEquals(header.getStatus(),"OK");
        assertEquals(header.getDescription(),"삭제되었습니다");
        verify(videoRepository).deleteById(id);
        verify(videoRepository).findById(eq(1L));
    }

    @Test
    //존재하지 않는 데이터 삭제하려 할 때
    public void delete_with_not_exist(){
        Long id = 1L;

        given(videoRepository.findById(1L)).willReturn(Optional.empty());

        Header header = videoApiService.delete(id);

        assertEquals(header.getStatus(),"ERROR");
        assertEquals(header.getDescription(),"존재하지 않는 데이터");
    }



    @Test
    //리스트 테스트
    public void list() {
        String category = "TEST";
        Pageable pageable = PageRequest.of(0, 5);

        Category cate = Category.builder().name(category).build();
        List<Video> videoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String file = "제목" + i;
            videoList.add(Video.builder()
                    .category(cate)
                    .title(file)
                    .fileName(file + ".mp4")
                    .imgUrl(file + ".jpg")
                    .views(1L)
                    .build());

        }

        given(categoryRepository.findByName(category)).willReturn(Optional.of(cate));
        given(videoRepository.findAllByCategory(cate, pageable)).willReturn(videoList.subList(0, 5));

        Header<List<VideoListResponse>> header = videoApiService.getList(category, pageable);

        verify(categoryRepository).findByName("TEST");
        verify(videoRepository).findAllByCategory(any(), any());
        assertEquals(header.getStatus(), "OK");
        assertTrue(header.getData().size() == 5);
    }

    @Test
    //존재하지 않는 카테고리일 때
    public void category_not_found_test() {
        String category = "TEST";
        Pageable pageable = PageRequest.of(0, 5);

        given(categoryRepository.findByName(category)).willReturn(Optional.empty());

        Header header = videoApiService.getList(category, pageable);
        assertEquals(header.getStatus(),"ERROR");
        assertTrue(header.getDescription().contains("카테고리"));
    }

    @Test
    //비디오 메타 데이터 정보 변경 테스트
    public void modify_video_data(){
        Video beforeModify = Video.builder()
                .fileName("변경 전 이름.avi")
                .imgUrl("변경전.jpg")
                .title("파일")
                .views(1L)
                .build();
        beforeModify.setId(1L);

        VideoModifyRequest request = VideoModifyRequest.builder()
                .id(1L)
                .fileName("변경된 이름.mp4")
                .imgUrl("변경.jpg")
                .title("변경된 파일입니다.")
                .views(50L)
                .build();

        given(videoRepository.findById(1L)).willReturn(Optional.of(beforeModify));
        given(videoRepository.save(any())).will(invocation -> {
            return invocation.getArgument(0);
        });

        Header<VideoModifyResponse> header = videoApiService.modify(request);
        verify(videoRepository).findById(eq(1L));
        verify(videoRepository).save(any());
        assertEquals(header.getStatus(),"OK");
        assertEquals(header.getData().getFileName(),"변경된 이름.mp4");
        assertEquals(header.getData().getImgUrl(),"변경.jpg");
    }

    @Test
    //존재하지 않는 데이터에 대한 수정 요구
    public void modify_not_existed_data(){
        Video beforeModify = Video.builder()
                .fileName("변경 전 이름.avi")
                .imgUrl("변경전.jpg")
                .title("파일")
                .views(1L)
                .build();
        beforeModify.setId(1L);

        VideoModifyRequest request = VideoModifyRequest.builder()
                .id(2L)
                .fileName("변경된 이름.mp4")
                .imgUrl("변경.jpg")
                .title("변경된 파일입니다.")
                .views(50L)
                .build();

        given(videoRepository.findById(2L)).willReturn(Optional.empty());

        Header header = videoApiService.modify(request);

        assertEquals(header.getStatus(),"ERROR");
        assertEquals(header.getDescription(),"존재하지 않는 데이터입니다.");
        assertEquals(header.getData(),null);
    }
}