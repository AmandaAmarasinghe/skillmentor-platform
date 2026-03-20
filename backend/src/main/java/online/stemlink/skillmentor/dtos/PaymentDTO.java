package online.stemlink.skillmentor.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentDTO {

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Session ID cannot be null")
    private Long sessionId;

    @Size(max = 255)
    private String receiptUrl;

    @Size(max = 500)
    private String notes;
}