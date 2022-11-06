package com.fareed.javanote.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Hibernate;

import lombok.Data;


@Entity(name = "Note")
@Table(name = "notes")
@Transactional
public @Data class Note extends SuperNote {

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<NoteUser> noteUsers = new ArrayList<>();

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    @OrderBy("createdAt ASC")
    private List<NoteHistory> histories = new ArrayList<>();


    public List<NoteUser> getNoteUsers() {
        if (!Hibernate.isInitialized(noteUsers)) {
            Hibernate.initialize(noteUsers);
        }
        return noteUsers;
    }

    public void setNoteUsers(List<NoteUser> noteUsers) {
        this.noteUsers = noteUsers;
    }

    public void addNoteUser(User user, NoteUser.Role role) {
        NoteUser noteUser = new NoteUser();
        noteUser.setNote(this);
        noteUser.setUser(user);
        noteUser.setRole(role);
        this.getNoteUsers();
        this.noteUsers.add(noteUser);
    }

    public List<NoteHistory> getHistories() {
        if (!Hibernate.isInitialized(histories)) {
            Hibernate.initialize(histories);
        }
        return histories;
    }

    public void setHistories(List<NoteHistory> histories) {
        this.histories = histories;
    }

    public void addHistory(NoteHistory history) {

        this.getHistories().add(history);
    }

    public void setOwner(User user) {
         this.addNoteUser(user, NoteUser.Role.OWNER);
    }

    public void removeNoteUser(User user) {
        var noteUserToDelete = this.getNoteUsers().stream().filter(noteUser -> noteUser.getUser().equals(user)).findFirst();
        if (noteUserToDelete.isPresent()) {
            this.noteUsers.remove(noteUserToDelete.get());
            user.getNotes().remove(noteUserToDelete.get());
        } else {
            throw new RuntimeException("User is not a member of this note");
        }
    }

    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Note note = (Note) obj;
        return note.getId().equals(this.getId());
    }

}
