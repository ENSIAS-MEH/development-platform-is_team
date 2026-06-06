package ma.ensias.mentorpath.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.message.dto.ConversationResponse;
import ma.ensias.mentorpath.message.dto.MessageRequest;
import ma.ensias.mentorpath.message.dto.MessageResponse;
import ma.ensias.mentorpath.message.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la messagerie interne.
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Messagerie interne entre étudiants et mentors")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Créer une conversation")
    @PostMapping("/conversations")
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(
            @RequestParam Long otherUserId,
            Authentication auth) {
        ConversationResponse response =
            messageService.createConversation(otherUserId, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Conversation créée", response));
    }

    @Operation(summary = "Voir mes conversations")
    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getMyConversations(
            Authentication auth) {
        List<ConversationResponse> response =
            messageService.getMyConversations(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Conversations récupérées", response));
    }

    @Operation(summary = "Voir les messages d'une conversation")
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(
            @PathVariable Long conversationId,
            Authentication auth) {
        List<MessageResponse> response =
            messageService.getMessages(conversationId, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Messages récupérés", response));
    }

    @Operation(summary = "Envoyer un message")
    @PostMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody MessageRequest request,
            Authentication auth) {
        MessageResponse response =
            messageService.sendMessage(conversationId, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Message envoyé", response));
    }
}