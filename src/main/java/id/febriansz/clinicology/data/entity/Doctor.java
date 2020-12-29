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
@Table(name = "doctor")
@NamedQueries({
    @NamedQuery(name = "Doctor.findAll", query = "SELECT d FROM Doctor d"),
    @NamedQuery(name = "Doctor.findById", query = "SELECT d FROM Doctor d WHERE d.id = :id"),
    @NamedQuery(name = "Doctor.findByRegistrationNo", query = "SELECT d FROM Doctor d WHERE d.registrationNo = :registrationNo"),
    @NamedQuery(name = "Doctor.findByName", query = "SELECT d FROM Doctor d WHERE d.name = :name"),
    @NamedQuery(name = "Doctor.findByPhoneCode", query = "SELECT d FROM Doctor d WHERE d.phoneCode = :phoneCode"),
    @NamedQuery(name = "Doctor.findByPhoneNumber", query = "SELECT d FROM Doctor d WHERE d.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Doctor.findByEmail", query = "SELECT d FROM Doctor d WHERE d.email = :email"),
    @NamedQuery(name = "Doctor.findByAddress", query = "SELECT d FROM Doctor d WHERE d.address = :address"),
    @NamedQuery(name = "Doctor.findByUpdatedAt", query = "SELECT d FROM Doctor d WHERE d.updatedAt = :updatedAt")})
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "registration_no")
    private String registrationNo;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "phone_code")
    private String phoneCode;
    @Basic(optional = false)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctorId")
    private List<MedicalRecord> medicalRecordList;
    @JoinColumn(name = "speciality_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DoctorSpeciality specialityId;

    public Doctor() {
    }

    public Doctor(Integer id) {
        this.id = id;
    }

    public Doctor(Integer id, String registrationNo, String name, String phoneCode, String phoneNumber, String address, Date updatedAt) {
        this.id = id;
        this.registrationNo = registrationNo;
        this.name = name;
        this.phoneCode = phoneCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public DoctorSpeciality getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(DoctorSpeciality specialityId) {
        this.specialityId = specialityId;
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
        if (!(object instanceof Doctor)) {
            return false;
        }
        Doctor other = (Doctor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.Doctor[ id=" + id + " ]";
    }

}
