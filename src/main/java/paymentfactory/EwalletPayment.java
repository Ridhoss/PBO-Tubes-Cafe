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
public class EwalletPayment extends Payment {

    private String ewalletProvider;

    public EwalletPayment(Integer orderId, Integer amountPaid, String ewalletProvider) {
        super(null, orderId, "E-Wallet", amountPaid, 0, java.time.LocalDateTime.now(), ewalletProvider);
        this.ewalletProvider = ewalletProvider;
    }

    public String getEwalletProvider() {
        return ewalletProvider;
    }

    public void setEwalletProvider(String ewalletProvider) {
        this.ewalletProvider = ewalletProvider;
    }
}
