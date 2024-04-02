package com.jorgeyh7.app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private  String password;
    @Column(name="is_enabled")
    private boolean isEnabled;
    @Column(name="account_no_expired")
    private  boolean accountNoExpired;
    @Column(name="account_no_locked")
    private  boolean accountNoLocked;
    @Column(name="credential_no_expired")
    private  boolean credentialNoExpired;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)//eager hace que cargue todos los roles que tiene - cascadeType.ALl, si guardo un usuario en la table, me guardara automaticamente los roles asociados al usuario
    @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<RoleEntity> roles=new HashSet<>();



}
