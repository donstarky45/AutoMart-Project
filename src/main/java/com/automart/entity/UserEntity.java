package com.automart.entity;




import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
// TutorialNew/IDtotesDtId@gmil.com
public class UserEntity extends RepresentationModel<UserEntity> implements UserDetails {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    private String emailVerificationToken;
    @Column(nullable = false)
    private boolean emailVerificationStatus = false;
    @Column(nullable = false)
    private String userId;
    private String password;
    @Column(unique = true, nullable = false)
    private String accountNumber;
private double balance;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy=("userDetails"), cascade=CascadeType.ALL)
    private List<AddressEntity> addresses;

    @OneToMany(mappedBy=("userDetails"), cascade=CascadeType.ALL)
    private List<Transactions> transactions;


    @OneToMany(mappedBy=("userId"), cascade=CascadeType.ALL)
    private List<Orders> orders;



    @OneToMany(mappedBy=("userId"), cascade=CascadeType.ALL)
    private List<Car> cars;

    @OneToMany(mappedBy=("userId"), cascade=CascadeType.ALL)
    private List<CarAD> carADS;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
