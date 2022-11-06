package com.fareed.javanote.application.dto;

import lombok.Data;

public @Data class NoteDTO {

    private String title;
    private String content;

    public NoteDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
