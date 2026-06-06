package ma.ensias.mentorpath.session.service;

import ma.ensias.mentorpath.session.dto.SessionRequest;
import ma.ensias.mentorpath.session.dto.SessionResponse;

import java.util.List;

/**
 * Service interface pour la gestion des sessions de mentorat.
 */
public interface SessionService {

    SessionResponse requestSession(SessionRequest request, String studentEmail);

    List<SessionResponse> getMySessions(String studentEmail);

    List<SessionResponse> getMentorSchedule(String mentorEmail);

    SessionResponse acceptSession(Long sessionId, String mentorEmail);

    SessionResponse declineSession(Long sessionId, String mentorEmail);
}