package ac.uk.bris.cs.spe.tutorial.movember;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table
@Data
public class Beard {

    @Id
    @GeneratedValue
    Long id;

    @Column
    @NotEmpty
    String name;
}
