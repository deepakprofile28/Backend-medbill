package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medbill.entity.Company;

import com.medbill.entity.SalesTransaction;
import com.medbill.entity.SalesOrder;
import com.medbill.entity.SuspendedOrder;
import com.medbill.entity.CompletedSale;
import com.medbill.entity.SalesExchange;
import com.medbill.entity.SalesReturn;

import com.medbill.repository.CompanyRepository;
import com.medbill.repository.SalesTransactionRepository;
import com.medbill.repository.SalesOrderRepository;
import com.medbill.repository.SuspendedOrderRepository;
import com.medbill.repository.CompletedSaleRepository;
import com.medbill.repository.SalesExchangeRepository;
import com.medbill.repository.SalesReturnRepository;

@Service
public class SalesService {

    private final SalesTransactionRepository salesTransactionRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SuspendedOrderRepository suspendedOrderRepository;
    private final CompletedSaleRepository completedSaleRepository;
    private final SalesExchangeRepository salesExchangeRepository;
    private final SalesReturnRepository salesReturnRepository;
    private final CompanyRepository companyRepository;

    public SalesService(
            SalesTransactionRepository salesTransactionRepository,
            SalesOrderRepository salesOrderRepository,
            SuspendedOrderRepository suspendedOrderRepository,
            CompletedSaleRepository completedSaleRepository,
            SalesExchangeRepository salesExchangeRepository,
            SalesReturnRepository salesReturnRepository,
            CompanyRepository companyRepository) {
        this.salesTransactionRepository = salesTransactionRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.suspendedOrderRepository = suspendedOrderRepository;
        this.completedSaleRepository = completedSaleRepository;
        this.salesExchangeRepository = salesExchangeRepository;
        this.salesReturnRepository = salesReturnRepository;
        this.companyRepository = companyRepository;
    }

    // =========================================================
    // 1. TRANSACTIONS
    // =========================================================
    public List<SalesTransaction> getAllTransactions(Long companyId) {
        if (companyId != null) {
            return salesTransactionRepository.findByCompanyId(companyId);
        }
        return salesTransactionRepository.findAll();
    }

