package com.app.reserva.reservadesalauem.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mamoru on 11/12/2015.
 */
public class DateUtils {
    // utilizdades relacionadas ao uso de data

    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date date = calendar.getTime();
        return date;
    }

    public static String dateToString(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        Date date = calendar.getTime();

        //DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        //DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        //String dt = format.format(date);
        String dt = new SimpleDateFormat("dd/MM/yyyy").format(date);

        return dt;
    }

    public static String dateToString(Date date) {

        //DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        //DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = new SimpleDateFormat("dd/MM/yyyy").format(date);

        return dt;
    }

}
