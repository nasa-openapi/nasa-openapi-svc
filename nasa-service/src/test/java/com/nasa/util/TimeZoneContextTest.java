package com.nasa.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeZoneContextTest {

    @Test
    public void testSetAndGetTimeZone(){
        TimeZoneContext.setTimezone("ABC");
        Assertions.assertEquals("ABC",TimeZoneContext.getTimezone());
    }

    @Test
    public void testClearTimeZone(){
        TimeZoneContext.setTimezone("DEF");
        TimeZoneContext.clear();
        Assertions.assertEquals("UTC",TimeZoneContext.getTimezone());
    }

    @Test
    public void testMultipleThreads() throws InterruptedException{
        TimeZoneContext.setTimezone("testMain");

        Thread newThread = new Thread(()->{
           Assertions.assertEquals("UTC", TimeZoneContext.getTimezone());
           TimeZoneContext.setTimezone("testSecondThread");
           Assertions.assertEquals("testSecondThread", TimeZoneContext.getTimezone());
           TimeZoneContext.clear();
           Assertions.assertEquals("UTC", TimeZoneContext.getTimezone());
        });

        newThread.start();
        newThread.join();
        Assertions.assertEquals("testMain",TimeZoneContext.getTimezone());
    }
}
