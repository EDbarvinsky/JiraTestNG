import com.epam.testng.JIRATestKey;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

//@Listeners(JIRATestNGListener.class)
public class DataProviderTest {

    @org.testng.annotations.Test(dataProvider = "sumTestSuccess")
    @JIRATestKey(key = "ShouldnotBeShown", disabled = true)
    public void sumTestShouldNotBeShown(long a, long b, long sum){
        long result = a + b;
        Assert.assertEquals(result, sum);
    }

    @org.testng.annotations.Test(dataProvider = "sumTestSuccess")
    @JIRATestKey(key = "sumTestShouldBePassed2+")
    public void sumTestShouldbePassed2(long a, long b, long sum){
        long result = a + b;
        Assert.assertEquals(result, sum);
    }

    @org.testng.annotations.Test(dataProvider = "sumTestFailed")
    @JIRATestKey(key = "sumTestShouldBeFailed")
    public void sumTestShouldBeFailed(long a, long b, long sum){
        long result = a + b;

        Assert.assertEquals(result, sum);
    }

    @org.testng.annotations.Test(dataProvider = "sumTestSuccess")
    public void ShouldnotBeShown(long a, long b, long sum){
        long result = a + b;
        Assert.assertEquals(result, sum);
    }

    @org.testng.annotations.Test(dataProvider = "sumTestFailed", enabled = false)
    @JIRATestKey(key = "Status should be 'Out of scope'")
    public void shouldntBeShown(long a, long b, long sum){
        long result = a + b;
        Assert.assertEquals(result, sum);
    }

    @DataProvider(name = "sumTestFailed")
    public Object[][] valuesForSumF(){
        return new Object[][] {
                {1, 1, 2},
                {10, -1, 11},
                {0, 0, 0},
        };
    }
}
