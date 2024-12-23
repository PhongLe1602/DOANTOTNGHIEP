package doan.ptit.programmingtrainingcenter.controller;

import doan.ptit.programmingtrainingcenter.dto.request.ConsultRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ConsultResponse;
import doan.ptit.programmingtrainingcenter.entity.Consult;
import doan.ptit.programmingtrainingcenter.mapper.UserMapper;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consults")
public class ConsultController {

    @Autowired
    private ConsultService consultService;

    @Autowired
    private UserMapper userMapper;


    @PostMapping
    public ResponseEntity<Consult> createConsult(@RequestBody ConsultRequest consultRequest) {
        Consult createdConsult = consultService.createConsult(consultRequest);
        return ResponseEntity.ok(createdConsult);
    }


    @PutMapping("/{id}")
    public ConsultResponse updateConsult(
            @PathVariable("id") String consultId,
            @RequestBody ConsultRequest consultRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Consult updatedConsult = consultService.updateConsult(currentUser.getId(),consultId, consultRequest);
        return ConsultResponse.builder()
                .fullName(updatedConsult.getFullName())
                .email(updatedConsult.getEmail())
                .phoneNumber(updatedConsult.getPhoneNumber())
                .requestMessage(updatedConsult.getRequestMessage())
                .status(updatedConsult.getStatus())
                .consultant(userMapper.toUserResponse(updatedConsult.getConsultant()))
                .build();
    }


    @GetMapping
    public ResponseEntity<List<Consult>> getAllConsults() {
        List<Consult> consults = consultService.getAllConsults();
        return ResponseEntity.ok(consults);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Consult> getConsultById(@PathVariable("id") String consultId) {
        Consult consult = consultService.getConsultById(consultId);
        return ResponseEntity.ok(consult);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultById(@PathVariable("id") String consultId) {
        consultService.deleteConsultById(consultId);
        return ResponseEntity.noContent().build();
    }
}