    public SalesTransaction createTransaction(SalesTransaction entity, Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            entity.setCompany(company);
        }
        return salesTransactionRepository.save(entity);
    }

    public SalesTransaction updateTransaction(Long id, SalesTransaction entity) {
        SalesTransaction existing = salesTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        existing.setCustomerName(entity.getCustomerName());
        existing.setCustomerPhone(entity.getCustomerPhone());
        existing.setTotalAmount(entity.getTotalAmount());
        existing.setPaymentMethod(entity.getPaymentMethod());
        existing.setPaymentStatus(entity.getPaymentStatus());
        existing.setStatus(entity.getStatus());
        return salesTransactionRepository.save(existing);
    }

    public void deleteTransaction(Long id) {
        salesTransactionRepository.deleteById(id);
    }

    // =========================================================
    // 2. ORDERS
    // =========================================================
    public List<SalesOrder> getAllOrders(Long companyId) {
        if (companyId != null) {
            return salesOrderRepository.findByCompanyId(companyId);
        }
        return salesOrderRepository.findAll();
    }

    public SalesOrder createOrder(SalesOrder entity, Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            entity.setCompany(company);
        }
        return salesOrderRepository.save(entity);
    }

    public SalesOrder updateOrder(Long id, SalesOrder entity) {
        SalesOrder existing = salesOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        existing.setCustomerName(entity.getCustomerName());
        existing.setCustomerPhone(entity.getCustomerPhone());
        existing.setItemCount(entity.getItemCount());
        existing.setTotalAmount(entity.getTotalAmount());
        existing.setOrderStatus(entity.getOrderStatus());
        existing.setStatus(entity.getStatus());
        return salesOrderRepository.save(existing);
    }

    public void deleteOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }

    // =========================================================
    // 3. SUSPENDED ORDERS
    // =========================================================
    public List<SuspendedOrder> getAllSuspended(Long companyId) {
        if (companyId != null) {
            return suspendedOrderRepository.findByCompanyId(companyId);
        }
        return suspendedOrderRepository.findAll();
    }

    public SuspendedOrder createSuspended(SuspendedOrder entity, Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            entity.setCompany(company);
        }
        return suspendedOrderRepository.save(entity);
    }

    public SuspendedOrder updateSuspended(Long id, SuspendedOrder entity) {
        SuspendedOrder existing = suspendedOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Suspended order not found"));
        existing.setCustomerName(entity.getCustomerName());
        existing.setCustomerPhone(entity.getCustomerPhone());
        existing.setTotalAmount(entity.getTotalAmount());
        existing.setHoldReason(entity.getHoldReason());
        existing.setSuspendedStatus(entity.getSuspendedStatus());
        existing.setStatus(entity.getStatus());
        return suspendedOrderRepository.save(existing);
    }

    public void deleteSuspended(Long id) {
        suspendedOrderRepository.deleteById(id);
    }

    // =========================================================
    // 4. COMPLETED SALES
    // =========================================================
    public List<CompletedSale> getAllCompleted(Long companyId) {
        if (companyId != null) {
            return completedSaleRepository.findByCompanyId(companyId);
        }
        return completedSaleRepository.findAll();
    }

    public CompletedSale createCompleted(CompletedSale entity, Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            entity.setCompany(company);
        }
        return completedSaleRepository.save(entity);
    }

    public CompletedSale updateCompleted(Long id, CompletedSale entity) {
        CompletedSale existing = completedSaleRepository.findById(id).orElseThrow(() -> new RuntimeException("Completed sale not found"));
        existing.setCustomerName(entity.getCustomerName());
        existing.setPaymentMethod(entity.getPaymentMethod());
        existing.setTotalAmount(entity.getTotalAmount());
        existing.setNetAmount(entity.getNetAmount());
        existing.setStatus(entity.getStatus());
        return completedSaleRepository.save(existing);
    }

    public void deleteCompleted(Long id) {
        completedSaleRepository.deleteById(id);
    }

    // =========================================================
    // 5. EXCHANGES
    // =========================================================
    public List<SalesExchange> getAllExchanges(Long companyId) {
        if (companyId != null) {
            return salesExchangeRepository.findByCompanyId(companyId);
        }
        return salesExchangeRepository.findAll();
    }

    public SalesExchange createExchange(SalesExchange entity, Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            entity.setCompany(company);
        }
        return salesExchangeRepository.save(entity);
    }

    public SalesExchange updateExchange(Long id, SalesExchange entity) {
        SalesExchange existing = salesExchangeRepository.findById(id).orElseThrow(() -> new RuntimeException("Exchange not found"));
        existing.setOriginalBillNo(entity.getOriginalBillNo());
        existing.setCustomerName(entity.getCustomerName());
        existing.setReturnedItem(entity.getReturnedItem());
        existing.setNewItem(entity.getNewItem());
        existing.setPriceDifference(entity.getPriceDifference());
        existing.setExchangeStatus(entity.getExchangeStatus());
        existing.setStatus(entity.getStatus());
        return salesExchangeRepository.save(existing);
    }

    public void deleteExchange(Long id) {
        salesExchangeRepository.deleteById(id);
    }

    // =========================================================
    // 6. RETURNS & REFUNDS
    // =========================================================
    public List<SalesReturn> getAllReturns(Long companyId) {
        if (companyId != null) {
            return salesReturnRepository.findByCompanyId(companyId);
        }
        return salesReturnRepository.findAll();
    }

    public SalesReturn createReturn(SalesReturn entity, Long companyId) {
        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            entity.setCompany(company);
        }
        return salesReturnRepository.save(entity);
    }

    public SalesReturn updateReturn(Long id, SalesReturn entity) {
        SalesReturn existing = salesReturnRepository.findById(id).orElseThrow(() -> new RuntimeException("Return not found"));
        existing.setOriginalBillNo(entity.getOriginalBillNo());
        existing.setCustomerName(entity.getCustomerName());
        existing.setReturnReason(entity.getReturnReason());
        existing.setRefundAmount(entity.getRefundAmount());
        existing.setRefundMethod(entity.getRefundMethod());
        existing.setRefundStatus(entity.getRefundStatus());
        existing.setStatus(entity.getStatus());
        return salesReturnRepository.save(existing);
    }

    public void deleteReturn(Long id) {
        salesReturnRepository.deleteById(id);
    }
}
