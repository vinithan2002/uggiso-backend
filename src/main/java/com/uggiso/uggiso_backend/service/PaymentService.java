package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.PaymentRequest;
import com.uggiso.uggiso_backend.dto.response.PaymentResponse;
import com.uggiso.uggiso_backend.entity.PaymentStatus;

import java.util.List;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByOrder(Long orderId);

    List<PaymentResponse> getAllPayments();

    PaymentResponse updatePaymentStatus(
            Long paymentId,
            PaymentStatus status
    );
}