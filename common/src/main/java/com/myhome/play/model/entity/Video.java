package com.myhome.play.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"category"})
public class Video extends BaseEntity {

    private String name;

    private String imgUrl;

    private Long views;

    @ManyToOne
    private Category category;
}
