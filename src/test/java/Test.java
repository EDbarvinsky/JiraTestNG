import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Ignore;
import org.testng.annotations.Listeners;

@Listeners(JIRATestNGListener.class)
public class Test {

    @org.testng.annotations.Test
    @JIRATestKey(key = "test1")
    public void test1() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }

    @org.testng.annotations.Test
    @JIRATestKey(key = "test2")
    public void test2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(false);
    }

    @org.testng.annotations.Test
    @JIRATestKey(key = "test3")
    public void test3() {
            throw new SkipException("Skipping this exception");
    }

    @org.testng.annotations.Test
    @JIRATestKey(key = "test4")
    @Ignore
    public void test4() {
        Assert.assertTrue(true);
    }

    @org.testng.annotations.Test
    public void test5() {
        Assert.assertTrue(true);
    }

    @org.testng.annotations.Test(enabled = false)
    @JIRATestKey(key = "test6")
    public void test6() {
        Assert.assertTrue(true);
    }
}
