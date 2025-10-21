package com.example.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {
    
    @Bean
    public NewTopic OrderCreatedTopic() {
        return TopicBuilder.name("order-created").build();
    }

    @Bean
    public NewTopic OrderApprovedTopic() {
        return TopicBuilder.name("order-approved").build();
    }

    @Bean
    public NewTopic OrderRejectedTopic() {
        return TopicBuilder.name("order-rejected").build();
    }

    @Bean
    public NewTopic StockReservedTopic() {
        return TopicBuilder.name("stock-reserved").build();
    }

    @Bean
    public NewTopic StockReservationFailedTopic() {
        return TopicBuilder.name("stock-reservation-failed").build();
    }

    @Bean
    public NewTopic PaymentAuthorizedTopic() {
        return TopicBuilder.name("payment-authorized").build();
    }

    @Bean
    public NewTopic PaymentFailedTopic() {
        return TopicBuilder.name("payment-failed").build();
    }

    @Bean
    public NewTopic PaymentCancelledTopic() {
        return TopicBuilder.name("payment-cancelled").build();
    }
}
