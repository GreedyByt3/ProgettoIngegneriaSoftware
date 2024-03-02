public class Date implements Comparable<Date>{
    private int day, month, year;
    final int millisecond = 86400000;

    final int thirtyone = 31 * millisecond;
    final int thirty = 30 * millisecond;
    final int twentyEighth = 28 * millisecond;

    int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public Date(String date){
        String arr[] = date.split("/");
        this.day = Integer.parseInt(arr[0]);
        this.month = Integer.parseInt(arr[1]);
        this.year = Integer.parseInt(arr[2]);
    }

    public int getDay() {
        return day;
    }
    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }

    public String toString() {
        return String.format("%d/%d/%d",day,month,year);
    }
    public int hashCode(){
        return day^month^year;
    }

    @Override
    public int compareTo(Date o) {
        int dif = this.year - o.year;
        if(dif == 0){
            dif = this.month - o.month;
            if(dif == 0){
                return this.day - o.day;
            }
            return dif;
        }
        return dif;
    }

    public boolean differenceBetween(Date o1) {
        int day1 = this.day;
        int day2 = o1.day;
        int month1 = this.month;
        int month2 = o1.month;
        int sum1 = 0;
        int sum2 = 0;
        if(this.year == o1.year){
            for (int i = 1; i < month1; i++){
                sum1 += days[i - 1];
            }
            for(int j = 1; j < month2; j++){
                sum2 += days[j - 1];
            }
            long date1ms = (long) sum1 * millisecond + (long) day1 * millisecond;
            long date2ms = (long) sum2 * millisecond + (long) day2 * millisecond;

            return !((Math.abs(date1ms - date2ms)) < thirtyone);
        }
        return true;
    }
}
