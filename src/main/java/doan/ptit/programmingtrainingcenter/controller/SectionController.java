package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.SectionRequest;
import doan.ptit.programmingtrainingcenter.entity.Section;
import doan.ptit.programmingtrainingcenter.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping
    List<Section> getSections() {
        return sectionService.getSections();
    }
    @PostMapping
    Section addSection(@RequestBody SectionRequest sectionRequest) {
        return sectionService.addSection(sectionRequest);
    }
    @GetMapping("/courses/{courseId}")
    List<Section> getSectionByCourseId(@PathVariable String courseId) {
        return sectionService.getSectionsByCourses(courseId);
    }

    @PutMapping("/{sectionId}")
    public Section updateSection(@PathVariable String sectionId, @RequestBody SectionRequest sectionRequest) {
        return sectionService.updateSection(sectionId, sectionRequest);
    }

    @DeleteMapping("/{sectionId}")
    public void deleteSection(@PathVariable String sectionId) {
        sectionService.deleteSection(sectionId);
    }
}
