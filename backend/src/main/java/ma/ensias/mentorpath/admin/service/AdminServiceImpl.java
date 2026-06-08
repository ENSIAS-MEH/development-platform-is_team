package ma.ensias.mentorpath.admin.service;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.admin.dto.AdminStatsResponse;
import ma.ensias.mentorpath.roadmap.repository.RoadmapRepository;
import ma.ensias.mentorpath.user.entity.Role;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoadmapRepository roadmapRepository;

    @Override
    public AdminStatsResponse getPlatformStats() {
        return AdminStatsResponse.builder()
            .students(userRepository.countByRole(Role.STUDENT))
            .mentors(userRepository.countByRole(Role.MENTOR))
            .roadmaps(roadmapRepository.count())
            .build();
    }
}
