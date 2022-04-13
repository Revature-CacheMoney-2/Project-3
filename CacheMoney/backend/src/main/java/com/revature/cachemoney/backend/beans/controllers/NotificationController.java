package com.revature.cachemoney.backend.beans.controllers;

import com.revature.cachemoney.backend.beans.annotations.RequireJwt;
import com.revature.cachemoney.backend.beans.models.Notification;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is a controller to perform actions on the Notifications class.
 */
@CrossOrigin
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    NotificationService notificationService;

    /**
     * This is a spring bean for the notification service layer.
     * @param notificationService this returns a notificationService bean.
     */
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * This is a method for returning all notifications.
     * @return
     */
    @GetMapping("/all")
    List<Notification> findAll() {
        return notificationService.findAll();
    }

    /**
     * This is a method that returns notifications for the specific user.
     * @param user_id this parameter is a supplied path variable and is specific to the user's ID.
     * @return
     */
    @GetMapping("/unread/{user_id}")
    List<Notification> findAllUnread(@PathVariable int user_id) {
        return notificationService.findAllByUnread(user_id);
    }

    /**
     * This is a method that allows for a notification to be added to the database.
     * @param notification This is a JSON request for the notification to add.
     * @return
     */
    @PostMapping("/add")
    Notification save(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    /**
     * This is a method for updating a notification
     * @param user this is a JSON request for the User to whom the notification belongs.
     */
    @PutMapping("/update")
    void updateNotifications(@RequestBody User user) {
        notificationService.updateNotifications(user);
    }
}
