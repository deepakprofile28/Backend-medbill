package com.medbill.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "store_configurations")
public class StoreConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false)
    private String storeName = "Apollo Pharmacy";

    @Column(name = "store_subtitle")
    private String storeSubtitle = "Clinical & Billing Hub";

    @Column(name = "logo_url", columnDefinition = "LONGTEXT")
    private String logoUrl;

    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol = "₹";

    @Column(name = "currency_code", length = 10)
    private String currencyCode = "INR";

    @Column(name = "phone", length = 25)
    private String phone = "7358441198";

    @Column(name = "email", length = 100)
    private String email = "deepakprofile28@gmail.com";

    @Column(name = "address", length = 500)
    private String address = "Muthu Mari Amman Koil St, Adhi Nagar, Chennai";

    @Column(name = "city", length = 100)
    private String city = "Chennai";

    @Column(name = "state", length = 100)
    private String state = "Tamil Nadu";

    @Column(name = "pincode", length = 20)
    private String pincode = "600001";

    @Column(name = "gstin", length = 30)
    private String gstin = "33AABCA1234F1Z8";

    @Column(name = "drug_license_no", length = 50)
    private String drugLicenseNo = "TN/CHN/20B/2026/0091";

    @Column(name = "invoice_prefix", length = 20)
    private String invoicePrefix = "INV-";

    @Column(name = "default_receipt_type", length = 20)
    private String defaultReceiptType = "thermal";

    @Column(name = "default_gst_rate")
    private Double defaultGstRate = 12.0;

    @Column(name = "bill_greeting", length = 255)
    private String billGreeting = "*** WISHING YOU A SPEEDY RECOVERY! GET WELL SOON ***";

    @Column(name = "return_policy_terms", columnDefinition = "TEXT")
    private String returnPolicyTerms = "Terms: Goods once sold cannot be returned without original bill. Please check expiry before consumption.";

    @Column(name = "enable_round_off")
    private Boolean enableRoundOff = true;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ================= GETTERS AND SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreSubtitle() {
        return storeSubtitle;
    }

    public void setStoreSubtitle(String storeSubtitle) {
        this.storeSubtitle = storeSubtitle;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getDrugLicenseNo() {
        return drugLicenseNo;
    }

    public void setDrugLicenseNo(String drugLicenseNo) {
        this.drugLicenseNo = drugLicenseNo;
    }

    public String getInvoicePrefix() {
        return invoicePrefix;
    }

    public void setInvoicePrefix(String invoicePrefix) {
        this.invoicePrefix = invoicePrefix;
    }

    public String getDefaultReceiptType() {
        return defaultReceiptType;
    }

    public void setDefaultReceiptType(String defaultReceiptType) {
        this.defaultReceiptType = defaultReceiptType;
    }

    public Double getDefaultGstRate() {
        return defaultGstRate;
    }

    public void setDefaultGstRate(Double defaultGstRate) {
        this.defaultGstRate = defaultGstRate;
    }

    public String getBillGreeting() {
        return billGreeting;
    }

    public void setBillGreeting(String billGreeting) {
        this.billGreeting = billGreeting;
    }

    public String getReturnPolicyTerms() {
        return returnPolicyTerms;
    }

    public void setReturnPolicyTerms(String returnPolicyTerms) {
        this.returnPolicyTerms = returnPolicyTerms;
    }

    public Boolean getEnableRoundOff() {
        return enableRoundOff;
    }

    public void setEnableRoundOff(Boolean enableRoundOff) {
        this.enableRoundOff = enableRoundOff;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

