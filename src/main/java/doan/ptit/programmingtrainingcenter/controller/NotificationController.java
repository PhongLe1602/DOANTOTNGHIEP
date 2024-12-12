package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.NotificationRequest;
import doan.ptit.programmingtrainingcenter.dto.response.NotificationRecipientResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserNotificationResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import doan.ptit.programmingtrainingcenter.entity.NotificationRecipient;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;



    @GetMapping
    public List<Notification> getNotifications() {
        return notificationService.getNotifications();
    }


    @PostMapping
    public Notification createNotification(@RequestBody NotificationRequest notificationRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return notificationService.createNotification(currentUser.getId(),notificationRequest);
    }



    @GetMapping("/recipient")
    public UserNotificationResponse getNotificationsOfRecipient() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return notificationService.getNotificationsOfRecipient(currentUser.getId());
    }
    @PutMapping("/mark-all-read")
    public boolean markAsALLReadByUser() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        notificationService.markAsALLReadByUser(currentUser.getId());
        return true;
    }
    @PutMapping("/mark-read/{notificationId}")
    public boolean markAsReadByUser(@PathVariable String notificationId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        notificationService.markAsReadByUser(currentUser.getId(), notificationId);
        return true;
    }

}
