package wannagohome.domain;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    private String profileImage;

    @NotBlank
    @Size(min = 1, max = 20)
    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

}
