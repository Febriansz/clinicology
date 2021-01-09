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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @NamedQuery(name = "MedicalRecordReceipt.findById", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.id = :id"),
    @NamedQuery(name = "MedicalRecordReceipt.findByQuantity", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.quantity = :quantity"),
    @NamedQuery(name = "MedicalRecordReceipt.findByUpdatedAt", query = "SELECT m FROM MedicalRecordReceipt m WHERE m.updatedAt = :updatedAt")})
public class MedicalRecordReceipt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantity")
    private short quantity;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "drug_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Drug drugId;
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MedicalRecord recordId;

    public MedicalRecordReceipt() {
    }

    public MedicalRecordReceipt(Integer id) {
        this.id = id;
    }

    public MedicalRecordReceipt(Integer id, short quantity, Date updatedAt) {
        this.id = id;
        this.quantity = quantity;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Drug getDrugId() {
        return drugId;
    }

    public void setDrugId(Drug drugId) {
        this.drugId = drugId;
    }

    public MedicalRecord getRecordId() {
        return recordId;
    }

    public void setRecordId(MedicalRecord recordId) {
        this.recordId = recordId;
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
        if (!(object instanceof MedicalRecordReceipt)) {
            return false;
        }
        MedicalRecordReceipt other = (MedicalRecordReceipt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.MedicalRecordReceipt[ id=" + id + " ]";
    }

    // custom class
    public int getTotal() {
        return quantity * drugId.getPrice();
    }

}
