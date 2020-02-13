package com.myhome.play.domain;

import com.myhome.play.enums.EncodingResult;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncodingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileSize;
    @Enumerated(value = EnumType.STRING)
    private EncodingResult encodingResult;
    private LocalDateTime endAt;
    private LocalDateTime startAt;

}
