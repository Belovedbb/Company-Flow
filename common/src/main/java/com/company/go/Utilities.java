package com.company.go;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class Utilities {

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public enum State{
        NEW,
        VIEW,
        EDIT;
    }

    public enum FilterCondition{
        NONE,
        AND,
        OR
    }

    @Getter
    @Setter
    static public class Filter<T>{
        Class<T> klass;
        Map<String, Object> properties;
    }

    static public DateTimeFormatter getDateTimeFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    }

    static public DateTimeFormatter getDateFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    static public DateTimeFormatter getISODateTimeFormatter(){
        return DateTimeFormatter.ISO_DATE_TIME;
    }

    static public boolean isWithinRange(LocalDate factor, LocalDate startDate, LocalDate endDate){
        return  factor.isAfter(startDate) && factor.isBefore(endDate);
    }

    static public boolean isWithinRangeInclusive(LocalDate factor, LocalDate startDate, LocalDate endDate){
        return  (factor.isAfter(startDate) || factor.isEqual(startDate)) && (factor.isBefore(endDate) || factor.isEqual(endDate));
    }

}
