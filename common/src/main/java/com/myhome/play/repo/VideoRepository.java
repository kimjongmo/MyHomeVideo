package com.myhome.play.repo;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.entity.Video;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    List<Video> findAllByCategory(Category category, Pageable pageable);
    List<Video> findTop5ByOrderByCreatedAtDesc();
    List<Video> findTop5ByCategoryOrderByCreatedAtDesc(Category category);
    @Query(value = "select * from Video v where id = ?1 for update;",nativeQuery = true)
    Optional<Video> findByIdForUpdate(Long id);
}
