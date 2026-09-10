package com.medbill.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase_return_items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_return_id")
    @JsonBackReference
    private PurchaseReturn purchaseReturn;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", length = 150)
    private String productName;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice = 0.0;

    private Double subtotal = 0.0;

    @Column(length = 200)
    private String reason;

    public PurchaseReturnItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PurchaseReturn getPurchaseReturn() { return purchaseReturn; }
    public void setPurchaseReturn(PurchaseReturn purchaseReturn) { this.purchaseReturn = purchaseReturn; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
