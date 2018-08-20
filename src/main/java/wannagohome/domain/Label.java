package wannagohome.domain;

import javax.persistence.*;

@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Color color;
    private String title;
}
