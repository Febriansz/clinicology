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
@Table(name = "clinic_profile")
@NamedQueries({
    @NamedQuery(name = "ClinicProfile.findAll", query = "SELECT c FROM ClinicProfile c"),
    @NamedQuery(name = "ClinicProfile.findById", query = "SELECT c FROM ClinicProfile c WHERE c.id = :id"),
    @NamedQuery(name = "ClinicProfile.findByName", query = "SELECT c FROM ClinicProfile c WHERE c.name = :name"),
    @NamedQuery(name = "ClinicProfile.findByBranch", query = "SELECT c FROM ClinicProfile c WHERE c.branch = :branch"),
    @NamedQuery(name = "ClinicProfile.findByAddress", query = "SELECT c FROM ClinicProfile c WHERE c.address = :address"),
    @NamedQuery(name = "ClinicProfile.findByPhoneCode", query = "SELECT c FROM ClinicProfile c WHERE c.phoneCode = :phoneCode"),
    @NamedQuery(name = "ClinicProfile.findByPhoneNumber", query = "SELECT c FROM ClinicProfile c WHERE c.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "ClinicProfile.findByUpdatedAt", query = "SELECT c FROM ClinicProfile c WHERE c.updatedAt = :updatedAt")})
public class ClinicProfile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "branch")
    private String branch;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "phone_code")
    private String phoneCode;
    @Basic(optional = false)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicId")
    private List<MedicalRecord> medicalRecordList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicId")
    private List<Account> accountList;

    public ClinicProfile() {
    }

    public ClinicProfile(Short id) {
        this.id = id;
    }

    public ClinicProfile(Short id, String name, String branch, String address, String phoneCode, String phoneNumber, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.branch = branch;
        this.address = address;
        this.phoneCode = phoneCode;
        this.phoneNumber = phoneNumber;
        this.updatedAt = updatedAt;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
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
        if (!(object instanceof ClinicProfile)) {
            return false;
        }
        ClinicProfile other = (ClinicProfile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.ClinicProfile[ id=" + id + " ]";
    }

}
