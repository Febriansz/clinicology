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
@Table(name = "doctor_speciality")
@NamedQueries({
    @NamedQuery(name = "DoctorSpeciality.findAll", query = "SELECT d FROM DoctorSpeciality d"),
    @NamedQuery(name = "DoctorSpeciality.findById", query = "SELECT d FROM DoctorSpeciality d WHERE d.id = :id"),
    @NamedQuery(name = "DoctorSpeciality.findBySpeciality", query = "SELECT d FROM DoctorSpeciality d WHERE d.speciality = :speciality"),
    @NamedQuery(name = "DoctorSpeciality.findByUpdatedAt", query = "SELECT d FROM DoctorSpeciality d WHERE d.updatedAt = :updatedAt")})
public class DoctorSpeciality implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "speciality")
    private String speciality;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialityId")
    private List<Doctor> doctorList;

    public DoctorSpeciality() {
    }

    public DoctorSpeciality(Short id) {
        this.id = id;
    }

    public DoctorSpeciality(Short id, String speciality, Date updatedAt) {
        this.id = id;
        this.speciality = speciality;
        this.updatedAt = updatedAt;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
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
        if (!(object instanceof DoctorSpeciality)) {
            return false;
        }
        DoctorSpeciality other = (DoctorSpeciality) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.DoctorSpeciality[ id=" + id + " ]";
    }

}
