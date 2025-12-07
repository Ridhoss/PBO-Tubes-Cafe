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
public class CashPayment extends Payment{

    public CashPayment(Integer orderId, Integer amountPaid) {
        super(null, orderId, "Cash", amountPaid, 0, java.time.LocalDateTime.now(), "-");
    }
}
