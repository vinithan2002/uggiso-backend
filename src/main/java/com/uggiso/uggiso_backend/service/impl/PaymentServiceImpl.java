package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.PaymentRequest;
import com.uggiso.uggiso_backend.dto.response.PaymentResponse;
import com.uggiso.uggiso_backend.entity.Orders;
import com.uggiso.uggiso_backend.entity.Payment;
import com.uggiso.uggiso_backend.entity.PaymentStatus;
import com.uggiso.uggiso_backend.repository.OrderRepository;
import com.uggiso.uggiso_backend.repository.PaymentRepository;
import com.uggiso.uggiso_backend.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public PaymentResponse makePayment(PaymentRequest request) {

        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new RuntimeException("Payment already exists for this order");
        }

        Payment payment = new Payment();

        payment.setOrder(order);

        payment.setAmount(order.getFinalAmount());

        payment.setPaymentMethod(request.getPaymentMethod());

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase()
        );

        payment.setPaymentTime(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return convertToResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        return convertToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrder(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found for this order"));

        return convertToResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updatePaymentStatus(
            Long paymentId,
            PaymentStatus status) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        payment.setPaymentStatus(status);

        if (status == PaymentStatus.SUCCESS) {
            payment.setPaymentTime(LocalDateTime.now());
        }

        Payment updatedPayment = paymentRepository.save(payment);

        return convertToResponse(updatedPayment);
    }

    private PaymentResponse convertToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setPaymentId(payment.getId());

        response.setOrderId(payment.getOrder().getId());

        response.setTransactionId(payment.getTransactionId());

        response.setAmount(payment.getAmount());

        response.setPaymentMethod(payment.getPaymentMethod());

        response.setPaymentStatus(payment.getPaymentStatus());

        response.setPaymentTime(payment.getPaymentTime());

        return response;
    }
}