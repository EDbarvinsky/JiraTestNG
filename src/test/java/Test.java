
import com.epam.testng.utils.JIRAAttachment;
import com.epam.testng.JIRATestKey;
import com.epam.testng.JIRATestNGListener;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Ignore;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.io.File;

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
        JIRAAttachment.addAttachment(new File("D:\\Untitled.png"), "test1");
        JIRAAttachment.addAttachment(new File("D:\\text1.txt"), "test1");
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

    @org.testng.annotations.Test
    @JIRATestKey(key = "test7")
    public void test7() {
        SoftAssert asert = new SoftAssert();
        asert.assertEquals(1, 1, "s");
        asert.assertEquals(2, 1, "f");
        asert.assertEquals(3, 3, "s");
        asert.assertAll();
    }
}