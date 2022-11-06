package com.fareed.javanote.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fareed.javanote.domain.entity.Note;
import com.fareed.javanote.domain.entity.User;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    
    public List<Note> findByNoteUsers_User(User user);



}
