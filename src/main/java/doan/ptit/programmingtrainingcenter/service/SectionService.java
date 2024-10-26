package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.SectionRequest;
import doan.ptit.programmingtrainingcenter.entity.Section;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectionService {
    List<Section> getSections();
    Section addSection(SectionRequest sectionRequest);
    List<Section> getSectionsByCourses(String courseId);
}
