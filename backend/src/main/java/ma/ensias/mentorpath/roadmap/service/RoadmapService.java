package ma.ensias.mentorpath.roadmap.service;

import ma.ensias.mentorpath.roadmap.dto.RoadmapRequest;
import ma.ensias.mentorpath.roadmap.dto.RoadmapResponse;

import java.util.List;

/**
 * Service interface pour la gestion des roadmaps.
 */
public interface RoadmapService {

    RoadmapResponse createRoadmap(RoadmapRequest request, String mentorEmail);

    RoadmapResponse updateRoadmap(Long id, RoadmapRequest request, String mentorEmail);

    void deleteRoadmap(Long id, String mentorEmail);

    List<RoadmapResponse> getAllRoadmaps(String filiere, String keyword);

    RoadmapResponse getRoadmapById(Long id);

    RoadmapResponse enrollStudent(Long roadmapId, String studentEmail);

    List<RoadmapResponse> getMyProgress(String studentEmail);
}