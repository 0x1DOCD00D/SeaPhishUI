import org.sikuli.script.*;

public class TestSikuli {

    public static void main(String[] args) {
        Screen s = new Screen();
        try{
            s.click("src/testimage.png");
            //s.wait("/src/testimage.png");
            s.click();
            s.write("hello world#ENTER.");
        }
        catch(FindFailed e){
            e.printStackTrace();
        }
    }
}
