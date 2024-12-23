package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.ConsultRequest;
import doan.ptit.programmingtrainingcenter.entity.Consult;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.ConsultRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultServiceImpl implements ConsultService {

    @Autowired
    private ConsultRepository consultRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Consult createConsult(ConsultRequest consultRequest) {
        Consult consult = Consult.builder()
                .fullName(consultRequest.getFullName())
                .email(consultRequest.getEmail())
                .status(consultRequest.getStatus())
                .phoneNumber(consultRequest.getPhoneNumber())
                .requestMessage(consultRequest.getRequestMessage())
                .build();
        return consultRepository.save(consult);
    }

    @Override
    public Consult updateConsult(String consultantId ,String consultId , ConsultRequest consultRequest) {
        // Kiểm tra xem bản ghi cần cập nhật có tồn tại không
        Consult existingConsult = consultRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("Consult record not found"));
        User consultant = userRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("User record not found"));

        existingConsult.setStatus(consultRequest.getStatus());
        existingConsult.setConsultant(consultant);

        // Lưu thay đổi vào cơ sở dữ liệu
        return consultRepository.save(existingConsult);
    }

    @Override
    public List<Consult> getAllConsults() {
        return consultRepository.findAll();
    }

    @Override
    public Consult getConsultById(String consultId) {
        return consultRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("Consult record not found"));
    }

    @Override
    public void deleteConsultById(String consultId) {
        Consult existingConsult = consultRepository.findById(consultId)
                .orElseThrow(() -> new IllegalArgumentException("Consult record not found"));
        consultRepository.delete(existingConsult);
    }


}
