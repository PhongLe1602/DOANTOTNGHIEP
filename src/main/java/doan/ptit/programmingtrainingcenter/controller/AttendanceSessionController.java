package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceSessionRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AttendanceSessionDetailResponse;
import doan.ptit.programmingtrainingcenter.entity.AttendanceSession;

import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.AttendanceSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-session")
public class AttendanceSessionController {

    @Autowired
    private AttendanceSessionService attendanceSessionService;


    @PostMapping
    public ResponseEntity<AttendanceSession> createSession(@RequestBody AttendanceSessionRequest attendanceSessionRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AttendanceSession session = attendanceSessionService.createSession(attendanceSessionRequest,currentUser.getId());
        return ResponseEntity.ok(session);

    }

    @GetMapping("/{sessionId}/details")
    public AttendanceSessionDetailResponse getAttendanceSessionDetails(
            @PathVariable String sessionId) {
        return attendanceSessionService.getAttendanceSessionDetail(sessionId);
    }
    @GetMapping("/classes/{id}")
    public List<AttendanceSession> getAttendanceSessionsByClassId(@PathVariable String id) {
        return attendanceSessionService.getAttendanceSessionsOfClass(id);
    }
}
