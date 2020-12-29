/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.data.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author febriansz
 */
@Entity
@Table(name = "medical_record_receipt")
@NamedQueries({
    @NamedQuery(name = "MedicalRecordReceipt.findAll", query = "SELECT m FROM MedicalRecordReceipt m"),
    @NamedQuery(name = "MedicalRecordReceipt.findByRecordId", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.medicalRecordReceiptPK.recordId = :recordId"),
    @NamedQuery(name = "MedicalRecordReceipt.findByDrugId", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.medicalRecordReceiptPK.drugId = :drugId"),
    @NamedQuery(name = "MedicalRecordReceipt.findByQuantity", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.quantity = :quantity"),
    @NamedQuery(name = "MedicalRecordReceipt.findByUpdatedAt", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.updatedAt = :updatedAt")})
public class MedicalRecordReceipt implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MedicalRecordReceiptPK medicalRecordReceiptPK;
    @Basic(optional = false)
    @Column(name = "quantity")
    private short quantity;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "drug_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Drug drug;
    @JoinColumn(name = "record_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MedicalRecord medicalRecord;

    public MedicalRecordReceipt() {
    }

    public MedicalRecordReceipt(MedicalRecordReceiptPK medicalRecordReceiptPK) {
        this.medicalRecordReceiptPK = medicalRecordReceiptPK;
    }

    public MedicalRecordReceipt(MedicalRecordReceiptPK medicalRecordReceiptPK, short quantity, Date updatedAt) {
        this.medicalRecordReceiptPK = medicalRecordReceiptPK;
        this.quantity = quantity;
        this.updatedAt = updatedAt;
    }

    public MedicalRecordReceipt(String recordId, String drugId) {
        this.medicalRecordReceiptPK = new MedicalRecordReceiptPK(recordId, drugId);
    }

    public MedicalRecordReceiptPK getMedicalRecordReceiptPK() {
        return medicalRecordReceiptPK;
    }

    public void setMedicalRecordReceiptPK(MedicalRecordReceiptPK medicalRecordReceiptPK) {
        this.medicalRecordReceiptPK = medicalRecordReceiptPK;
    }

    public short getQuantity() {
        return quantity;
    }

    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medicalRecordReceiptPK != null ? medicalRecordReceiptPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicalRecordReceipt)) {
            return false;
        }
        MedicalRecordReceipt other = (MedicalRecordReceipt) object;
        if ((this.medicalRecordReceiptPK == null && other.medicalRecordReceiptPK != null) || (this.medicalRecordReceiptPK != null && !this.medicalRecordReceiptPK.equals(other.medicalRecordReceiptPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.MedicalRecordReceipt[ medicalRecordReceiptPK=" + medicalRecordReceiptPK + " ]";
    }

}
