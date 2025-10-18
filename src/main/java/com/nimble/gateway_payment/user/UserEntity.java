package com.nimble.gateway_payment.user;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
    private String password;

    public UserEntity(RegisterInputDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.cpf = dto.getCpf();
        this.password = dto.getPassword();
    }
}

