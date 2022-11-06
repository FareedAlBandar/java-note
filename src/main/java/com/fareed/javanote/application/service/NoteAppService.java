package com.fareed.javanote.application.service;

import java.util.List;
import java.util.NoSuchElementException;

// import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import com.fareed.javanote.application.dto.NoteDTO;
import com.fareed.javanote.domain.entity.Note;
import com.fareed.javanote.domain.entity.NoteHistory;
import com.fareed.javanote.domain.entity.NoteUser;
import com.fareed.javanote.domain.entity.User;
import com.fareed.javanote.domain.repository.NoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NoteAppService {

    @Autowired
    UserAppService userAppService;

    @Autowired
    NoteRepository noteRepository;

    private Boolean isNoteOwner(Note note) {
        return note.getNoteUsers().stream()
                .anyMatch(noteUser -> noteUser.getUser().equals(userAppService.getCurrentUser())
                        && noteUser.getRole().equals(NoteUser.Role.OWNER));
    }

    private Boolean canEdit(Note note) {
        return note.getNoteUsers().stream()
                .anyMatch(noteUser -> noteUser.getUser().equals(userAppService.getCurrentUser())
                        && (noteUser.getRole().equals(NoteUser.Role.OWNER)
                                || noteUser.getRole().equals(NoteUser.Role.EDITOR)));
    }

    private void auditedSave(Note note) {
        NoteHistory noteHistory = new NoteHistory(note, userAppService.getCurrentUser());
        note.addHistory(noteHistory);
        noteRepository.save(note);
    }

    public List<Note> getUserNotes() {

        User user = userAppService.getCurrentUser();
        return noteRepository.findByNoteUsers_User(user);
    }

    public Note createNote(NoteDTO noteDTO) {
        User user = userAppService.getCurrentUser();
        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        auditedSave(note);
        note.setOwner(user);
        noteRepository.save(note);
        return note;
    }

    public void shareNoteWithUser(Long id, String username, String rolename) throws UsernameNotFoundException,
            NoSuchElementException, AuthorizationServiceException, IllegalArgumentException {
        User user = userAppService.getByUsername(username);
        Note note = noteRepository.findById(id).get();
        NoteUser.Role role = NoteUser.Role.valueOf(rolename);

        if (!isNoteOwner(note)) {
            throw new AuthorizationServiceException("You are not the owner of this note");
        }

        if (note.getNoteUsers().stream().anyMatch(noteUser -> noteUser.getUser().equals(user))) {
            throw new IllegalArgumentException("Note is already shared with user");
        }

        if (role.equals(NoteUser.Role.OWNER)) {
            throw new IllegalArgumentException("Cannot share note with owner role");
        }

        note.addNoteUser(user, role);
        noteRepository.save(note);
    }

    public void changeNoteUserRole(Long id, String username, String rolename) throws UsernameNotFoundException,
    NoSuchElementException, AuthorizationServiceException, IllegalArgumentException {
        User user = userAppService.getByUsername(username);
        Note note = noteRepository.findById(id).get();
        NoteUser.Role role = NoteUser.Role.valueOf(rolename);

        if (!isNoteOwner(note)) {
            throw new AuthorizationServiceException("You are not the owner of this note");
        }

        if (role.equals(NoteUser.Role.OWNER)) {
            throw new IllegalArgumentException("Cannot share note with owner role");
        }

        NoteUser noteUser = note.getNoteUsers().stream().filter(nu -> nu.getUser().equals(user)).findFirst().get();
        if (noteUser.getRole().equals(NoteUser.Role.OWNER)) {
            throw new IllegalArgumentException("Cannot change owner role");
        }

        noteUser.setRole(role);
        noteRepository.save(note);
    }

    public Note updateNoteById(Long id, NoteDTO noteDto) throws NoSuchElementException, AuthorizationServiceException {
        Note note = noteRepository.findById(id).get();

        if (!canEdit(note)) {
            throw new AuthorizationServiceException("User can't edit this note");
        }

        if (!noteDto.getTitle().isEmpty()) {
            note.setTitle(noteDto.getTitle());
        }
        if (!noteDto.getContent().isEmpty()) {
            note.setContent(noteDto.getContent());
        }
        auditedSave(note);
        return note;
    }

    public List<NoteHistory> getNoteHistory(Long id) {
        Note note = noteRepository.findById(id).get();
        if (!isNoteOwner(note)) {
            throw new AuthorizationServiceException("User is not owner of this note");
        }
        return note.getHistories();
    }

    public Note revertNoteToHistory(Long id, Long historyId) {
        Note note = noteRepository.findById(id).get();
        NoteHistory history = note.getHistories().stream().filter(h -> h.getId().equals(historyId)).findFirst().get();
        if (!isNoteOwner(note)) {
            throw new AuthorizationServiceException("User is not owner of this note");
        }
        note.setTitle(history.getTitle());
        note.setContent(history.getContent());
        auditedSave(note);
        return note;
    }

    public void deleteNoteById(Long id) {
        Note note = noteRepository.findById(id).get();
        if (!isNoteOwner(note)) {
            throw new AuthorizationServiceException("User is not owner of this note");
        }
        noteRepository.delete(note);
    }

    

}
