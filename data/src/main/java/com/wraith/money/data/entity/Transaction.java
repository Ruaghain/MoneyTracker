package com.wraith.money.data.entity;

import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a transaction in the database.
 *
 * User: rowan.massey Date: 16/08/12
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Audited
@AuditTable(value = "Transaction_Audit")
@Table(name = "[Transaction]")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "transaction_id")),
        @AttributeOverride(name = "version", column = @Column(name = "transaction_version"))})
public class Transaction extends BaseEntity implements Serializable {

    private Payee payee;
    private Currency currency;
    private Category category;
    private int number;
    private String checkNumber;
    private Date transactionDate;
    private Double amount;
    private int quantity;
    private String notes;
    private Account account;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_payee_id")
    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_currency_id")
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_category_id")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(name = "transaction_number", nullable = false)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Column(name = "transaction_checknumber", nullable = true)
    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    @Column(name = "transaction_date", nullable = false)
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Column(name = "transaction_amount", nullable = false)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "transaction_quantity", nullable = true)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Column(name = "transaction_notes", nullable = true)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_account_id", nullable = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
