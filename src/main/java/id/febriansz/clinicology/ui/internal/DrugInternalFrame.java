/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.ui.internal;

import id.febriansz.clinicology.Constant;
import id.febriansz.clinicology.data.entity.Drug;
import id.febriansz.clinicology.utils.DialogUtils;
import java.awt.event.ActionEvent;
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
public class DrugInternalFrame extends javax.swing.JInternalFrame {

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory(Constant.PERSISTENCE_UNIT);
    private final EntityManager manager = factory.createEntityManager();

    private List<Drug> drugs;

    public DrugInternalFrame() {
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

        txtDrugId.addActionListener(saveAction);
        txtName.addActionListener(saveAction);
        txtPrice.addActionListener(saveAction);
        btnSave.addActionListener(saveAction);

        txtKeyword.addActionListener(fetchTableAction);
        btnSearch.addActionListener(fetchTableAction);

        btnReset.addActionListener((e) -> reset());
        btnDelete.addActionListener((e) -> delete());

        tblDrug.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int index = tblDrug.getSelectedRow();
            if (index >= 0) {
                selectDrugFromTable(index);
            }
        });
    }

    private void reset() {
        txtDrugId.setText("");
        txtName.setText("");
        cmbCategory.setSelectedIndex(0);
        txtPrice.setText("");
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

        txtDrugId.setEnabled(true);

        txtDrugId.requestFocus();
    }

    private void enableEditState() {
        btnSearch.setEnabled(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(true);
        btnReset.setEnabled(true);

        btnSave.setText("Ubah");

        txtDrugId.setEnabled(false);

        txtName.requestFocus();
    }

    private void selectDrugFromTable(int index) {
        Drug selectedDrug = drugs.get(index);

        txtDrugId.setText(selectedDrug.getId());
        txtName.setText(selectedDrug.getName());
        cmbCategory.setSelectedItem(selectedDrug.getCategory());
        txtPrice.setText(String.valueOf(selectedDrug.getPrice()));

        enableEditState();
    }

    private boolean isFormCompleted() {
        return !(txtDrugId.getText().isBlank()
                || txtName.getText().isBlank()
                || cmbCategory.getSelectedIndex() < 0
                || txtPrice.getText().isBlank());
    }

    private void fetchTable() {
        try {
            TypedQuery<Drug> query;

            String keyword = txtKeyword.getText();
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
            // nothing
        }
    }

    private void save() {
        if (!isFormCompleted()) {
            DialogUtils.showWarning(this, "Form belum lengkap");
            return;
        }

        boolean isNew = txtDrugId.isEnabled();

        try {
            manager.getTransaction().begin();

            Drug drug;

            if (isNew) {
                drug = new Drug();
                drug.setId(txtDrugId.getText());
            } else {
                drug = manager.find(Drug.class, txtDrugId.getText());
            }

            drug.setName(txtName.getText());
            drug.setCategory(cmbCategory.getSelectedItem().toString());
            drug.setPrice(Integer.parseInt(txtPrice.getText()));

            manager.persist(drug);
            manager.getTransaction().commit();

            if (isNew) {
                DialogUtils.showSuccess(this, "Obat " + drug.getName() + " berhasil disimpan");
            } else {
                DialogUtils.showSuccess(this, "Obat " + drug.getName() + " berhasil diubah");
            }

            reset();
        } catch (Exception e) {
            DialogUtils.showError(this, "Error " + e.getLocalizedMessage());
        }
    }

    private void delete() {
        String drugId = txtDrugId.getText();

        try {
            Drug drug = manager.find(Drug.class, drugId);

            manager.getTransaction().begin();
            manager.remove(drug);
            manager.getTransaction().commit();

            DialogUtils.showSuccess(this, "Obat " + drug.getName() + " berhasil dihapus");
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
        tblDrug = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtDrugId = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        cmbCategory = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        tblDrug.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Obat", "Nama", "Kategori", "Price"
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
        tblDrug.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblDrug);

        jLabel1.setText("Kode Obat:");

        jLabel2.setText("Nama Obat:");

        jLabel3.setText("Kategori:");

        jLabel4.setText("Harga:");

        btnSave.setBackground(new java.awt.Color(30, 136, 229));
        btnSave.setText("Simpan");

        btnReset.setText("Reset");

        btnDelete.setBackground(new java.awt.Color(225, 119, 26));
        btnDelete.setText("Hapus");

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Antibiotik", "Anti Nyeri", "Batuk & Flu", "Peradangan" }));
        cmbCategory.setSelectedIndex(-1);

        jLabel5.setText("Cari Kode / Nama Obat:");

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtDrugId)
                    .addComponent(txtName)
                    .addComponent(txtPrice)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnReset)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave))
                    .addComponent(cmbCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDrugId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(5, 5, 5)
                        .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReset)
                            .addComponent(btnDelete)
                            .addComponent(btnSave)))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDrug;
    private javax.swing.JTextField txtDrugId;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables

}
