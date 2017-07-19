package classes;

/**
 * Created by MunnaSharma on 7/14/2017.
 */

public class categoryDetails {
    private String CategoryName,imageurl;
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
