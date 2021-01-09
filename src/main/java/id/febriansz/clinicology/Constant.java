/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology;

/**
 *
 * @author febriansz
 */
public class Constant {

    public static final String PERSISTENCE_UNIT = "Clinicology_JPA";

    public class MedicalAction {

        public static final String CONSULTATION = "Konsultasi";
        public static final String CHECKUP = "Pemeriksaan";
        public static final String MINOR_SURGERY = "Operasi Kecil";
    }

    public class MedicalActionPrice {

        public static final int CONSULTATION = 50000;
        public static final int CHECKUP = 75000;
        public static final int MINOR_SURGERY = 250000;
    }

    public class Gender {

        public static final String MALE = "Laki - Laki";
        public static final String FEMALE = "Perempuan";
    }

    public class DateFormat {

        public static final String DATE_INDONESIAN = "dd/MM/yyyy";
        public static final String DATE_US = "yyyy-MM-dd";
    }

    public class PaymentMethod {

        public static final String CASH = "Cash";
        public static final String DEBIT = "Debit";
    }
}
