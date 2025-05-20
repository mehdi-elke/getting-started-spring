    package com.example.demo.controller;

    import com.example.demo.model.PaymentMethod;
    import com.example.demo.model.PaymentStatus;
    import com.example.demo.model.dto.PaymentRequest;
    import com.example.demo.model.dto.PaymentResponse;
    import com.example.demo.service.PaymentService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.setup.MockMvcBuilders;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.UUID;

    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.when;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    public class PaymentControllerTest {

        private MockMvc mockMvc;

        @Mock
        private PaymentService paymentService;

        @InjectMocks
        private PaymentController paymentController;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        }

        @Test
        void testProcessPayment() throws Exception {
            PaymentResponse response = PaymentResponse.builder()
                    .id(UUID.randomUUID())
                    .orderId(UUID.randomUUID())
                    .amount(new BigDecimal("100.00"))
                    .paymentMethod(PaymentMethod.CREDIT_CARD)
                    .status(PaymentStatus.COMPLETED)
                    .message("Paiement réussi")
                    .build();

            when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

            mockMvc.perform(post("/payment/process")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "orderId": "123e4567-e89b-12d3-a456-426614174000",
                                        "amount": 100.00,
                                        "paymentMethod": "CREDIT_CARD"
                                    }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("COMPLETED"))
                    .andExpect(jsonPath("$.message").value("Paiement réussi"));
        }

        @Test
        void testGetPaymentMethods() throws Exception {
            when(paymentService.getAvailablePaymentMethods()).thenReturn(List.of(PaymentMethod.CREDIT_CARD, PaymentMethod.PAYPAL));

            mockMvc.perform(get("/payment/methods"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0]").value("CREDIT_CARD"))
                    .andExpect(jsonPath("$[1]").value("PAYPAL"));
        }
    }