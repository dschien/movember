package ac.uk.bris.cs.spe.tutorial.movember;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovemberApplicationTests {


    @Autowired
    private BeardRepository beardRepository;

    @Test
    public void simpleBeard() {
        Beard beard = new Beard();
        beard.setName("moustache");

        beardRepository.save(beard);

        Iterable<Beard> all = beardRepository.findAll();

        for (Beard b : all) {
            System.out.println(b.name);
        }

    }

}
