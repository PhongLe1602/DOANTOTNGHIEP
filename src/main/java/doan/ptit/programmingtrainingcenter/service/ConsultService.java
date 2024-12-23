package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.ConsultRequest;
import doan.ptit.programmingtrainingcenter.entity.Consult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConsultService {
    Consult createConsult(ConsultRequest consultRequest);
    Consult updateConsult(String consultantId ,String consultId, ConsultRequest consultRequest);
    List<Consult> getAllConsults();
    Consult getConsultById(String consultId);
    void deleteConsultById(String consultId);
}
