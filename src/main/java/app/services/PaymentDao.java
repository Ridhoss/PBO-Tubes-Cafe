/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.services;

import dao.GenericDaoImpl;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Payment;

/**
 *
 * @author RIDHO
 */
public class PaymentDao extends GenericDaoImpl<Integer, Payment> {

    public PaymentDao() {
        super(
                "payments",
                "payment_id",
                "order_id",
                "payment_method",
                "amount_paid",
                "change_amount",
                "info_payment"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, Payment entity) throws Exception {
        ps.setInt(1, entity.getOrder_id());
        ps.setString(2, entity.getPayment_method());
        ps.setInt(3, entity.getAmount_paid());
        ps.setInt(4, entity.getChange_amount());
        ps.setString(5, entity.getInfoPayment());
    }

    @Override
    protected void setIdParam(PreparedStatement ps, Payment entity, int index) throws Exception {
        ps.setInt(0, entity.getPayment_id());
    }

    @Override
    protected Payment mapResult(ResultSet rs) throws Exception {
        Payment p = new Payment();
        p.setPayment_id(rs.getInt("payment_id"));
        p.setOrder_id(rs.getInt("order_id"));
        p.setPayment_method(rs.getString("payment_method"));
        p.setAmount_paid(rs.getInt("amount_paid"));
        p.setChange_amount(rs.getInt("change_amount"));
        p.setPayment_time(rs.getTimestamp("payment_time") != null
                ? rs.getTimestamp("payment_time").toLocalDateTime()
                : null);
        p.setInfo_payment(rs.getString("info_payment"));

        return p;
    }

    public Payment findByOrderId(Integer orderId) throws Exception {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResult(rs);
            }
        }
        return null;
    }

}
