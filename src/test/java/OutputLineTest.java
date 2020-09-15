import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OutputLineTest {
    @Test
    public void testTwoProducts(){
        OutputLine outputLine = new OutputLine(
            Optional.of(new Package(Sets.newHashSet(
                new Product(2, 10.0, 20.0),
                new Product(1, 10.0, 20.0))))
        );
        Assertions.assertEquals("1,2", outputLine.toString());
    }

    @Test
    public void testNoProduct(){
        OutputLine outputLine = new OutputLine(
            Optional.of(new Package(Sets.newHashSet()))
        );
        Assertions.assertEquals("-", outputLine.toString());
    }

    @Test
    public void testNoPackage(){
        OutputLine outputLine = new OutputLine(
            Optional.empty()
        );
        Assertions.assertEquals("-", outputLine.toString());
    }
}
