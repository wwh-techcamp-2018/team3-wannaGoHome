package wannagohome.domain.team;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static wannagohome.domain.user.User.DEFAULT_PROFILE;

@Builder
@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String profile;

    @NotBlank
    @Size(min = 1, max = 20)
    @Column(length = 20, nullable = false)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    public String getProfile() {
        if (profile == null || profile.isEmpty())
            return DEFAULT_PROFILE;
        return this.profile;
    }

    public boolean isDefaultProfile() {
        return getProfile().equals(DEFAULT_PROFILE);
    }

    public void delete() {
        deleted = true;
    }
}
