package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceRequest;
import doan.ptit.programmingtrainingcenter.entity.Attendance;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public List<Attendance> getAllAttendances() {
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/{id}")
    public Attendance getAttendance(@PathVariable String id) {
        return attendanceService.getAttendanceById(id);
    }

//    @PostMapping
//    public Attendance addAttendance(@RequestBody AttendanceRequest attendanceRequest) {
//        return attendanceService.addAttendance(attendanceRequest);
//    }
//
//    @PutMapping("/{id}")
//    public Attendance updateAttendance(@PathVariable String id, @RequestBody AttendanceRequest attendanceRequest) {
//        return attendanceService.updateAttendance(id, attendanceRequest);
//    }

    @DeleteMapping("/{id}")
    public boolean deleteAttendance(@PathVariable String id) {
        attendanceService.deleteAttendance(id);
        return true;
    }

    @PostMapping("/checkin")
    public ResponseEntity<String> checkIn(@RequestParam String sessionId) {

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String result = attendanceService.checkIn(sessionId,currentUser.getId());
        if (result.equals("Check-in successful!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
