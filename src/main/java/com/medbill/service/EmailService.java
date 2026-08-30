package com.medbill.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@medbillpro.com}")
    private String senderEmail;

    // =========================================================================
    // 1. SEND OTP VERIFICATION EMAIL
    // =========================================================================
    public CompletableFuture<Boolean> sendOtpEmail(String toEmail, String recipientName, String otp) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (toEmail == null || toEmail.trim().isEmpty() || !toEmail.contains("@")) {
                    System.out.println("EmailService: Invalid email address: " + toEmail);
                    return false;
                }

                String name = (recipientName != null && !recipientName.trim().isEmpty()) ? recipientName.trim() : "Valued Customer";
                String subject = "🔐 Your MedBill Pro Verification OTP: " + otp;

                String template = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f0fdf4; margin: 0; padding: 20px; color: #1e293b; }
                            .email-container { max-width: 580px; margin: 0 auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.06); border: 1px solid #d1fae5; }
                            .header { background: linear-gradient(135deg, #052e24 0%, #047857 100%); padding: 32px 24px; text-align: center; color: #ffffff; }
                            .header h1 { margin: 0; font-size: 24px; font-weight: 800; letter-spacing: -0.5px; }
                            .header p { margin: 6px 0 0 0; font-size: 13px; color: #a7f3d0; }
                            .content { padding: 36px 28px; }
                            .greeting { font-size: 16px; font-weight: 700; color: #0f172a; margin-bottom: 12px; }
                            .message { font-size: 14px; line-height: 1.6; color: #475569; margin-bottom: 24px; }
                            .otp-box { background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%); border: 2px dashed #059669; border-radius: 12px; padding: 20px; text-align: center; margin: 24px 0; }
                            .otp-label { font-size: 12px; font-weight: 700; text-transform: uppercase; color: #047857; letter-spacing: 1px; margin-bottom: 6px; }
                            .otp-code { font-size: 36px; font-weight: 900; letter-spacing: 8px; color: #065f46; font-family: monospace; }
                            .expiry-warning { font-size: 12.5px; color: #dc2626; font-weight: 600; text-align: center; margin-top: 8px; }
                            .security-note { background: #f8fafc; border-left: 4px solid #0d9488; padding: 12px 16px; border-radius: 6px; font-size: 12px; color: #64748b; margin-top: 24px; }
                            .footer { background: #f8fafc; padding: 20px; text-align: center; font-size: 12px; color: #94a3b8; border-top: 1px solid #e2e8f0; }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                                <h1>💊 MedBill Pro / PharmaCare</h1>
                                <p>Smart Pharmacy Billing & Healthcare Management</p>
                            </div>
                            <div class="content">
                                <div class="greeting">Hello {{name}},</div>
                                <div class="message">
                                    Thank you for registering with <strong>MedBill Pro</strong>. Please use the following One-Time Password (OTP) to verify your account:
                                </div>
                                <div class="otp-box">
                                    <div class="otp-label">Your 6-Digit Verification Code</div>
                                    <div class="otp-code">{{otp}}</div>
                                    <div class="expiry-warning">⏱️ Code valid for 10 minutes only.</div>
                                </div>
                                <div class="security-note">
                                    🔒 <strong>Security Tip:</strong> Never share your verification code with anyone. MedBill Pro staff will never ask for your OTP.
                                </div>
                            </div>
                            <div class="footer">
                                &copy; {{year}} MedBill Pro Healthcare Solutions. All rights reserved.
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

                String htmlContent = template
                    .replace("{{name}}", name)
                    .replace("{{otp}}", otp)
                    .replace("{{year}}", String.valueOf(LocalDateTime.now().getYear()));

                return sendHtmlEmail(toEmail, subject, htmlContent);

            } catch (Exception e) {
                System.err.println("EmailService sendOtpEmail error: " + e.getMessage());
                return false;
            }
        });
    }

    // =========================================================================
    // 2. SEND FORGOT PASSWORD RESET EMAIL
    // =========================================================================
    public CompletableFuture<Boolean> sendForgotPasswordEmail(String toEmail, String recipientName, String resetTokenOrOtp, String resetLink) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (toEmail == null || toEmail.trim().isEmpty()) return false;

                String name = (recipientName != null && !recipientName.trim().isEmpty()) ? recipientName.trim() : "Valued User";
                String subject = "🔑 Password Reset Request - MedBill Pro";
                String targetLink = (resetLink != null && !resetLink.isEmpty()) ? resetLink : "http://localhost:4200/login";

                String template = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: 'Segoe UI', Roboto, sans-serif; background-color: #f8fafc; margin: 0; padding: 20px; color: #1e293b; }
                            .email-container { max-width: 580px; margin: 0 auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.06); border: 1px solid #e2e8f0; }
                            .header { background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); padding: 32px 24px; text-align: center; color: #ffffff; }
                            .header h1 { margin: 0; font-size: 22px; font-weight: 800; }
                            .content { padding: 36px 28px; }
                            .btn-reset { display: inline-block; background: #059669; color: #ffffff !important; padding: 14px 32px; border-radius: 8px; font-weight: 700; text-decoration: none; margin: 20px 0; font-size: 15px; }
                            .otp-badge { display: inline-block; background: #ecfdf5; border: 1px solid #a7f3d0; padding: 8px 16px; border-radius: 6px; font-weight: 800; font-size: 20px; color: #047857; letter-spacing: 3px; }
                            .footer { background: #f8fafc; padding: 20px; text-align: center; font-size: 12px; color: #94a3b8; border-top: 1px solid #e2e8f0; }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                                <h1>MedBill Pro - Password Reset</h1>
                            </div>
                            <div class="content">
                                <p>Hello <strong>{{name}}</strong>,</p>
                                <p>We received a request to reset your MedBill Pro account password.</p>
                                <div style="text-align: center; margin: 24px 0;">
                                    <div class="otp-badge">{{otp}}</div>
                                    <br><br>
                                    <a href="{{link}}" class="btn-reset">Reset My Password</a>
                                </div>
                                <p style="font-size: 13px; color: #64748b;">If you did not request this, you can safely ignore this email. Your password will remain unchanged.</p>
                            </div>
                            <div class="footer">
                                &copy; {{year}} MedBill Pro Healthcare Solutions.
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

                String htmlContent = template
                    .replace("{{name}}", name)
                    .replace("{{otp}}", resetTokenOrOtp)
                    .replace("{{link}}", targetLink)
                    .replace("{{year}}", String.valueOf(LocalDateTime.now().getYear()));

                return sendHtmlEmail(toEmail, subject, htmlContent);

            } catch (Exception e) {
                System.err.println("EmailService sendForgotPasswordEmail error: " + e.getMessage());
                return false;
            }
        });
    }

    // =========================================================================
    // 3. SEND PATIENT / CUSTOMER MEDICAL BILL INVOICE EMAIL
    // =========================================================================
    public CompletableFuture<Boolean> sendBillInvoiceEmail(
            String toEmail,
            String patientName,
            String invoiceNo,
            String pharmacyName,
            String billDate,
            Double totalAmount,
            List<Map<String, Object>> items,
            String notes
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (toEmail == null || toEmail.trim().isEmpty()) return false;

                String pName = (patientName != null && !patientName.isEmpty()) ? patientName : "Customer";
                String sName = (pharmacyName != null && !pharmacyName.isEmpty()) ? pharmacyName : "MedBill Pharmacy Store";
                String inv = (invoiceNo != null && !invoiceNo.isEmpty()) ? invoiceNo : "INV-" + System.currentTimeMillis();
                String date = (billDate != null && !billDate.isEmpty()) ? billDate : LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"));
                double grandTotal = (totalAmount != null) ? totalAmount : 0.0;

                // Build Table Rows
                StringBuilder itemsHtml = new StringBuilder();
                if (items != null && !items.isEmpty()) {
                    int idx = 1;
                    for (Map<String, Object> item : items) {
                        String medName = String.valueOf(item.getOrDefault("name", item.getOrDefault("medicineName", "Medicine Item")));
                        String qty = String.valueOf(item.getOrDefault("quantity", item.getOrDefault("qty", "1")));
                        String price = String.valueOf(item.getOrDefault("price", item.getOrDefault("unitPrice", "0.00")));
                        String total = String.valueOf(item.getOrDefault("total", item.getOrDefault("amount", "0.00")));

                        itemsHtml.append("""
                            <tr style="border-bottom: 1px solid #f1f5f9;">
                                <td style="padding: 10px; font-size: 13px; color: #475569;">""" + idx++ + """
                                </td>
                                <td style="padding: 10px; font-size: 13px; font-weight: 600; color: #1e293b;">""" + medName + """
                                </td>
                                <td style="padding: 10px; font-size: 13px; text-align: center; color: #475569;">""" + qty + """
                                </td>
                                <td style="padding: 10px; font-size: 13px; text-align: right; color: #475569;">₹""" + price + """
                                </td>
                                <td style="padding: 10px; font-size: 13px; text-align: right; font-weight: 700; color: #059669;">₹""" + total + """
                                </td>
                            </tr>
                        """);
                    }
                } else {
                    itemsHtml.append("""
                        <tr>
                            <td colspan="5" style="padding: 14px; text-align: center; color: #94a3b8; font-size: 13px;">General Medical & Pharmacy Prescription</td>
                        </tr>
                    """);
                }

                String subject = "🧾 Medical Bill & Tax Invoice [" + inv + "] from " + sName;

                String template = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 20px; color: #1e293b; }
                            .invoice-card { max-width: 620px; margin: 0 auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.08); border: 1px solid #e2e8f0; }
                            .inv-header { background: linear-gradient(135deg, #052e24 0%, #047857 100%); padding: 28px; color: #ffffff; }
                            .inv-title { font-size: 22px; font-weight: 800; margin: 0; }
                            .inv-sub { font-size: 13px; color: #a7f3d0; margin-top: 4px; }
                            .inv-body { padding: 28px; }
                            .info-grid { display: table; width: 100%; margin-bottom: 24px; }
                            .info-col { display: table-cell; width: 50%; vertical-align: top; font-size: 13px; line-height: 1.5; color: #475569; }
                            .table-wrap { width: 100%; border-collapse: collapse; margin: 20px 0; }
                            .table-wrap th { background: #f8fafc; padding: 10px; font-size: 12px; font-weight: 700; color: #334155; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
                            .total-box { background: #ecfdf5; border: 1.5px solid #a7f3d0; border-radius: 10px; padding: 16px 20px; text-align: right; margin-top: 16px; }
                            .grand-total { font-size: 22px; font-weight: 900; color: #047857; }
                            .footer { background: #f8fafc; padding: 18px; text-align: center; font-size: 12px; color: #94a3b8; border-top: 1px solid #e2e8f0; }
                        </style>
                    </head>
                    <body>
                        <div class="invoice-card">
                            <div class="inv-header">
                                <div>
                                    <div class="inv-title">🏥 {{pharmacyName}}</div>
                                    <div class="inv-sub">GST Registered Pharmacy & Medical Store</div>
                                </div>
                            </div>
                            <div class="inv-body">
                                <div class="info-grid">
                                    <div class="info-col">
                                        <strong>Billed To:</strong><br>
                                        Patient / Customer: <strong>{{patientName}}</strong><br>
                                        Email: {{toEmail}}
                                    </div>
                                    <div class="info-col" style="text-align: right;">
                                        Invoice No: <strong>{{invoiceNo}}</strong><br>
                                        Date: {{billDate}}<br>
                                        Status: <span style="color: #059669; font-weight: 700;">PAID ✅</span>
                                    </div>
                                </div>

                                <table class="table-wrap">
                                    <thead>
                                        <tr>
                                            <th style="text-align: left;">#</th>
                                            <th style="text-align: left;">Medicine / Description</th>
                                            <th style="text-align: center;">Qty</th>
                                            <th style="text-align: right;">Rate</th>
                                            <th style="text-align: right;">Amount</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {{itemsRows}}
                                    </tbody>
                                </table>

                                <div class="total-box">
                                    <div style="font-size: 13px; color: #065f46; font-weight: 600;">Grand Total Amount:</div>
                                    <div class="grand-total">₹ {{grandTotal}}</div>
                                </div>

                                <div style="margin-top: 20px; font-size: 12px; color: #64748b; background: #f8fafc; padding: 12px; border-radius: 8px;">
                                    📌 <em>Thank you for your visit. Wish you a speedy recovery!</em>
                                </div>
                            </div>
                            <div class="footer">
                                Powered by <strong>MedBill Pro Cloud</strong> &copy; {{year}}
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

                String htmlContent = template
                    .replace("{{pharmacyName}}", sName)
                    .replace("{{patientName}}", pName)
                    .replace("{{toEmail}}", toEmail)
                    .replace("{{invoiceNo}}", inv)
                    .replace("{{billDate}}", date)
                    .replace("{{itemsRows}}", itemsHtml.toString())
                    .replace("{{grandTotal}}", String.format("%.2f", grandTotal))
                    .replace("{{year}}", String.valueOf(LocalDateTime.now().getYear()));

                return sendHtmlEmail(toEmail, subject, htmlContent);

            } catch (Exception e) {
                System.err.println("EmailService sendBillInvoiceEmail error: " + e.getMessage());
                return false;
            }
        });
    }

    // =========================================================================
    // HELPER: HTML EMAIL DISPATCHER VIA JAVAMAILSENDER
    // =========================================================================
    private boolean sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        if (mailSender == null) {
            System.out.println("--------------------------------------------------");
            System.out.println("📧 [SIMULATED EMAIL - JavaMailSender Not Configured]");
            System.out.println("To     : " + toEmail);
            System.out.println("Subject: " + subject);
            System.out.println("--------------------------------------------------");
            return true;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, "MedBill Pro Pharmacy Solutions");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            System.out.println("✅ [EMAIL SENT SUCCESSFULLY] To: " + toEmail + " | Subject: " + subject);
            return true;

        } catch (Exception e) {
            System.err.println("❌ [EMAIL SEND FAILED] To: " + toEmail + " | Error: " + e.getMessage());
            return false;
        }
    }
}
