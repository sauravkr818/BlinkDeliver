package com.saurav.BlinkDeliver.service.payment;

import com.saurav.BlinkDeliver.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating appropriate PaymentProcessor based on
 * PaymentMethod.
 * Follows Factory Pattern and Open/Closed Principle.
 * New payment methods can be added by creating new processor implementations
 * and registering them here without modifying existing code.
 */
@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {

    private final RazorpayPaymentProcessor razorpayPaymentProcessor;
    // Future: Add other processors like StripePaymentProcessor,
    // PayPalPaymentProcessor, etc.

    public PaymentProcessor getProcessor(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING, WALLET -> razorpayPaymentProcessor;
            case CASH_ON_DELIVERY -> throw new UnsupportedOperationException("COD does not require payment gateway");
            default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        };
    }
}
