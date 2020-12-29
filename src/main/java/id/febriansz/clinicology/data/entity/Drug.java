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
@Table(name = "drug")
@NamedQueries({
    @NamedQuery(name = "Drug.findAll", query = "SELECT d FROM Drug d"),
    @NamedQuery(name = "Drug.findById", query = "SELECT d FROM Drug d WHERE d.id = :id"),
    @NamedQuery(name = "Drug.findByName", query = "SELECT d FROM Drug d WHERE d.name = :name"),
    @NamedQuery(name = "Drug.findByPurpose", query = "SELECT d FROM Drug d WHERE d.purpose = :purpose"),
    @NamedQuery(name = "Drug.findBySideEffect", query = "SELECT d FROM Drug d WHERE d.sideEffect = :sideEffect"),
    @NamedQuery(name = "Drug.findByUnit", query = "SELECT d FROM Drug d WHERE d.unit = :unit"),
    @NamedQuery(name = "Drug.findByDosage", query = "SELECT d FROM Drug d WHERE d.dosage = :dosage"),
    @NamedQuery(name = "Drug.findByPrice", query = "SELECT d FROM Drug d WHERE d.price = :price"),
    @NamedQuery(name = "Drug.findByCreatedAt", query = "SELECT d FROM Drug d WHERE d.createdAt = :createdAt"),
    @NamedQuery(name = "Drug.findByUpdatedAt", query = "SELECT d FROM Drug d WHERE d.updatedAt = :updatedAt")})
public class Drug implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "purpose")
    private String purpose;
    @Column(name = "side_effect")
    private String sideEffect;
    @Column(name = "unit")
    private String unit;
    @Column(name = "dosage")
    private String dosage;
    @Basic(optional = false)
    @Column(name = "price")
    private int price;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "drug")
    private List<MedicalRecordReceipt> medicalRecordReceiptList;
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DrugBrand brandId;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DrugCategory categoryId;
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DrugClass classId;

    public Drug() {
    }

    public Drug(String id) {
        this.id = id;
    }

    public Drug(String id, String name, String purpose, int price, Date createdAt) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.price = price;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public List<MedicalRecordReceipt> getMedicalRecordReceiptList() {
        return medicalRecordReceiptList;
    }

    public void setMedicalRecordReceiptList(List<MedicalRecordReceipt> medicalRecordReceiptList) {
        this.medicalRecordReceiptList = medicalRecordReceiptList;
    }

    public DrugBrand getBrandId() {
        return brandId;
    }

    public void setBrandId(DrugBrand brandId) {
        this.brandId = brandId;
    }

    public DrugCategory getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(DrugCategory categoryId) {
        this.categoryId = categoryId;
    }

    public DrugClass getClassId() {
        return classId;
    }

    public void setClassId(DrugClass classId) {
        this.classId = classId;
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
        if (!(object instanceof Drug)) {
            return false;
        }
        Drug other = (Drug) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.Drug[ id=" + id + " ]";
    }

}
