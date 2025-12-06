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
public class PaymentFactory {

    public static Payment createPayment(
            String method,
            Integer orderId,
            Integer amountPaid,
            Integer changeAmount,
            String extraInfo) {

        return switch (method.toLowerCase()) {
            case "cash" ->
                new CashPayment(orderId, amountPaid);
            case "e-wallet" ->
                new EwalletPayment(orderId, amountPaid, extraInfo);
            case "debit" ->
                new DebitPayment(orderId, amountPaid, extraInfo);
            default ->
                throw new IllegalArgumentException("Metode pembayaran tidak dikenal: " + method);
        };
    }
}
