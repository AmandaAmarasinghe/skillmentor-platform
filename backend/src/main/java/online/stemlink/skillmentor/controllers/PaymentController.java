package online.stemlink.skillmentor.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.stemlink.skillmentor.dtos.PaymentDTO;
import online.stemlink.skillmentor.entities.Payment;
import online.stemlink.skillmentor.services.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController extends AbstractController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Payment>> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentService.getAllPayments(pageable);
        return sendOkResponse(payments);
    }

    @GetMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return sendOkResponse(payment);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        Payment createdPayment = paymentService.createNewPayment(paymentDTO);
        return sendCreatedResponse(createdPayment);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id,
                                                 @Valid @RequestBody PaymentDTO updatedPaymentDTO) {
        Payment updatedPayment = paymentService.updatePaymentById(id, updatedPaymentDTO);
        return sendOkResponse(updatedPayment);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return sendNoContentResponse();
    }
}
