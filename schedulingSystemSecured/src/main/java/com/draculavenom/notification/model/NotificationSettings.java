package com.draculavenom.notification.model;

import com.draculavenom.security.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "notification_settings", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
    }
)

public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email_enabled", nullable=false)
    private boolean emailEnabled = true;
    
    @Column(name = "appointment_created", nullable = false)
    private boolean appointmentCreated = true;

    @Column(name = "payment_runs_out", nullable = false)
    private boolean paymentRunsOut = true;

    @Column(name = "appointment_status_changes", nullable = false)
    private boolean appointmentStatusChanges = true;

    public NotificationSettings(User user) {
        this.user = user;
        this.emailEnabled = true;
        this.appointmentCreated = true;
        this.paymentRunsOut = true;
        this.appointmentStatusChanges = true;
    }
    
}
