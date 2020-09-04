package com.soccer.ws.service;

import net.fortuna.ical4j.model.Calendar;

import java.net.SocketException;
import java.util.UUID;

/**
 * Created by u0090265 on 1/26/15.
 */
public interface VCalendarService {
    Calendar getMatchesCalendar(UUID seasonId) throws SocketException;
}
