package com.fareed.javanote.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Entity(name = "NoteHistory")
@Table(name = "note_histories")
public @Data class NoteHistory extends SuperNote implements Comparable<NoteHistory> {
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ManyToOne(optional=false)
    @JoinColumn(name="author_id", referencedColumnName="id")
    private User author;

    public NoteHistory(Note note, User author) {
        this.note = note;
        this.author = author;
        this.setTitle(note.getTitle());
        this.setContent(note.getContent()); 
        this.createdAt = new Date();
    }


    public NoteHistory() {
    }




    @Override
    public int compareTo(NoteHistory o) {
        return this.createdAt.compareTo(((NoteHistory)o).getCreatedAt());
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
        NoteHistory history = (NoteHistory) obj;
        return history.getId().equals(this.getId());
    }
}
