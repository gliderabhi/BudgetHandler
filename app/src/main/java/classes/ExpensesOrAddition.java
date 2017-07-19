package classes;

/**
 * Created by MunnaSharma on 7/13/2017.
 */

public class ExpensesOrAddition {
    private String timeStamp;
    private Float amount;
    private String CategoryName,CategoryImageUrl;

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return CategoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        CategoryImageUrl = categoryImageUrl;
    }

    public ExpensesOrAddition(){

    }


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getAmount() {
        return amount;
    }
     /*private categoryDetails categoryDetails;

    public classes.categoryDetails getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(classes.categoryDetails categoryDetails) {
        this.categoryDetails = categoryDetails;
    }
*/
}
