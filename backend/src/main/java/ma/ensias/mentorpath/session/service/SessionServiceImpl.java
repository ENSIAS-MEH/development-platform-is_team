package ma.ensias.mentorpath.session.service;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.session.dto.SessionRequest;
import ma.ensias.mentorpath.session.dto.SessionResponse;
import ma.ensias.mentorpath.session.entity.Session;
import ma.ensias.mentorpath.session.entity.SessionStatus;
import ma.ensias.mentorpath.session.repository.SessionRepository;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des sessions.
 * Gère les demandes, acceptations et refus de sessions.
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public SessionResponse requestSession(SessionRequest request, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        User mentor = userRepository.findById(request.getMentorId())
            .orElseThrow(() -> new ResourceNotFoundException("Mentor non trouvé"));

        Session session = Session.builder()
            .student(student)
            .mentor(mentor)
            .sessionDate(request.getSessionDate())
            .message(request.getMessage())
            .status(SessionStatus.PENDING)
            .build();

        return toResponse(sessionRepository.save(session));
    }

    @Override
    public List<SessionResponse> getMySessions(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        return sessionRepository.findByStudentId(student.getId())
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SessionResponse> getMentorSchedule(String mentorEmail) {
        User mentor = userRepository.findByEmail(mentorEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor non trouvé"));

        return sessionRepository.findByMentorId(mentor.getId())
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public SessionResponse acceptSession(Long sessionId, String mentorEmail) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session non trouvée"));

        if (!session.getMentor().getEmail().equals(mentorEmail))
            throw new RuntimeException("Vous n'êtes pas le mentor de cette session");

        session.setStatus(SessionStatus.ACCEPTED);
        return toResponse(sessionRepository.save(session));
    }

    @Override
    public SessionResponse declineSession(Long sessionId, String mentorEmail) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session non trouvée"));

        if (!session.getMentor().getEmail().equals(mentorEmail))
            throw new RuntimeException("Vous n'êtes pas le mentor de cette session");

        session.setStatus(SessionStatus.DECLINED);
        return toResponse(sessionRepository.save(session));
    }

    /** Convertit une entité Session en DTO SessionResponse */
    private SessionResponse toResponse(Session session) {
        return SessionResponse.builder()
            .id(session.getId())
            .studentEmail(session.getStudent().getEmail())
            .mentorEmail(session.getMentor().getEmail())
            .sessionDate(session.getSessionDate())
            .message(session.getMessage())
            .status(session.getStatus())
            .createdAt(session.getCreatedAt())
            .build();
    }
}