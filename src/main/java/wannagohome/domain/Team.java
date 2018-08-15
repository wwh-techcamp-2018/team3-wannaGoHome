package wannagohome.domain;

import org.hibernate.annotations.ColumnDefault;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Team {

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

    @NotBlank
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;
}
