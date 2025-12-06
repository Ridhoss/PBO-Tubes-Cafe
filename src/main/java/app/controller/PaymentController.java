/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.PaymentDao;
import app.services.ProductsDao;
import dao.GenericDaoImpl;
import java.time.LocalDateTime;
import java.util.List;
import models.Payment;

/**
 *
 * @author RIDHO
 */
public class PaymentController {
    
    private static PaymentController instance;
    
    private final PaymentDao paymentDao;
    
    public PaymentController() {
        paymentDao = new PaymentDao();
    }
    
    public static synchronized PaymentController getInstance() {
        if (instance == null) {
            instance = new PaymentController();
        }
        return instance;
    }
    
    public void addPayment(Integer orderId, String paymentMethod, Integer amountPaid,
            Integer changeAmount, String info) throws Exception {
        
        Payment p = new Payment();
        p.setOrder_id(orderId);
        p.setPayment_method(paymentMethod);
        p.setAmount_paid(amountPaid);
        p.setChange_amount(changeAmount);
        p.setInfo_payment(info);
        
        paymentDao.insert(p);
    }
    
    public void addPaymentNew(Payment p) throws Exception {
        paymentDao.insert(p);
    }
    
    public void updatePayment(Integer paymentId, Integer orderId, String paymentMethod,
            Integer amountPaid, Integer changeAmount, String info) throws Exception {
        
        Payment existing = paymentDao.findById(paymentId);
        
        if (existing == null) {
            throw new Exception("Payment ID " + paymentId + " tidak ditemukan");
        }
        
        existing.setOrder_id(orderId);
        existing.setPayment_method(paymentMethod);
        existing.setAmount_paid(amountPaid);
        existing.setChange_amount(changeAmount);
        existing.setInfo_payment(info);
        
        paymentDao.update(existing);
    }
    
    public void deletePayment(Integer paymentId) throws Exception {
        Payment existing = paymentDao.findById(paymentId);
        
        if (existing == null) {
            throw new Exception("Payment ID " + paymentId + " tidak ditemukan");
        }
        
        paymentDao.delete(paymentId);
    }
    
    public Payment findByPaymentId(Integer paymentId) throws Exception {
        Payment p = paymentDao.findById(paymentId);
        
        if (p == null) {
            throw new Exception("Payment ID " + paymentId + " tidak ditemukan");
        }
        
        return p;
    }
    
    public Payment findByOrderId(Integer orderId) throws Exception {
        Payment p = paymentDao.findByOrderId(orderId);
        
        if (p == null) {
            throw new Exception("Payment untuk Order ID " + orderId + " tidak ditemukan");
        }
        
        return p;
    }
    
    public List<Payment> getAllPayments() throws Exception {
        return paymentDao.findAll();
    }
    
}
