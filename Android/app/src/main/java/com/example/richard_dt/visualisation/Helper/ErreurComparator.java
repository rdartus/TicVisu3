package com.example.richard_dt.visualisation.Helper;


import java.util.Comparator;

public class ErreurComparator {

    public static Comparator<ErreurItem> getSalleComparator() {
        return new SalleComparator();
    }
    public static dateComparator getStringComparator() {
        return new dateComparator();
    }

    public static intensiteComparator getIntensiteComparator() {
        return new intensiteComparator();
    }

    public static ErreurTypeComparator getErreurComparator() {
        return new ErreurTypeComparator();
    }
    private static class SalleComparator implements Comparator<ErreurItem> {



        @Override
        public int compare(ErreurItem salle1, ErreurItem salle2) {
            return salle1.getClassroom_name().compareTo(salle2.getClassroom_name());
        }
    }
    private static class dateComparator implements Comparator<ErreurItem> {


        @Override
        public int compare(ErreurItem lhs, ErreurItem rhs) {
            return lhs.getCours_date().split("//")[1].compareTo(rhs.getCours_date().split("//")[1]);
        }
    }
    private static class intensiteComparator implements Comparator<ErreurItem> {



        @Override
        public int compare(ErreurItem salle1, ErreurItem salle2) {
            return salle1.getIntensite().compareTo(salle2.getIntensite());
        }
    }
    private static class ErreurTypeComparator implements Comparator<ErreurItem> {



        @Override
        public int compare(ErreurItem salle1, ErreurItem salle2) {
            return salle1.getErreurType().compareTo(salle2.getErreurType());
        }
    }
}