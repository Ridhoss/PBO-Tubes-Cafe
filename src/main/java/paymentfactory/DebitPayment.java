/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paymentfactory;

import models.Payment;

/**
 *
 * @author RIDHO
 */
public class DebitPayment extends Payment {

    private String bankName;

    public DebitPayment(Integer orderId, Integer amountPaid, String bankName) {
        super(null, orderId, "Debit", amountPaid, 0, java.time.LocalDateTime.now(), bankName);
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }
}
