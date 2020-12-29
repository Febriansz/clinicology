/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author febriansz
 */
@Entity
@Table(name = "medical_record")
@NamedQueries({
    @NamedQuery(name = "MedicalRecord.findAll", query = "SELECT m FROM MedicalRecord m"),
    @NamedQuery(name = "MedicalRecord.findById", query = "SELECT m FROM MedicalRecord m WHERE m.id = :id"),
    @NamedQuery(name = "MedicalRecord.findByWeight", query = "SELECT m FROM MedicalRecord m WHERE m.weight = :weight"),
    @NamedQuery(name = "MedicalRecord.findByHeight", query = "SELECT m FROM MedicalRecord m WHERE m.height = :height"),
    @NamedQuery(name = "MedicalRecord.findByComplaint", query = "SELECT m FROM MedicalRecord m WHERE m.complaint = :complaint"),
    @NamedQuery(name = "MedicalRecord.findByExamination", query = "SELECT m FROM MedicalRecord m WHERE m.examination = :examination"),
    @NamedQuery(name = "MedicalRecord.findByDiagnosis", query = "SELECT m FROM MedicalRecord m WHERE m.diagnosis = :diagnosis"),
    @NamedQuery(name = "MedicalRecord.findByMedicineAllergy", query = "SELECT m FROM MedicalRecord m WHERE m.medicineAllergy = :medicineAllergy"),
    @NamedQuery(name = "MedicalRecord.findByLeaveCondition", query = "SELECT m FROM MedicalRecord m WHERE m.leaveCondition = :leaveCondition"),
    @NamedQuery(name = "MedicalRecord.findByCreatedAt", query = "SELECT m FROM MedicalRecord m WHERE m.createdAt = :createdAt"),
    @NamedQuery(name = "MedicalRecord.findByUpdatedAt", query = "SELECT m FROM MedicalRecord m WHERE m.updatedAt = :updatedAt")})
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "complaint")
    private String complaint;
    @Column(name = "examination")
    private String examination;
    @Column(name = "diagnosis")
    private String diagnosis;
    @Column(name = "medicine_allergy")
    private String medicineAllergy;
    @Column(name = "leave_condition")
    private String leaveCondition;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ClinicProfile clinicId;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Doctor doctorId;
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MedicalAction actionId;
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Patient patientId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicalRecord")
    private List<MedicalRecordReceipt> medicalRecordReceiptList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recordId")
    private List<Invoice> invoiceList;

    public MedicalRecord() {
    }

    public MedicalRecord(String id) {
        this.id = id;
    }

    public MedicalRecord(String id, Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicineAllergy() {
        return medicineAllergy;
    }

    public void setMedicineAllergy(String medicineAllergy) {
        this.medicineAllergy = medicineAllergy;
    }

    public String getLeaveCondition() {
        return leaveCondition;
    }

    public void setLeaveCondition(String leaveCondition) {
        this.leaveCondition = leaveCondition;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ClinicProfile getClinicId() {
        return clinicId;
    }

    public void setClinicId(ClinicProfile clinicId) {
        this.clinicId = clinicId;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public MedicalAction getActionId() {
        return actionId;
    }

    public void setActionId(MedicalAction actionId) {
        this.actionId = actionId;
    }

    public Patient getPatientId() {
        return patientId;
    }

    public void setPatientId(Patient patientId) {
        this.patientId = patientId;
    }

    public List<MedicalRecordReceipt> getMedicalRecordReceiptList() {
        return medicalRecordReceiptList;
    }

    public void setMedicalRecordReceiptList(List<MedicalRecordReceipt> medicalRecordReceiptList) {
        this.medicalRecordReceiptList = medicalRecordReceiptList;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicalRecord)) {
            return false;
        }
        MedicalRecord other = (MedicalRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.MedicalRecord[ id=" + id + " ]";
    }

}
