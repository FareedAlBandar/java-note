package com.fareed.javanote.presentation.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fareed.javanote.domain.entity.Note;
import com.fareed.javanote.domain.entity.NoteUser;
import com.fareed.javanote.domain.entity.User;

import lombok.Data;

public @Data class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private String owner;
    private String role;

    public NoteResponse(Note note, User user) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.owner = note.getNoteUsers().stream().filter(noteUser -> noteUser.getRole().equals(NoteUser.Role.OWNER)).findFirst().get().getUser().getUsername();
        this.role = note.getNoteUsers().stream().filter(noteUser -> noteUser.getUser().getUsername().equals(user.getUsername())).findFirst().get().getRole().toString();
    }


    public static List<NoteResponse> collection(List<Note> notes, User user) {
        return notes.stream().map(note -> new NoteResponse(note, user)).collect(Collectors.toList());
    }




}
