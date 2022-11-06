package com.fareed.javanote.presentation.controller;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fareed.javanote.application.dto.NoteDTO;
import com.fareed.javanote.application.service.NoteAppService;
import com.fareed.javanote.application.service.UserAppService;
import com.fareed.javanote.domain.entity.Note;
import com.fareed.javanote.domain.entity.NoteHistory;
import com.fareed.javanote.presentation.request.NoteRequest;
import com.fareed.javanote.presentation.request.ShareRequest;
import com.fareed.javanote.presentation.response.HistoryResponse;
import com.fareed.javanote.presentation.response.NoteResponse;

@Component
@Path("/v1/notes")
public class NoteController {
    @Autowired
    private NoteAppService noteAppService;
    @Autowired
    private UserAppService userAppService;

    // private Authentication authentication;
    // public NoteController(Authentication authentication) {
    // this.authentication = authentication;
    // }
    @GET
    @Produces("application/json")
    public ResponseEntity<List<NoteResponse>> getNotes() {
        List<Note> notes = noteAppService.getUserNotes();
        return ResponseEntity.ok(NoteResponse.collection(notes, userAppService.getCurrentUser()));
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<NoteResponse> createNote(NoteRequest noteRequest) {
        NoteDTO noteDto = new NoteDTO(noteRequest.getTitle(), noteRequest.getContent());
        NoteResponse noteResponse = new NoteResponse(noteAppService.createNote(noteDto),
                userAppService.getCurrentUser());
        return ResponseEntity.ok(noteResponse);
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<NoteResponse> updateNote(@PathParam("id") Long id, NoteRequest noteRequest) {
        NoteDTO noteDto = new NoteDTO(noteRequest.getTitle(), noteRequest.getContent());
        NoteResponse noteResponse = new NoteResponse(noteAppService.updateNoteById(id, noteDto),
                userAppService.getCurrentUser());
        return ResponseEntity.ok(noteResponse);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public ResponseEntity<String> deleteNoteById(@PathParam("id") Long id) {
        noteAppService.deleteNoteById(id);
        return ResponseEntity.ok("Note deleted successfully");
    }

    @GET
    @Path("/{id}/history")
    @Produces("application/json")
    public ResponseEntity<List<HistoryResponse>> getNoteHistory(@PathParam("id") Long id) {
        List<NoteHistory> histories = noteAppService.getNoteHistory(id);
        return ResponseEntity.ok(HistoryResponse.collection(histories));
    }

    @PUT
    @Path("/{id}/history/{historyId}")
    @Produces("application/json")
    public ResponseEntity<NoteResponse> revertNoteToHistory(@PathParam("id") Long id,
            @PathParam("historyId") Long historyId) {
        NoteResponse noteResponse = new NoteResponse(noteAppService.revertNoteToHistory(id, historyId),
                userAppService.getCurrentUser());
        return ResponseEntity.ok(noteResponse);
    }

    @POST
    @Path("/{id}/share")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<String> shareNote(@PathParam("id") Long id, ShareRequest shareRequest) {

        noteAppService.shareNoteWithUser(id, shareRequest.getUsername(), shareRequest.getRole());
        return ResponseEntity.ok("Note shared successfully");

    }

    @PUT
    @Path("/{id}/share")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<String> updateShare(@PathParam("id") Long id, ShareRequest shareRequest) {

        noteAppService.changeNoteUserRole(id, shareRequest.getUsername(), shareRequest.getRole());
        return ResponseEntity.ok("Note shared successfully");

    }

}
