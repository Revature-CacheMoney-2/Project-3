package com.revature.cachemoney.backend.beans.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class NotificationTest {
    @Mock
    User user;
    @InjectMocks
    Notification notification;





    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSetNotif_id() {
        notification.setNotif_id(0);
    }

    @Test
    void testSetSubject() {
        notification.setSubject("subject");
    }

    @Test
    void testSetNotif_text() {
        notification.setNotif_text("notif_text");
    }

    @Test
    void testSetUser() {
        notification.setUser(new User("firstName", "lastName", "email", "password", "username"));
    }

    @Test
    void testSetHas_read() {
        notification.setHas_read(true);
    }

    @Test
    void testSetDate() {
        notification.setDate("date");
    }

    @Test
    void testEquals() {

        Notification notif = new Notification(0, null, null, user, false, null);
        boolean result = notification.equals(notif);
        Assertions.assertTrue(result);
    }




    @Test
    void testHashCode() {
        int result = notification.hashCode();
        Assertions.assertEquals(notification.hashCode(), result);
    }

    @Test
    void testToString() {
        String result = notification.toString();
        Assertions.assertEquals("Notification(notif_id=0, subject=null, notif_text=null, user=user, has_read=false, date=null)", result);
    }
}

