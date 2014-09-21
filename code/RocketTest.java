import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** The unit test class RocketTest. */
public class RocketTest
{
    /** Default constructor for test class RocketTest */
    public RocketTest()
    {
    }

    /** Sets up the test fixture
     * Called before every test case method. */
    @Before
    public void setUp()
    {
    }

    /** Tears down the test fixture.
     * Called after every test case method. */
    @After
    public void tearDown()
    {
    }

    @Test
    public void rocketPosition()
    {
        Map map1 = new Map();
        Rocket rocket1 = new Rocket("files/rocket1.png", 200, 200, map1);
        assertEquals(200, rocket1.getX());
        rocket1.move(2, 2);
        rocket1.rotate(40);
        assertEquals(202, rocket1.getY());
    }
}