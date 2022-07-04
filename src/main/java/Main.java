import frame.BukuViewFrame;
import  helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        BukuViewFrame viewFrame = new BukuViewFrame();
        viewFrame.setVisible(true);
    }
}
