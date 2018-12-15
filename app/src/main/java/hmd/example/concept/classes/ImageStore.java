package hmd.example.concept.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageStore {
    private List<String> imageUrls = new ArrayList<>();

    public ImageStore() {
        imageUrls.add("https://4.img-dpreview.com/files/p/TS600x450~sample_galleries/0753964569/9690409693.jpg");
        imageUrls.add("https://2.img-dpreview.com/files/p/TS600x450~sample_galleries/0753964569/8940777164.jpg");
        imageUrls.add("https://2.img-dpreview.com/files/p/TS600x450~sample_galleries/0753964569/3365908689.jpg");
        imageUrls.add("https://1.img-dpreview.com/files/p/TS600x450~sample_galleries/0753964569/6271090023.jpg");
        imageUrls.add("https://2.img-dpreview.com/files/p/TS600x450~sample_galleries/0753964569/6996087835.jpg");
    }

    //this method selects a random url from list
    public String getRandomPic()
    {
        int randomIndex = new Random().nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }
}
