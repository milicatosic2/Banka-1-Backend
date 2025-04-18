package com.banka1.banking.models;

import com.banka1.banking.models.helper.DeliveryStatus;
import com.banka1.banking.models.helper.IdempotenceKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String url;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventDelivery> deliveries = new ArrayList<>();

    @Embedded
    private IdempotenceKey idempotenceKey;

    @Column(unique = true)
    private String uniqueKey;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @PostLoad @PrePersist
    private void setUniqueKey() {
        if (idempotenceKey != null) {
            this.uniqueKey = idempotenceKey.getRoutingNumber() + "-" + idempotenceKey.getLocallyGeneratedKey();
        }
    }
}