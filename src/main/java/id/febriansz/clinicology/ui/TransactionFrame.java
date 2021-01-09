/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.ui;

import id.febriansz.clinicology.Constant;
import id.febriansz.clinicology.data.entity.Drug;
import id.febriansz.clinicology.data.entity.Invoice;
import id.febriansz.clinicology.data.entity.MedicalRecord;
import id.febriansz.clinicology.data.entity.MedicalRecordReceipt;
import id.febriansz.clinicology.data.entity.Patient;
import id.febriansz.clinicology.report.InvoiceInternalFrame;
import id.febriansz.clinicology.utils.DateUtils;
import id.febriansz.clinicology.utils.DialogUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author febriansz
 */
public class TransactionFrame extends javax.swing.JFrame {

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(Constant.PERSISTENCE_UNIT);
    private final EntityManager manager = factory.createEntityManager();

    private DashboardFrame dashboard;

    private Invoice invoice;
    private Patient patient;
    private Drug selectedDrug;

    private int selectedReceiptIndex;

    private List<MedicalRecordReceipt> receipts = new ArrayList<>();
    private List<Drug> drugs;

    public TransactionFrame(DashboardFrame dashboard) {
        initComponents();
        initListener();

        if (dashboard == null) {
            System.exit(0);
        }

        this.dashboard = dashboard;

        setLocationRelativeTo(null);

        reset();
    }

