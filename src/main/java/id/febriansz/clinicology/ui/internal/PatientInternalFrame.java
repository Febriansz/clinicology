/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.ui.internal;

import id.febriansz.clinicology.Constant;
import id.febriansz.clinicology.data.entity.Patient;
import id.febriansz.clinicology.utils.DateUtils;
import id.febriansz.clinicology.utils.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author febriansz
 */
public class PatientInternalFrame extends javax.swing.JInternalFrame {

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(Constant.PERSISTENCE_UNIT);
    private final EntityManager manager = factory.createEntityManager();

    private List<Patient> patients;

    public PatientInternalFrame() {
        initComponents();
        initListener();

        reset();
    }

    private void initListener() {
        Action saveAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        };

        Action fetchTableAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchTable();
            }
        };

        txtPatientId.addActionListener(saveAction);
        txtCardId.addActionListener(saveAction);
        txtPhone.addActionListener(saveAction);
        btnSave.addActionListener(saveAction);

        txtKeyword.addActionListener(fetchTableAction);
        btnSearch.addActionListener(fetchTableAction);

        btnReset.addActionListener((e) -> reset());
        btnDelete.addActionListener((e) -> delete());

        tblPatient.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int index = tblPatient.getSelectedRow();
            if (index >= 0) {
                selectPatientFromTable(index);
            }
        });
    }

    private void reset() {
        txtPatientId.setText(DateUtils.generateId());
        txtCardId.setText("");
        txtName.setText("");
        cmbGender.setSelectedIndex(-1);
        txtBirthDate.setText(DateUtils.format(new Date(), Constant.DateFormat.DATE_INDONESIAN));
        txtPhone.setText("");
        txtAddress.setText("");
        txtKeyword.setText("");

        enableAddState();
        fetchTable();
    }

    private void enableAddState() {
        btnSearch.setEnabled(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        btnReset.setEnabled(false);

        btnSave.setText("Simpan");

        txtCardId.setEditable(true);

        txtPatientId.requestFocus();
    }

    private void enableEditState() {
        btnSearch.setEnabled(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(true);
        btnReset.setEnabled(true);

        btnSave.setText("Ubah");

        txtCardId.setEditable(false);

        txtCardId.requestFocus();
    }

    private void selectPatientFromTable(int index) {
        Patient selectedPatient = patients.get(index);

        txtPatientId.setText(selectedPatient.getId());
        txtCardId.setText(selectedPatient.getCardId());
        txtName.setText(selectedPatient.getName());

        if (selectedPatient.getGender().equals(Patient.Gender.MALE)) {
            cmbGender.setSelectedItem(Constant.Gender.MALE);
        } else {
            cmbGender.setSelectedItem(Constant.Gender.FEMALE);
        }

        String birthDate = DateUtils.format(selectedPatient.getBirthDate(), Constant.DateFormat.DATE_INDONESIAN);
        txtBirthDate.setText(birthDate);

        txtPhone.setText(String.valueOf(selectedPatient.getPhoneNumber()));
        txtAddress.setText(String.valueOf(selectedPatient.getAddress()));

        enableEditState();
    }

    private boolean isFormCompleted() {
        return !(txtPatientId.getText().isBlank()
                || txtCardId.getText().isBlank()
                || cmbGender.getSelectedIndex() < 0
                || txtPhone.getText().isBlank());
    }

    private void fetchTable() {
        try {
            TypedQuery<Patient> query;

            String keyword = txtKeyword.getText();
            if (keyword.isBlank()) {
                query = manager.createNamedQuery("Patient.finds", Patient.class);
                patients = query.getResultList();
            } else {
                query = manager.createNamedQuery("Patient.search", Patient.class);
                patients = query
                        .setParameter("id", keyword)
                        .setParameter("card_id", keyword)
                        .setParameter("name", "%" + keyword + "%")
                        .getResultList();
            }

            DefaultTableModel model = (DefaultTableModel) tblPatient.getModel();
            tblPatient.clearSelection();
            model.setRowCount(0);

            patients.forEach((item) -> {
                model.addRow(new Object[]{
                    item.getId(),
                    item.getCardId(),
                    item.getName(),
                    DateUtils.format(item.getBirthDate(), Constant.DateFormat.DATE_INDONESIAN)
                });
            });
        } catch (Exception e) {
            // nothing
        }
    }

    private void save() {
        if (!isFormCompleted()) {
            DialogUtils.showWarning(this, "Form belum lengkap");
            return;
        }

        boolean isNew = txtPatientId.isEnabled();

        try {
            manager.getTransaction().begin();

            Patient patient;

            if (isNew) {
                patient = new Patient();
                patient.setId(txtPatientId.getText());
            } else {
                patient = manager.find(Patient.class, txtPatientId.getText());
            }

            patient.setCardId(txtCardId.getText());
            patient.setName(txtName.getText());

            if (cmbGender.getSelectedItem().toString().equals(Constant.Gender.MALE)) {
                patient.setGender(Patient.Gender.MALE);
            } else {
                patient.setGender(Patient.Gender.FEMALE);
            }

            Date birthDate = DateUtils.parse(txtBirthDate.getText(), Constant.DateFormat.DATE_INDONESIAN);
            patient.setBirthDate(birthDate);

            patient.setPhoneNumber(txtPhone.getText());
            patient.setAddress(txtAddress.getText());

            manager.persist(patient);
            manager.getTransaction().commit();

            if (isNew) {
                DialogUtils.showSuccess(this, "Pasien " + patient.getName() + " berhasil disimpan");
            } else {
                DialogUtils.showSuccess(this, "Pasien " + patient.getName() + " berhasil diubah");
            }

            reset();
        } catch (Exception e) {
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }
    }

    private void delete() {
        String patientId = txtPatientId.getText();

        try {
            Patient patient = manager.find(Patient.class, patientId);

            manager.getTransaction().begin();
            manager.remove(patient);
            manager.getTransaction().commit();

            DialogUtils.showSuccess(this, "Pasien " + patient.getName() + " berhasil dihapus");
            reset();
        } catch (Exception e) {
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblPatient = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPatientId = new javax.swing.JTextField();
        txtCardId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        cmbGender = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtBirthDate = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();

        tblPatient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Pasien", "No. KTP", "Nama", "Tgl. Lahir"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tblPatient.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblPatient);

        jLabel1.setText("Kode Pasien:");

        txtPatientId.setEditable(false);

        jLabel2.setText("Nomor KTP:");

        jLabel3.setText("Jenis Kelamin:");

        jLabel4.setText("Tanggal Lahir:");

        btnSave.setBackground(new java.awt.Color(30, 136, 229));
        btnSave.setText("Simpan");

        btnReset.setText("Reset");

        btnDelete.setBackground(new java.awt.Color(225, 119, 26));
        btnDelete.setText("Hapus");

        cmbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki - Laki", "Perempuan" }));

        jLabel5.setText("Cari Kode / KTP / Nama:");

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N

        jLabel6.setText("Nama Pasien:");

        jLabel7.setText("Nomor Handphone:");

        txtBirthDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));

        jLabel8.setText("Alamat:");

        txtAddress.setColumns(20);
        txtAddress.setRows(5);
        jScrollPane2.setViewportView(txtAddress);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtBirthDate)
                                .addGap(18, 18, 18))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnSave)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtPatientId)
                                        .addComponent(txtCardId)
                                        .addComponent(txtName)
                                        .addComponent(txtPhone)
                                        .addComponent(jLabel8)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnReset)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete)
                                .addGap(105, 105, 105)))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPatientId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCardId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReset)
                            .addComponent(btnDelete)
                            .addComponent(btnSave)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPatient;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JFormattedTextField txtBirthDate;
    private javax.swing.JTextField txtCardId;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPatientId;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables

}
