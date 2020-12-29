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
@Table(name = "drug_brand")
@NamedQueries({
    @NamedQuery(name = "DrugBrand.findAll", query = "SELECT d FROM DrugBrand d"),
    @NamedQuery(name = "DrugBrand.findById", query = "SELECT d FROM DrugBrand d WHERE d.id = :id"),
    @NamedQuery(name = "DrugBrand.findByBrand", query = "SELECT d FROM DrugBrand d WHERE d.brand = :brand"),
    @NamedQuery(name = "DrugBrand.findByUpdatedAt", query = "SELECT d FROM DrugBrand d WHERE d.updatedAt = :updatedAt")})
public class DrugBrand implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "brand")
    private String brand;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brandId")
    private List<Drug> drugList;

    public DrugBrand() {
    }

    public DrugBrand(Short id) {
        this.id = id;
    }

    public DrugBrand(Short id, String brand, Date updatedAt) {
        this.id = id;
        this.brand = brand;
        this.updatedAt = updatedAt;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Drug> getDrugList() {
        return drugList;
    }

    public void setDrugList(List<Drug> drugList) {
        this.drugList = drugList;
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
        if (!(object instanceof DrugBrand)) {
            return false;
        }
        DrugBrand other = (DrugBrand) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.DrugBrand[ id=" + id + " ]";
    }

}
