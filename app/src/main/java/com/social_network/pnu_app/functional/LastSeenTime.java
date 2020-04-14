package com.social_network.pnu_app.functional;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;

public class LastSeenTime {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "був у мережі прямо зараз";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "був у мережі хвилину тому";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "був у мережі " + diff / MINUTE_MILLIS + " хвилин тому";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "був у мережі годину назад";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "був у мережі " + diff / HOUR_MILLIS + " годин тому";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "був у мережі вчора";
        } else {
            return "був у мережі " + diff / DAY_MILLIS + " днів тому";
        }
    }

    public String getTimeMessenger(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;

         if (diff < 24 * HOUR_MILLIS) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            df.format(time);
            return  df.format(time);
        }


        else if (diff < 48 * HOUR_MILLIS) {
            return "був у мережі вчора";
        }


        else {
            return "був у мережі " + diff / DAY_MILLIS + " днів тому";
        }
    }
}
