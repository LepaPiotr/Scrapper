package com.mongo.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Search {
    @Id
    private String id;
    private String phrase;
    private Date searchDate;

    public Search(String phrase, Date searchDate) {
        this.phrase = phrase;
        this.searchDate = searchDate;
    }
}
