package com.myhome.play.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"videoList"})
public class Category extends BaseEntity{

    private String name;
    private String directoryPath;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Video> videoList;
}
