package com.mcg.jwt.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "profile")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Profile extends BaseEntity{

    @Id
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNo;

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "id")
    @MapsId
    private User user;
}
