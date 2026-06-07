package ma.ensias.mentorpath.roadmap.service;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.roadmap.dto.RoadmapRequest;
import ma.ensias.mentorpath.roadmap.dto.RoadmapResponse;
import ma.ensias.mentorpath.roadmap.entity.Roadmap;
import ma.ensias.mentorpath.roadmap.entity.RoadmapEnrollment;
import ma.ensias.mentorpath.roadmap.entity.RoadmapStep;
import ma.ensias.mentorpath.roadmap.repository.RoadmapEnrollmentRepository;
import ma.ensias.mentorpath.roadmap.repository.RoadmapRepository;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des roadmaps.
 * Applique les règles métier pour la création, modification et suivi.
 */
@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Override
    public RoadmapResponse createRoadmap(RoadmapRequest request, String mentorEmail) {
        User mentor = userRepository.findByEmail(mentorEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor non trouvé"));

        Roadmap roadmap = Roadmap.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .filiere(request.getFiliere())
            .mentor(mentor)
            .build();

        if (request.getSteps() != null) {
            List<RoadmapStep> steps = request.getSteps().stream()
                .map(s -> RoadmapStep.builder()
                    .title(s.getTitle())
                    .description(s.getDescription())
                    .stepOrder(s.getStepOrder())
                    .roadmap(roadmap)
                    .build())
                .collect(Collectors.toList());
            roadmap.setSteps(steps);
        }

        return toResponse(roadmapRepository.save(roadmap), null);
    }

    @Override
    public RoadmapResponse updateRoadmap(Long id, RoadmapRequest request, String mentorEmail) {
        Roadmap roadmap = roadmapRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Roadmap non trouvée"));

        if (!roadmap.getMentor().getEmail().equals(mentorEmail))
            throw new RuntimeException("Vous n'êtes pas le propriétaire de cette roadmap");

        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setFiliere(request.getFiliere());

        return toResponse(roadmapRepository.save(roadmap), null);
    }

    @Override
    public void deleteRoadmap(Long id, String mentorEmail) {
        Roadmap roadmap = roadmapRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Roadmap non trouvée"));

        if (!roadmap.getMentor().getEmail().equals(mentorEmail))
            throw new RuntimeException("Vous n'êtes pas le propriétaire de cette roadmap");

        roadmapRepository.delete(roadmap);
    }

    @Override
    public List<RoadmapResponse> getAllRoadmaps(String filiere, String keyword) {
        boolean hasFiliere = filiere != null && !filiere.isBlank();
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        List<Roadmap> roadmaps;
        if (hasFiliere && hasKeyword) {
            roadmaps = roadmapRepository.searchByFiliereAndKeyword(filiere, keyword);
        } else if (hasFiliere) {
            roadmaps = roadmapRepository.findByFiliere(filiere);
        } else if (hasKeyword) {
            roadmaps = roadmapRepository.searchByKeyword(keyword);
        } else {
            roadmaps = roadmapRepository.findAll();
        }

        return roadmaps.stream()
            .map(r -> toResponse(r, null))
            .collect(Collectors.toList());
    }

    @Override
    public RoadmapResponse getRoadmapById(Long id) {
        Roadmap roadmap = roadmapRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Roadmap non trouvée"));
        return toResponse(roadmap, null);
    }

    @Override
    public RoadmapResponse enrollStudent(Long roadmapId, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        Roadmap roadmap = roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new ResourceNotFoundException("Roadmap non trouvée"));

        enrollmentRepository.findByStudentIdAndRoadmapId(student.getId(), roadmapId)
            .ifPresent(e -> { throw new RuntimeException("Déjà inscrit à cette roadmap"); });

        RoadmapEnrollment enrollment = RoadmapEnrollment.builder()
            .student(student)
            .roadmap(roadmap)
            .progressPercent(0)
            .build();

        enrollmentRepository.save(enrollment);
        return toResponse(roadmap, enrollment);
    }

    @Override
    public List<RoadmapResponse> getMyProgress(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        return enrollmentRepository.findByStudentId(student.getId())
            .stream()
            .map(e -> toResponse(e.getRoadmap(), e))
            .collect(Collectors.toList());
    }

    /** Convertit une entité Roadmap en DTO RoadmapResponse */
    private RoadmapResponse toResponse(Roadmap roadmap, RoadmapEnrollment enrollment) {
        List<RoadmapResponse.StepResponse> steps = roadmap.getSteps().stream()
            .map(s -> new RoadmapResponse.StepResponse(
                s.getId(), s.getTitle(), s.getDescription(), s.getStepOrder()))
            .collect(Collectors.toList());

        return RoadmapResponse.builder()
            .id(roadmap.getId())
            .title(roadmap.getTitle())
            .description(roadmap.getDescription())
            .filiere(roadmap.getFiliere())
            .mentorName(roadmap.getMentor().getEmail())
            .enrollmentCount(enrollmentRepository.findByRoadmapId(roadmap.getId()).size())
            .progressPercent(enrollment != null ? enrollment.getProgressPercent() : null)
            .steps(steps)
            .createdAt(roadmap.getCreatedAt())
            .build();
    }
}