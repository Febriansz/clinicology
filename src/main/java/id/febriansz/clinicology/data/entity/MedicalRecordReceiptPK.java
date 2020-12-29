/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.data.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author febriansz
 */
@Embeddable
public class MedicalRecordReceiptPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "record_id")
    private String recordId;
    @Basic(optional = false)
    @Column(name = "drug_id")
    private String drugId;

    public MedicalRecordReceiptPK() {
    }

    public MedicalRecordReceiptPK(String recordId, String drugId) {
        this.recordId = recordId;
        this.drugId = drugId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        hash += (drugId != null ? drugId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicalRecordReceiptPK)) {
            return false;
        }
        MedicalRecordReceiptPK other = (MedicalRecordReceiptPK) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        if ((this.drugId == null && other.drugId != null) || (this.drugId != null && !this.drugId.equals(other.drugId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.MedicalRecordReceiptPK[ recordId=" + recordId + ", drugId=" + drugId + " ]";
    }

}
