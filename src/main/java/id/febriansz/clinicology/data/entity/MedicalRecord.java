/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
    @NamedQuery(name = "MedicalRecord.findByAction", query = "SELECT m FROM MedicalRecord m WHERE m.action = :action"),
    @NamedQuery(name = "MedicalRecord.findByWeight", query = "SELECT m FROM MedicalRecord m WHERE m.weight = :weight"),
    @NamedQuery(name = "MedicalRecord.findByHeight", query = "SELECT m FROM MedicalRecord m WHERE m.height = :height"),
    @NamedQuery(name = "MedicalRecord.findByComplaint", query = "SELECT m FROM MedicalRecord m WHERE m.complaint = :complaint"),
    @NamedQuery(name = "MedicalRecord.findByDiagnosis", query = "SELECT m FROM MedicalRecord m WHERE m.diagnosis = :diagnosis"),
    @NamedQuery(name = "MedicalRecord.findByCreatedAt", query = "SELECT m FROM MedicalRecord m WHERE m.createdAt = :createdAt"),
    @NamedQuery(name = "MedicalRecord.findByUpdatedAt", query = "SELECT m FROM MedicalRecord m WHERE m.updatedAt = :updatedAt")})
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "complaint")
    private String complaint;
    @Column(name = "diagnosis")
    private String diagnosis;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Patient patientId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recordId")
    private Collection<MedicalRecordReceipt> medicalRecordReceiptCollection;

    public MedicalRecord() {
    }

    public MedicalRecord(String id) {
        this.id = id;
    }

    public MedicalRecord(String id, String action, Date createdAt) {
        this.id = id;
        this.action = action;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
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

    public Patient getPatientId() {
        return patientId;
    }

    public void setPatientId(Patient patientId) {
        this.patientId = patientId;
    }

    public Collection<MedicalRecordReceipt> getMedicalRecordReceiptCollection() {
        return medicalRecordReceiptCollection;
    }

    public void setMedicalRecordReceiptCollection(Collection<MedicalRecordReceipt> medicalRecordReceiptCollection) {
        this.medicalRecordReceiptCollection = medicalRecordReceiptCollection;
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
