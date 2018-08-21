package wannagohome.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Color color;

    @Size(max = 20)
    private String title;
}
