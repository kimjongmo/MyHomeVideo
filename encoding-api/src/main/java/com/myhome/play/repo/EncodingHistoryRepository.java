package com.myhome.play.repo;

import com.myhome.play.domain.EncodingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncodingHistoryRepository extends JpaRepository<EncodingHistory,Long> {
}
