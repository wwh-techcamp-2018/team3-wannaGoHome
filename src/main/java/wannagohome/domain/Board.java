package wannagohome.domain;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Builder
@EqualsAndHashCode
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String title;

    @ManyToOne
    private Team team;

//    private List<Task> tasks;
//    private List<Activity> activities;

//    private Color color;

    @NotBlank
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;
}
