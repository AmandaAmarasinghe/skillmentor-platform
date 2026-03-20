package online.stemlink.skillmentor.services;

import online.stemlink.skillmentor.dtos.PaymentDTO;
import online.stemlink.skillmentor.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Payment createNewPayment(PaymentDTO paymentDTO);
    Page<Payment> getAllPayments(Pageable pageable);
    Payment getPaymentById(Long id);
    Payment updatePaymentById(Long id, PaymentDTO updatedPaymentDTO);
    void deletePayment(Long id);
}
