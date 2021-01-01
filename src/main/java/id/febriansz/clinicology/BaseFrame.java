package id.febriansz.clinicology;

import id.febriansz.clinicology.data.entity.Account;

/**
 *
 * @author febriansz
 */
public class BaseFrame extends javax.swing.JFrame {

    public static final String PERSISTENCE_UNIT = "Clinicology_JPA";

    private static Account mAccount;

    public static Account getAccount() {
        return mAccount;
    }

    public static void setAccount(Account account) {
        mAccount = account;
    }
}
