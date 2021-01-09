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
@Table(name = "invoice")
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.id = :id"),
    @NamedQuery(name = "Invoice.findByDrugsCost", query = "SELECT i FROM Invoice i WHERE i.drugsCost = :drugsCost"),
    @NamedQuery(name = "Invoice.findByActionCost", query = "SELECT i FROM Invoice i WHERE i.actionCost = :actionCost"),
    @NamedQuery(name = "Invoice.findByPaymentMethod", query = "SELECT i FROM Invoice i WHERE i.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Invoice.findByUpdatedAt", query = "SELECT i FROM Invoice i WHERE i.updatedAt = :updatedAt")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "drugs_cost")
    private int drugsCost;
    @Basic(optional = false)
    @Column(name = "action_cost")
    private int actionCost;
    @Basic(optional = false)
    @Column(name = "payment_method")
    private String paymentMethod;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MedicalRecord recordId;

    public Invoice() {
    }

    public Invoice(String id) {
        this.id = id;
    }

    public Invoice(String id, int drugsCost, int actionCost, String paymentMethod, Date updatedAt) {
        this.id = id;
        this.drugsCost = drugsCost;
        this.actionCost = actionCost;
        this.paymentMethod = paymentMethod;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDrugsCost() {
        return drugsCost;
    }

    public void setDrugsCost(int drugsCost) {
        this.drugsCost = drugsCost;
    }

    public int getActionCost() {
        return actionCost;
    }

    public void setActionCost(int actionCost) {
        this.actionCost = actionCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.febriansz.clinicology.data.entity.Invoice[ id=" + id + " ]";
    }

    // custom method
    public int getTotalCost() {
        return actionCost + drugsCost;
    }

    public void addDrugsCost(int drugsCost) {
        this.drugsCost += drugsCost;
    }

}
