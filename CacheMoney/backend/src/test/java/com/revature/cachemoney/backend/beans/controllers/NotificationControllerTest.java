package com.revature.cachemoney.backend.beans.controllers;

import com.revature.cachemoney.backend.beans.models.Notification;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.services.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class NotificationControllerTest {
    @Mock
    NotificationService notificationService;
    @Mock
    User user;
    @Mock
    NotificationController notificationController1;


    @InjectMocks
    NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAll() {

        List<Notification> notifsList = Arrays.asList(new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date"),
        new Notification( 1, "subject1", "notif_text1", new User("firstName1", "lastName1", "email1", "password1", "username1"), true, "date1"));

        when(notificationService.findAll()).thenReturn(notifsList);

        List<Notification> result = notificationController.findAll();
        Assertions.assertEquals(notifsList, result);
    }

    @Test
    void testFindAllUnread() {

        List<Notification> notifsList = Arrays.asList(new Notification( 0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date"),
                new Notification( 1, "subject1", "notif_text1", new User("firstName1", "lastName1", "email1", "password1", "username1"), true, "date1"));




        when(notificationService.findAllByUnread(anyInt())).thenReturn(notifsList);

        List<Notification> result = notificationController.findAllUnread(1);
        Assertions.assertEquals(notifsList, result);
    }

    @Test
    void testSave() {
        Notification notification = new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date");

        when(notificationService.saveNotification(any())).thenReturn(notification);

        Notification result = notificationController.save(new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date"));
        Assertions.assertEquals(notification, result);
    }

    @Test
    void testUpdateNotifications() {
        notificationController1.updateNotifications(user);

        Notification notification = new Notification(0, "subject", "notif_text", new User("firstName", "lastName", "email", "password", "username"), true, "date");
        when(notificationService.saveNotification(any())).thenReturn(notification);

        verify(notificationController1, times(1)).updateNotifications(user);

    }
}

