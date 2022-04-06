package com.revature.cachemoney.backend.beans.services;

import com.revature.cachemoney.backend.beans.models.Notification;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.repositories.NotificationRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class NotificationServiceTest {
    @Mock
    NotificationRepo notificationRepo;
    @Mock
    User user;
    @Mock
    NotificationService notificationService;
    @InjectMocks
    NotificationService notificationService1;

    /**
     * This is setting up the data to be mocked prior to each test.
     * Each test in this suite of tests, tests CRUD operations for
     * the notification class/model
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This test ensures data retrieval for all notifications is working.
     */
    @Test
    void testFindAll() {
        List<Notification> notifsList = Arrays.asList(new Notification( 0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date"),
                new Notification( 1, "subject1", "notif_text1", new User("firstName1", "lastName1", "email1", "password1", "username1"), true, "date1"));
        when(notificationRepo.findAll()).thenReturn(notifsList);

        List<Notification> result = notificationService1.findAll();
        Assertions.assertEquals(notifsList, result);
    }

    /**
     * This test ensures notifications can be saved to a database.
     */
    @Test
    void testSaveNotification() {

        Notification notification = new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date");
        when(notificationRepo.save(any())).thenReturn(notification);

        Notification result = notificationService1.saveNotification(new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date"));
        Assertions.assertEquals(notification, result);
    }

    /**
     * This test ensures only unread notifications can be retrieved.
     */
    @Test
    void testFindAllByUnread() {

        List<Notification> notifsList = Arrays.asList(new Notification( 0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date"),
                new Notification( 1, "subject1", "notif_text1", new User("firstName1", "lastName1", "email1", "password1", "username1"), true, "date1"));



        when(notificationRepo.findAllByUnread(anyInt())).thenReturn(notifsList);

        List<Notification> result = notificationService1.findAllByUnread(1);
        Assertions.assertEquals(notifsList, result);
    }

    /**
     * This test ensures notifications can be updated.
     */
    @Test
    void testUpdateNotifications() {
        notificationService.updateNotifications(user);

        Notification notification = new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date");

        when(notificationRepo.save(any())).thenReturn(notification);
       // when(notificationRepo.getById(0)).thenReturn(notification);

        verify(notificationService, times(1)).updateNotifications(user);
    }
}

