package home.stanislavpoliakov.meet15_practice.presentation.presenter;

import android.widget.TextView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import home.stanislavpoliakov.meet15_practice.R;

public class Convert {
    private static ZoneId timeZone;

    public static void setZoneId(String timeZoneString) {
        timeZone = ZoneId.of(timeZoneString);
    }

    public static String toFormattedZoneDate(Long unixTime) {
        Date date = new Date(unixTime * 1000);
        Instant instant = date.toInstant();
        ZonedDateTime zonedDate = instant.atZone(timeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return zonedDate.format(formatter);
    }

    public static String toFormattedZoneTime(Long unixTime) {
        Date time = new Date(unixTime * 1000);
        Instant instant = time.toInstant();
        ZonedDateTime zonedTime = instant.atZone(timeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return zonedTime.format(formatter);
    }

    public static String toPercent(double value) {
        int percent = (int) (value * 100);
        return String.valueOf(percent) + "%";
    }

    public static String toIntensity(double intensity) {
        final double mmInch = 25.4;
        double mmIntencity = intensity * mmInch;
        return String.format(Locale.getDefault(),"%.2f  мм/ч", mmIntencity);
    }

    public static String toCelsius(double fahrenheit) {
        int celsius = (int) Math.round((fahrenheit - 32) * 5 / 9);
        String prefix = (celsius > 0) ? "+" : "";
        return prefix + String.valueOf(celsius) + "˚С";
    }

    public static String toMercury(double pressure) {
        int mercury = (int) Math.round(pressure * 0.750062);
        return String.valueOf(mercury) + " мм.рт.ст.";
    }

    public static String toDirection(double degrees) {
        String chars;
        
        if (degrees >= 337.5 || degrees < 22.5) chars = "С";
        else if (degrees < 67.5) chars = "С-В";
        else if (degrees < 112.5) chars = "В";
        else if (degrees < 157.5) chars = "Ю-В";
        else if (degrees < 202.5) chars = "Ю";
        else if (degrees < 247.5) chars = "Ю-З";
        else if (degrees < 292.5) chars = "З";
        else chars = "С-З";
        
        return chars;
    }

    static String toMeterPerSecond(double speed) {
        double fSpeed = speed / 2.23694;
        return String.format(Locale.getDefault(), "%.1f м/с", fSpeed);
    }

    static String toPrecipInfo(double precipProbability, double precipIntenisity) {
        StringBuilder builder = new StringBuilder();
        builder.append("Вероятность осадков: ")
                .append(toPercent(precipProbability))
                .append(", интенсивность: ")
                .append(toIntensity(precipIntenisity));
        return builder.toString();
    }

    static String toPrecipMax(double precipIntensityMax, long precipIntensityMaxTime) {
        StringBuilder builder = new StringBuilder();
        builder.append("Максимальное количестов осадков: ")
                .append(toIntensity(precipIntensityMax))
                .append(", в ")
                .append(toFormattedZoneTime(precipIntensityMaxTime));
        return builder.toString();
    }

    static String toHumidyAndDewPoint(double humidity, double dewPoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("Влажность воздуха: ")
                .append(toPercent(humidity))
                .append(", точка росы при ")
                .append(toCelsius(dewPoint));
        return builder.toString();
    }

    static String toWindInfo(double bearing, double speed, double gust) {
        StringBuilder builder = new StringBuilder();
        builder.append("Ветер: ")
                .append(toDirection(bearing))
                .append(", ")
                .append(toMeterPerSecond(speed))
                .append(", порывы до: ")
                .append(toMeterPerSecond(gust));
        return builder.toString();
    }

    static String toTempMaxInfo(double temperatureMax, long temperatureMaxTime) {
        StringBuilder builder = new StringBuilder();
        builder.append("Максимальная температура: ")
                .append(toCelsius(temperatureMax))
                .append(" в ")
                .append(toFormattedZoneTime(temperatureMaxTime));
        return builder.toString();
    }

    static String toTempMinInfo(double temperatureMin, long temperatureMinTime) {
        StringBuilder builder = new StringBuilder();
        builder.append("Минимальная температура: ")
                .append(toCelsius(temperatureMin))
                .append(" в ")
                .append(toFormattedZoneTime(temperatureMinTime));
        return builder.toString();
    }
}

