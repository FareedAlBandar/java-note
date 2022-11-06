package com.fareed.javanote.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;

@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters Long")
    private String password;

    
    @OneToMany(mappedBy = "user")
    private List<NoteUser> notes = new ArrayList<>();

    public User() {
    }
    
    //constructor 
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        
    }

    public List<NoteUser> getNotes() {
        if (!Hibernate.isInitialized(notes)) {
            Hibernate.initialize(notes);
        }
        return notes;
    }

    public void setNotes(List<NoteUser> notes) {
        this.notes = notes;
    }

    public void addNote(NoteUser note) {
        this.notes.add(note);
    }

    public void removeNote(NoteUser note) {
        this.notes.remove(note);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
