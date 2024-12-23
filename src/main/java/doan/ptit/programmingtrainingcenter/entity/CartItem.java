package doan.ptit.programmingtrainingcenter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "cart_id", foreignKey = @ForeignKey(name = "FK_cart_item_cart_id"), nullable = false)
    @JsonBackReference
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "FK_cart_item_course_id"), nullable = false)
//    @JsonBackReference
    Course course;

    @Column(nullable = false)
    BigDecimal price;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;
}
