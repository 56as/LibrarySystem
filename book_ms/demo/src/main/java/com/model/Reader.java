package com.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private String phone;

    private String address;
    
    @Column(name = "membership_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
    
    public enum MembershipStatus {
        ACTIVE,
        EXPIRED,
        SUSPENDED
    }
}