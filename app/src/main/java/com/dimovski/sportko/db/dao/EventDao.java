package com.dimovski.sportko.db.dao;

import android.arch.persistence.room.*;
import com.dimovski.sportko.db.converter.Converters;
import com.dimovski.sportko.db.model.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert (Event event);

    @Delete
    void delete (Event event);

    @Update
    void update (Event event);

    @Query("SELECT * FROM events ORDER BY scheduled")
    List<Event> getAll ();

    @TypeConverters(Converters.class)
    @Query("SELECT * FROM events WHERE scheduled > :date")
    List<Event> getUpcoming( Date date);

    @Query("SELECT * FROM events WHERE id = :eventId")
    Event getEvent(String eventId);

    @TypeConverters(Converters.class)
    @Query("SELECT * FROM events WHERE city = :city AND scheduled > :date")
    List<Event> getUpcomingEventsForCity(Date date, String city); //todo get only upcoming

    @TypeConverters(Converters.class)
    @Query("SELECT * FROM events WHERE attendees LIKE :user")
    List<Event> getEventsForUser(String user);

}
