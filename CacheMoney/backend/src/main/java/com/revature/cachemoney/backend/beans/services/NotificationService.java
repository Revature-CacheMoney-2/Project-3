package com.revature.cachemoney.backend.beans.services;

import com.revature.cachemoney.backend.beans.models.Notification;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.repositories.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is the service layer for notifications.
 * @author Mika Nelson, Dylan Wilson, Cullen Kuch, Max Hilken, Tyler Seufert
 */
@Transactional
@Service
public class NotificationService {
    private NotificationRepo notificationRepo;

    /**
     * This is a spring bean for a notification repository object.
     * @param notificationRepo is the NotificationRepository bean
     */
    @Autowired
    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    /**
     * This is a service method to GET *ALL* Notifications.
     * @return an ArrayList of *ALL* Notifications
     */
    public List<Notification> findAll() {
        return notificationRepo.findAll();
    }

    /**
     * This a service method to POST a Notification
     * @param notification to save
     * @return notification added to the database
     */
    public Notification saveNotification(Notification notification) {
        return notificationRepo.save(notification);
    }

    /**
     * This is a service method to GET Unread Notifications for a specific User.
     * @param user_id to access a specific User
     * @return an ArrayList of notifications for the User
     */
    public List<Notification> findAllByUnread(int user_id) {
        return notificationRepo.findAllByUnread(user_id);
    }

    /**
     * This is a service method to PUT a Notification.
     * @param user to request a specific User
     */
    public void updateNotifications(User user) {
        notificationRepo.updateNotifications(user.getUserId());
    }
}