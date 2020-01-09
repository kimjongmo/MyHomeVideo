package com.myhome.play.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"category"})
public class Video extends BaseEntity {

    private String title;        // 유저에게 보여지는 이름
    private String fileName;    // 실제 파일의 이름
    private String imgUrl;      // 썸네일 url
    private Long views;         // 재생 횟수
    @ManyToOne
    private Category category;  // 카테고리
}
