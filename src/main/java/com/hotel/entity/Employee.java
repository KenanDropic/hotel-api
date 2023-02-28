package com.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee {


    public Employee(String firstName, String lastName,
                    String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Id
    @SequenceGenerator(name = "employee_id", sequenceName = "employee_id", allocationSize = 1, initialValue = 4)
    @GeneratedValue(generator = "employee_id", strategy = SEQUENCE)
    @Column(name = "employee_id")
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @Column(unique = true)
    @NotNull
    private String email;

    @JsonIgnore
    @NotNull
    private String password;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = {MERGE})
    @JoinTable(name = "employee_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void addRole(Role role) {
        this.getRoles().add(role);
    }

    public void setRole(Role role){
        this.roles = (Set<Role>) role;
    }
}
