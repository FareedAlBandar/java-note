package com.fareed.javanote.presentation.response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fareed.javanote.domain.entity.NoteHistory;

import lombok.Data;

public @Data class HistoryResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Date createdAt;

    public HistoryResponse(NoteHistory noteHistory) {
        this.id = noteHistory.getId();
        this.title = noteHistory.getTitle();
        this.content = noteHistory.getContent();
        this.author = noteHistory.getAuthor().getUsername();
        this.createdAt = noteHistory.getCreatedAt();
    }



    public static List<HistoryResponse> collection(List<NoteHistory> histories) {
        return histories.stream().map(history -> new HistoryResponse(history)).collect(Collectors.toList());
    }

}
