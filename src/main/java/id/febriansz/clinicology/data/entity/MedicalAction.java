/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.data.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "medical_action")
@NamedQueries({
    @NamedQuery(name = "MedicalAction.findAll", query = "SELECT m FROM MedicalAction m"),
    @NamedQuery(name = "MedicalAction.findById", query = "SELECT m FROM MedicalAction m WHERE m.id = :id"),
    @NamedQuery(name = "MedicalAction.findByAction", query = "SELECT m FROM MedicalAction m WHERE m.action = :action"),
    @NamedQuery(name = "MedicalAction.findByUpdatedAt", query = "SELECT m FROM MedicalAction m WHERE m.updatedAt = :updatedAt")})
public class MedicalAction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actionId")
    private List<MedicalRecord> medicalRecordList;

    public MedicalAction() {
    }

    public MedicalAction(Short id) {
        this.id = id;
    }

    public MedicalAction(Short id, String action, Date updatedAt) {
        this.id = id;
        this.action = action;
        this.updatedAt = updatedAt;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MedicalRecord> getMedicalRecordList() {
        return medicalRecordList;
    }

    public void setMedicalRecordList(List<MedicalRecord> medicalRecordList) {
        this.medicalRecordList = medicalRecordList;
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
        if (!(object instanceof MedicalAction)) {
            return false;
        }
        MedicalAction other = (MedicalAction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.MedicalAction[ id=" + id + " ]";
    }

}
