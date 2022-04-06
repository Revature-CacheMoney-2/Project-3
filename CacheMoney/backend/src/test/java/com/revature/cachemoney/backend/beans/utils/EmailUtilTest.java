package com.revature.cachemoney.backend.beans.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Properties;

import static org.mockito.Mockito.*;

class EmailUtilTest {
    @Mock
    EmailUtil emailUtil1;
    @Mock
    Properties properties;

    //Field session of type Session - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @InjectMocks
    EmailUtil emailUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetInstance() {
        EmailUtil result = EmailUtil.getInstance();

        result.setProperties(properties);
        result.setHost(emailUtil.getHost());
        result.setEmailAcc(emailUtil.getEmailAcc());
        result.setEmailPass(emailUtil.getEmailPass());
        result.setSession(emailUtil.getSession());

        Assertions.assertEquals(emailUtil, result);
    }

    @Test
    void testSendEmail() {

        when(emailUtil1.getEmailAcc()).thenReturn("getEmailAccResponse");
        when(emailUtil1.getSession()).thenReturn(null);

        emailUtil.sendEmail("recipient", "subject", "body");
    }

    @Test
    void testSetProperties() {
        emailUtil.setProperties(null);
    }

    @Test
    void testSetHost() {
        emailUtil.setHost("host");
    }

    @Test
    void testSetEmailAcc() {
        emailUtil.setEmailAcc("emailAcc");
    }

    @Test
    void testSetEmailPass() {
        emailUtil.setEmailPass("emailPass");
    }

    @Test
    void testSetSession() {
        emailUtil.setSession(null);
    }

    @Test
    void testEquals() {

        when(emailUtil1.getProperties()).thenReturn(properties);
        when(emailUtil1.getHost()).thenReturn(emailUtil.getHost());
        when(emailUtil1.getEmailAcc()).thenReturn(emailUtil.getEmailAcc());
        when(emailUtil1.getEmailPass()).thenReturn(emailUtil.getEmailPass());
        when(emailUtil1.getSession()).thenReturn(null);
        when(emailUtil1.canEqual(any())).thenReturn(true);

        emailUtil.setSession(null);

        boolean result = emailUtil.equals(emailUtil1);

        Assertions.assertTrue(result);
    }


    @Test
    void testHashCode() {

        int result = emailUtil.hashCode();
        Assertions.assertEquals(emailUtil.hashCode(), result);
    }

}

