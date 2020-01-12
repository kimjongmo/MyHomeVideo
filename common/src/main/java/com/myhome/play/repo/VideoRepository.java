package com.myhome.play.repo;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    List<Video> findAllByCategory(Category category, Pageable pageable);
    List<Video> findTop5ByOrderByCreatedAtDesc();
    List<Video> findTop5ByCategoryOrderByCreatedAtDesc(Category category);
}
