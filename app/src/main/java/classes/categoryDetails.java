package classes;

/**
 * Created by MunnaSharma on 7/14/2017.
 */

public class categoryDetails {
    private String CategoryName;
    private String imageurl;
    private String multiFac;

    public String getMultiFac() {
        return multiFac;
    }

    public void setMultiFac(String multiFac) {
        this.multiFac = multiFac;
    }

   public categoryDetails(){

    }


    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
