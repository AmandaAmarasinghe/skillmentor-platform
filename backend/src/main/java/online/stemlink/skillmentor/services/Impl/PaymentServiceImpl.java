package online.stemlink.skillmentor.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.stemlink.skillmentor.dtos.PaymentDTO;
import online.stemlink.skillmentor.entities.Payment;
import online.stemlink.skillmentor.entities.Session;
import online.stemlink.skillmentor.entities.Student;
import online.stemlink.skillmentor.exceptions.SkillMentorException;
import online.stemlink.skillmentor.repositories.PaymentRepository;
import online.stemlink.skillmentor.repositories.SessionRepository;
import online.stemlink.skillmentor.repositories.StudentRepository;
import online.stemlink.skillmentor.services.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final SessionRepository sessionRepository;

    @Override
    public Payment createNewPayment(PaymentDTO paymentDTO) {
        try {
            Student student = studentRepository.findById(paymentDTO.getStudentId())
                    .orElseThrow(() -> new SkillMentorException("Student not found", HttpStatus.NOT_FOUND));

            Session session = sessionRepository.findById(paymentDTO.getSessionId())
                    .orElseThrow(() -> new SkillMentorException("Session not found", HttpStatus.NOT_FOUND));

            Payment payment = new Payment();
            payment.setStudent(student);
            payment.setSession(session);
            payment.setReceiptUrl(paymentDTO.getReceiptUrl());
            payment.setNotes(paymentDTO.getNotes());

            return paymentRepository.save(payment);

        } catch (SkillMentorException e) {
            log.warn("Failed to create payment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating payment", e);
            throw new SkillMentorException("Failed to create payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Payment> getAllPayments(Pageable pageable) {
        try {
            return paymentRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Failed to fetch payments", e);
            throw new SkillMentorException("Failed to fetch payments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Payment getPaymentById(Long id) {
        try {
            return paymentRepository.findById(id)
                    .orElseThrow(() -> new SkillMentorException("Payment not found", HttpStatus.NOT_FOUND));
        } catch (SkillMentorException e) {
            log.warn("Payment not found with id {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error fetching payment with id {}", id, e);
            throw new SkillMentorException("Failed to fetch payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Payment updatePaymentById(Long id, PaymentDTO updatedPaymentDTO) {
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new SkillMentorException("Payment not found", HttpStatus.NOT_FOUND));

            if (updatedPaymentDTO.getStudentId() != null) {
                Student student = studentRepository.findById(updatedPaymentDTO.getStudentId())
                        .orElseThrow(() -> new SkillMentorException("Student not found", HttpStatus.NOT_FOUND));
                payment.setStudent(student);
            }

            if (updatedPaymentDTO.getSessionId() != null) {
                Session session = sessionRepository.findById(updatedPaymentDTO.getSessionId())
                        .orElseThrow(() -> new SkillMentorException("Session not found", HttpStatus.NOT_FOUND));
                payment.setSession(session);
            }

            payment.setReceiptUrl(updatedPaymentDTO.getReceiptUrl());
            payment.setNotes(updatedPaymentDTO.getNotes());

            return paymentRepository.save(payment);

        } catch (SkillMentorException e) {
            log.warn("Failed to update payment id {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating payment id {}", id, e);
            throw new SkillMentorException("Failed to update payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deletePayment(Long id) {
        try {
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete payment id {}", id, e);
            throw new SkillMentorException("Failed to delete payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
