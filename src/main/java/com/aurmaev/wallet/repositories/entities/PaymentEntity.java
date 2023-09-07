package com.aurmaev.wallet.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue
    private long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private AccountEntity receiverAccount;
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private AccountEntity senderAccount;

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public AccountEntity getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(AccountEntity receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public AccountEntity getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(AccountEntity senderAccount) {
        this.senderAccount = senderAccount;
    }
}
