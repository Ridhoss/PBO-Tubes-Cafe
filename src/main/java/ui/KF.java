/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ui.layout.LayoutAdmin;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Point;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import models.User;
import ui.admin.CategoryAdmin;
import ui.admin.DashboardAdmin;
import ui.admin.ProductAdmin;
import ui.admin.UserAdmin;
import ui.admin.category.EditCategory;
import ui.admin.category.TambahCategory;
import ui.admin.product.EditProduct;
import ui.admin.product.TambahProduct;
import ui.admin.user.EditUser;
import ui.admin.user.TambahUser;
import ui.cashier.CashierDashboard;
import ui.customer.ConfirmOrder;
import ui.customer.DashboardCustomer;
import ui.customer.DetailOrder;
import ui.customer.DetailProductCustomer;
import ui.customer.KeranjangCustomer;
import ui.customer.OrderMenu;
import ui.layout.CashierLayout;
import ui.layout.CustomerLayout;

/**
 *
 * @author RIDHO
 */
public class KF {

    // auth
    public static Login flogin = new Login();
    public static Register fregister = new Register();
    public static User currentUser = null;

    //admin
    public static LayoutAdmin flayoutAdmin = new LayoutAdmin();
    public static DashboardAdmin fdashAdmin = new DashboardAdmin();
    public static ProductAdmin fproductAdmin = new ProductAdmin();
    public static CategoryAdmin fCategoryAdmin = new CategoryAdmin();
    public static UserAdmin fUserAdmin = new UserAdmin();

    //category admin
    public static TambahCategory fAddCategory = new TambahCategory();
    public static EditCategory feditCategory = new EditCategory();
    //product admin
    public static TambahProduct fAddProduct = new TambahProduct();
    public static EditProduct fEditProduct = new EditProduct();
    //user admin
    public static TambahUser fAddUser = new TambahUser();
    public static EditUser fEditUser = new EditUser();

    //user 
    public static CustomerLayout flayoutCustomer = new CustomerLayout();
    public static DashboardCustomer fdashCustomer = new DashboardCustomer();
    public static DetailProductCustomer fDetailProductCustomer = new DetailProductCustomer();
    public static KeranjangCustomer fkeranjang = new KeranjangCustomer();
    public static ConfirmOrder forderpayment = new ConfirmOrder();
    public static OrderMenu fordermenu = new OrderMenu();
    public static DetailOrder fdetailorder = new DetailOrder();
    
    //cashier
    public static CashierLayout fcashierlayout = new CashierLayout();
    public static CashierDashboard fcashierDash = new CashierDashboard();

    public static void UntukInternalFrame(JPanel panelapa, JInternalFrame frameapa) {
        try {
            panelapa.removeAll();
            panelapa.add(frameapa);
            frameapa.setVisible(true);
            frameapa.setMaximum(true);
            frameapa.setBorder(null);
            ((BasicInternalFrameUI) frameapa.getUI()).setNorthPane(null);
            panelapa.repaint();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(KF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void UntukPanel(JPanel panelTarget, JPanel panelIsi) {
        panelTarget.removeAll();
        panelTarget.setLayout(new java.awt.BorderLayout());
        panelTarget.add(panelIsi, java.awt.BorderLayout.CENTER);
        panelTarget.revalidate();
        panelTarget.repaint();
    }

//    public static void UntukJdialog(JDialog dialogapa, JFrame frameapa) {
//        dialogapa.setUndecorated(true);
//        dialogapa.getRootPane().setBackground(new Color(0, 0, 0, 200));
//        dialogapa.setBackground(new Color(0, 0, 0, 180));
//        dialogapa.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        dialogapa.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//
//        int lokasix, lokasiy;
//
//        lokasix = frameapa.getLocationOnScreen().x + 7;
//        lokasiy = frameapa.getLocationOnScreen().y;
//        dialogapa.setLocation(new Point(lokasix, lokasiy));
//        dialogapa.setSize(frameapa.getWidth() - 14, frameapa.getHeight() - 8);
//        dialogapa.setVisible(true);
//    }
}
