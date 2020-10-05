package com.mcg.jwt.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends BaseEntity{
    @Id
    private int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "home_town")
    private String homeTown;

    @Column(name = "gender")
    private String gender;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "id")
    @JsonIgnore
    @MapsId
    private User user;
}
