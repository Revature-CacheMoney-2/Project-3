package com.revature.cachemoney.backend.beans.repositories;

import com.revature.cachemoney.backend.beans.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is a repository to handle CRUD operations tied to notifications.
 */
@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer> {
    /**
     * This saves a notification to the database and returns the notification that was added.
     * @param notification this takes in the notification to save.
     * @return
     */
    Notification save(Notification notification);

    /**
     * This returns an ArrayList of all notifications.
     * @return
     */
    List<Notification> findAll();

    /**
     * This returns an ArrayList of all notifications for a specific user.
     * @param user_id this is the path variable used to identify which user's notification(s) to access.
     * @return
     */
    @Query("FROM Notification WHERE has_read = false AND user_id = :user_id")
    List<Notification> findAllByUnread(int user_id);

    /**
     * This updates a notification tied to a specific user.
     * @param user_id this is the path variable used to identify which user's notification(s) to update.
     */
    @Modifying
    @Query("UPDATE Notification SET has_read = true where user_id = :user_id")
    void updateNotifications(int user_id);
}