    private void initListener() {
        Action addDrugAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDrug();
            }
        };

        Action fetchTableDrugAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchTableDrug();
            }
        };

        Action searchPatientAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPatient();
            }
        };

        btnDeleteDrug.addActionListener((e) -> deleteDrug());
        btnAddDrug.addActionListener((e) -> {
            dlgDrug.setTitle("Clinicology - Transaksi");
            dlgDrug.setSize(620, 300);
            dlgDrug.setLocationRelativeTo(this);
            dlgDrug.setVisible(true);

            fetchTableDrug();
        });

        txtDrugQty.addActionListener(addDrugAction);
        btnDrugAdd.addActionListener(addDrugAction);

        txtDrugKeyword.addActionListener(fetchTableDrugAction);
        btnDrugSearch.addActionListener(fetchTableDrugAction);

        txtPatientId.addActionListener(searchPatientAction);
        btnSearchPatient.addActionListener(searchPatientAction);

        cmbAction.addItemListener((ItemEvent e) -> {
            if (cmbAction.getSelectedIndex() >= 0) {
                switch (cmbAction.getSelectedItem().toString()) {
                    case Constant.MedicalAction.CONSULTATION:
                        invoice.setActionCost(Constant.MedicalActionPrice.CONSULTATION);
                        break;
                    case Constant.MedicalAction.CHECKUP:
                        invoice.setActionCost(Constant.MedicalActionPrice.CHECKUP);
                        break;
                    case Constant.MedicalAction.MINOR_SURGERY:
                        invoice.setActionCost(Constant.MedicalActionPrice.MINOR_SURGERY);
                        break;
                    default:
                        invoice.setActionCost(0);
                }
            }

            txtActionCost.setText(String.valueOf(invoice.getActionCost()));
            txtTotalCost.setText(String.valueOf(invoice.getTotalCost()));
        });

        btnSave.addActionListener((e) -> {
            dlgPayment.setTitle("Clinicology - Pembayaran");
            dlgPayment.setSize(270, 100);
            dlgPayment.setLocationRelativeTo(this);
            dlgPayment.setVisible(true);
        });

        btnCash.addActionListener((e) -> save(Constant.PaymentMethod.CASH));
        btnDebit.addActionListener((e) -> save(Constant.PaymentMethod.DEBIT));
        btnReset.addActionListener((e) -> reset());

        tblReceipt.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int index = tblReceipt.getSelectedRow();
            if (index >= 0) {
                selectReceiptFromTable(index);
            } else {
                enableDeleteState(false);
            }
        });

        tblDrug.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int index = tblDrug.getSelectedRow();
            if (index >= 0) {
                selectDrugFromTable(index);
                btnDrugAdd.setEnabled(true);
            } else {
                selectedDrug = null;
                btnDrugAdd.setEnabled(false);
            }
        });

        dlgDrug.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // nothing
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                dlgDrug.dispose();
            }
        });
    }

    private void reset() {
        txtPatientId.setText("");
        txtName.setText("");
        cmbAction.setSelectedIndex(-1);
        txtHeight.setText("");
        txtWeight.setText("");
        txtComplaint.setText("");
        txtDiagnosis.setText("");
        txtDrugKeyword.setText("");
        txtDrugsCost.setText("");
        txtActionCost.setText("");
        txtTotalCost.setText("");

        invoice = new Invoice(DateUtils.generateId());
        patient = null;
        selectedDrug = null;
        selectedReceiptIndex = -1;

        enableAddState();

        DefaultTableModel model = (DefaultTableModel) tblReceipt.getModel();
        tblReceipt.clearSelection();
        model.setRowCount(0);
    }

    private void resetDrug() {
        txtDrugKeyword.setText("");
        txtDrugName.setText("");
        txtDrugPrice.setText("");
        txtDrugQty.setText("");

        txtDrugKeyword.requestFocus();

        fetchTableDrug();
    }

    private void enableAddState() {
        btnDrugSearch.setEnabled(true);
        btnSave.setEnabled(true);
        btnReset.setEnabled(false);

        txtPatientId.requestFocus();
    }

    private void enableDeleteState(boolean enable) {
        btnAddDrug.setEnabled(!enable);
        btnDeleteDrug.setEnabled(enable);
    }

    private void selectDrugFromTable(int index) {
        selectedDrug = drugs.get(index);

        txtDrugName.setText(selectedDrug.getName());
        txtDrugPrice.setText(String.valueOf(selectedDrug.getPrice()));
        txtDrugQty.setText("");
        txtDrugKeyword.requestFocus();
    }

    private void selectReceiptFromTable(int index) {
        selectedReceiptIndex = index;

        enableDeleteState(true);
    }

    private boolean isFormCompleted() {
        return !(txtPatientId.getText().isBlank()
                || txtName.getText().isBlank()
                || cmbAction.getSelectedIndex() < 0
                || txtHeight.getText().isBlank()
                || txtWeight.getText().isBlank()
                || txtComplaint.getText().isBlank()
                || txtDiagnosis.getText().isBlank());
    }

    private void addDrug() {
        try {
            MedicalRecordReceipt receipt = new MedicalRecordReceipt();
            receipt.setDrugId(selectedDrug);
            receipt.setQuantity(Short.parseShort(txtDrugQty.getText()));

            receipts.add(receipt);
            fetchTableReceipt();
            resetDrug();
        } catch (Exception e) {
            dlgDrug.dispose();
            e.printStackTrace();
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }
    }

    private void deleteDrug() {
        tblReceipt.clearSelection();
        receipts.remove(selectedReceiptIndex);
        selectedReceiptIndex = -1;
        fetchTableReceipt();
    }

    private void searchPatient() {
        try {
            patient = null;
            txtName.setText("");

            String keyword = txtPatientId.getText();
            TypedQuery<Patient> query = manager.createNamedQuery("Patient.searchById", Patient.class);
            patient = query
                    .setParameter("id", keyword)
                    .setParameter("card_id", keyword)
                    .getSingleResult();

            if (patient == null || patient.getName().isBlank()) {
                DialogUtils.showWarning(this, "Pasien tidak ditemukan");
                return;
            }

            txtName.setText(patient.getName());
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showWarning(this, "Pasien tidak ditemukan");
        }
    }

    private void fetchTableDrug() {
        try {
            TypedQuery<Drug> query;

            String keyword = txtDrugKeyword.getText();
            if (keyword.isBlank()) {
                query = manager.createNamedQuery("Drug.finds", Drug.class);
                drugs = query.getResultList();
            } else {
                query = manager.createNamedQuery("Drug.search", Drug.class);
                drugs = query
                        .setParameter("id", keyword)
                        .setParameter("name", "%" + keyword + "%")
                        .getResultList();
            }

            DefaultTableModel model = (DefaultTableModel) tblDrug.getModel();
            tblDrug.clearSelection();
            model.setRowCount(0);

            drugs.forEach((item) -> {
                model.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getCategory(),
                    item.getPrice()
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchTableReceipt() {
        DefaultTableModel model = (DefaultTableModel) tblReceipt.getModel();
        tblReceipt.clearSelection();
        model.setRowCount(0);

        receipts.forEach((item) -> {
            model.addRow(new Object[]{
                item.getDrugId().getName(),
                item.getQuantity(),
                item.getDrugId().getPrice(),
                item.getTotal()
            });

            invoice.addDrugsCost(item.getTotal());
        });

        txtDrugsCost.setText(String.valueOf(invoice.getDrugsCost()));
        txtTotalCost.setText(String.valueOf(invoice.getTotalCost()));
    }

    private void save(String paymentMethod) {
        if (!isFormCompleted()) {
            DialogUtils.showWarning(this, "Form belum lengkap");
            return;
        }

        try {
            manager.getTransaction().begin();

            MedicalRecord record = new MedicalRecord();
            record.setId(DateUtils.generateId());
            record.setPatientId(patient);
            record.setAction(cmbAction.getSelectedItem().toString());

            double height = Double.parseDouble(txtHeight.getText());
            record.setHeight(new BigDecimal(height));

            double weight = Double.parseDouble(txtWeight.getText());
            record.setWeight(new BigDecimal(weight));

            record.setComplaint(txtComplaint.getText());
            record.setDiagnosis(txtDiagnosis.getText());

            manager.persist(record);
            manager.getTransaction().commit();

            saveMedicalRecord(record, paymentMethod);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }
    }

    private void saveMedicalRecord(MedicalRecord record, String paymentMethod) {
        try {
            manager.getTransaction().begin();

            for (int i = 0; i < receipts.size(); i++) {
                receipts.get(i).setRecordId(record);
            }

            record.setMedicalRecordReceiptCollection(receipts);

            manager.persist(record);
            manager.getTransaction().commit();

            generateInvoice(record, paymentMethod);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }

    }

    private void generateInvoice(MedicalRecord record, String paymentMethod) {
        try {
            manager.getTransaction().begin();

            invoice.setRecordId(record);
            invoice.setPaymentMethod(paymentMethod);
            invoice.setUpdatedAt(new Date());

            manager.persist(invoice);
            manager.getTransaction().commit();

            dlgPayment.dispose();
            openInvoice();

            reset();
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }
    }

    private void openInvoice() {
        JDialog dialog = new JDialog();
        InvoiceInternalFrame iFrame = new InvoiceInternalFrame(dialog, invoice);
        dialog.add(iFrame);

        iFrame.setVisible(true);
        iFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgDrug = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDrug = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtDrugKeyword = new javax.swing.JTextField();
        btnDrugSearch = new javax.swing.JButton();
        txtDrugName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDrugPrice = new javax.swing.JTextField();
        txtDrugQty = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnDrugAdd = new javax.swing.JButton();
        dlgPayment = new javax.swing.JDialog();
        btnCash = new javax.swing.JButton();
        btnDebit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnSearchPatient = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDiagnosis = new javax.swing.JTextArea();
        btnAddDrug = new javax.swing.JButton();
        cmbAction = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReceipt = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        txtPatientId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtHeight = new javax.swing.JFormattedTextField();
        txtWeight = new javax.swing.JFormattedTextField();
        btnDeleteDrug = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtComplaint = new javax.swing.JTextArea();
        txtName = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtDrugsCost = new javax.swing.JTextField();
        txtActionCost = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTotalCost = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        dlgDrug.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgDrug.setTitle("Daftar Obat");
        dlgDrug.setAlwaysOnTop(true);

        tblDrug.setFont(new java.awt.Font("Lucida Grande", 0, 13)); // NOI18N
        tblDrug.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode", "Nama", "Kategori", "Harga"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblDrug);

        jLabel5.setText("Cari Kode / Nama Obat:");

        btnDrugSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N

        txtDrugName.setEditable(false);

        jLabel3.setText("Nama Obat:");

        jLabel4.setText("Harga:");

        txtDrugPrice.setEditable(false);

        jLabel12.setText("Kuantitas:");

        btnDrugAdd.setText("Tambah");
        btnDrugAdd.setEnabled(false);

        javax.swing.GroupLayout dlgDrugLayout = new javax.swing.GroupLayout(dlgDrug.getContentPane());
        dlgDrug.getContentPane().setLayout(dlgDrugLayout);
        dlgDrugLayout.setHorizontalGroup(
            dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgDrugLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(txtDrugName, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(txtDrugPrice))
                    .addComponent(jLabel12)
                    .addComponent(txtDrugQty)
                    .addGroup(dlgDrugLayout.createSequentialGroup()
                        .addGap(0, 106, Short.MAX_VALUE)
                        .addComponent(btnDrugAdd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(dlgDrugLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDrugKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDrugSearch)))
                .addContainerGap())
        );
        dlgDrugLayout.setVerticalGroup(
            dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgDrugLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDrugKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDrugSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgDrugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgDrugLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDrugName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDrugPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDrugQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDrugAdd))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgPayment.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgPayment.setTitle("Daftar Obat");
        dlgPayment.setAlwaysOnTop(true);

        btnCash.setBackground(new java.awt.Color(225, 119, 26));
        btnCash.setText("Tunai");

        btnDebit.setBackground(new java.awt.Color(30, 136, 229));
        btnDebit.setText("Debit");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel2.setText("Pilih Metode Pembayaran");

        javax.swing.GroupLayout dlgPaymentLayout = new javax.swing.GroupLayout(dlgPayment.getContentPane());
        dlgPayment.getContentPane().setLayout(dlgPaymentLayout);
        dlgPaymentLayout.setHorizontalGroup(
            dlgPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dlgPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addGroup(dlgPaymentLayout.createSequentialGroup()
                        .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDebit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dlgPaymentLayout.setVerticalGroup(
            dlgPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(dlgPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDebit)
                    .addComponent(btnCash))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setTitle("Clinicology - Transaksi");
        setBackground(java.awt.Color.lightGray);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel6.setText("Nama Pasien:");

        jLabel10.setText("Keluhan:");

        btnSearchPatient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N

        txtDiagnosis.setColumns(20);
        txtDiagnosis.setRows(5);
        jScrollPane3.setViewportView(txtDiagnosis);

        btnAddDrug.setText("Tambah Obat");

        cmbAction.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Konsultasi", "Pemeriksaan", "Operasi Kecil" }));
        cmbAction.setSelectedIndex(-1);

        jLabel11.setText("Diagnosa:");

        tblReceipt.setFont(new java.awt.Font("Lucida Grande", 0, 13)); // NOI18N
        tblReceipt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama Obat", "Kuantitas", "Harga", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblReceipt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblReceipt);

        jLabel7.setText("Tindakan:");

        btnReset.setBackground(new java.awt.Color(225, 119, 26));
        btnReset.setText("Reset");

        jLabel1.setText("Kode Pasien / Nomor KTP:");

        jLabel8.setText("Tinggi Badan:");

        btnSave.setBackground(new java.awt.Color(30, 136, 229));
        btnSave.setText("Proses Pembayaran");

        jLabel9.setText("Berat Badan:");

        txtHeight.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        txtWeight.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        btnDeleteDrug.setBackground(new java.awt.Color(225, 119, 26));
        btnDeleteDrug.setText("Hapus Obat");

        txtComplaint.setColumns(20);
        txtComplaint.setRows(5);
        jScrollPane2.setViewportView(txtComplaint);

        txtName.setEditable(false);

        jLabel13.setText("Biaya Obat:");

        txtDrugsCost.setEditable(false);
        txtDrugsCost.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        txtDrugsCost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDrugsCost.setMargin(new java.awt.Insets(0, 0, 0, 10));

        txtActionCost.setEditable(false);
        txtActionCost.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        txtActionCost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtActionCost.setMargin(new java.awt.Insets(0, 0, 0, 10));

        jLabel14.setText("Biaya Tindakan:");

        txtTotalCost.setEditable(false);
        txtTotalCost.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        txtTotalCost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCost.setMargin(new java.awt.Insets(0, 0, 0, 10));

        jLabel15.setText("Total Biaya:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtPatientId, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearchPatient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel7)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel8))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9)
                                                .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(cmbAction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtName)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3))
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSave))
                    .addComponent(txtTotalCost)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(txtDrugsCost, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(txtActionCost, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAddDrug, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteDrug)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnAddDrug)
                    .addComponent(btnDeleteDrug))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPatientId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearchPatient))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                            .addComponent(txtDrugsCost)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtActionCost)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        dashboard.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TransactionFrame(null).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDrug;
    private javax.swing.JButton btnCash;
    private javax.swing.JButton btnDebit;
    private javax.swing.JButton btnDeleteDrug;
    private javax.swing.JButton btnDrugAdd;
    private javax.swing.JButton btnDrugSearch;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearchPatient;
    private javax.swing.JComboBox<String> cmbAction;
    private javax.swing.JDialog dlgDrug;
    private javax.swing.JDialog dlgPayment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tblDrug;
    private javax.swing.JTable tblReceipt;
    private javax.swing.JTextField txtActionCost;
    private javax.swing.JTextArea txtComplaint;
    private javax.swing.JTextArea txtDiagnosis;
    private javax.swing.JTextField txtDrugKeyword;
    private javax.swing.JTextField txtDrugName;
    private javax.swing.JTextField txtDrugPrice;
    private javax.swing.JTextField txtDrugQty;
    private javax.swing.JTextField txtDrugsCost;
    private javax.swing.JFormattedTextField txtHeight;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPatientId;
    private javax.swing.JTextField txtTotalCost;
    private javax.swing.JFormattedTextField txtWeight;
    // End of variables declaration//GEN-END:variables
}
