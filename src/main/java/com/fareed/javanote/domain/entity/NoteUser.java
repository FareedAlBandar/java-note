package com.fareed.javanote.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Hibernate;


@Entity(name = "NoteUser")
@Table(name = "note_user")
@IdClass(NoteUserId.class)
public class NoteUser {

    public static enum Role {
        OWNER, VIEWER, EDITOR
    }


    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;


    private  Role role;

    public NoteUser() {
    }


    public User getUser() {
        if (!Hibernate.isInitialized(user)) {
            Hibernate.initialize(user);
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Note getNote() {
        if (!Hibernate.isInitialized(note)) {
            Hibernate.initialize(note);
        }
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    
}
