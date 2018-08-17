package wannagohome.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    @Getter
    private String title;

    @ManyToOne
    private Team team;

//    private List<Task> tasks;
//    private List<Activity> activities;

    @Enumerated(EnumType.ORDINAL)
    @Getter
    private Color color;

    @NotBlank
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;
}
