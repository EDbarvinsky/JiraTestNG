import com.epam.testng.JIRATestKey;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//@Listeners(JIRATestNGListener.class)
public class ParametrizedXMLTest {
    @Test
    @Parameters({"number1", "number2", "sum"})
    @JIRATestKey(key = "parameterTestPassed")
    public void parameterTest(int number1, int number2, int sum) {
        Assert.assertEquals(number1 + number2, sum);
    }
}
