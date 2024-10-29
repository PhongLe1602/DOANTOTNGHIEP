package doan.ptit.programmingtrainingcenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

//    @Column(nullable = false, unique = true, length = 255)
//    String username;

    @Column(nullable = false, length = 100)
    String fullName;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(nullable = false)
    @JsonIgnore
    String password;

    @Column(length = 10)
    String gender;

    @Temporal(TemporalType.DATE)
    Date birthDate ;

    @Column(length = 15)
    String phoneNumber;

    @Column(length = 255)
    String address;

    @Column(length = 255)
    String profilePicture;

    @Column(length = 500)
    String bio; // Thêm phần giới thiệu về user

    Boolean isLocked;

    Boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

}
