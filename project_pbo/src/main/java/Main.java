import frame.BukuViewFrame;
import frame.DetailViewFrame;
import  helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        BukuViewFrame viewFrame = new BukuViewFrame();
//        DetailViewFrame viewFrame = new DetailViewFrame();
        viewFrame.setVisible(true);
    }
}
