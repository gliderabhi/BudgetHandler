package classes;

/**
 * Created by MunnaSharma on 7/13/2017.
 */

public class Tables {
    private String tablesName,tableMonth;
    private float credit,balance,expenditure;

    public float getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(float expenditure) {
        this.expenditure = expenditure;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

   public Tables(){

    }

    public String getTablesName() {
        return tablesName;
    }

    public void setTablesName(String tablesName) {
        this.tablesName = tablesName;
    }

    public String getTableMonth() {
        return tableMonth;
    }

    public void setTableMonth(String tableMonth) {
        this.tableMonth = tableMonth;
    }
}
