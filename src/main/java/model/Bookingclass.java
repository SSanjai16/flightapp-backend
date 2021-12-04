package model;

import java.util.Iterator;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Bookingclass {
	int bill=0;
	int bookingcosteconomy=1000;
	int bookingcostbusiness=2000; 
	int mealcost=200;
	public int bookingamount(BookingModel bookingmodel)
	{	
		bill=0;
		if(bookingmodel.getTravelclass().equals("business"))
		{
			bill+=bookingcostbusiness*bookingmodel.getTickets();
		}
		else
		{ 
			bill+=bookingcosteconomy*bookingmodel.getTickets();
		}
		if(bookingmodel.getMeals().equals("yes"))
		{
			bill+=mealcost*bookingmodel.getTickets();
		}
		System.out.println("bill-setamount-"+bill);
		return bill;
	}
	public List<BookingModel> roundbookingamount(List<BookingModel> b)
	{
		//b[0].setAmount(bookingamount(b[0]));
		 //Iterator<Object> it = b.iterator();
//		    while(it.hasNext()){
//		    		it.next(se)
//		    }
		 for(BookingModel book : b)
		 {
			 book.setAmount(bookingamount(book));
		 }
		 return b;
		
	}
	public boolean checkweekend(String d) {
		LocalDate date = LocalDate.parse(d);
		DayOfWeek day = date.getDayOfWeek();
		System.out.println(day.getValue());
		if(day.getValue()==6 || day.getValue()==7)
		{	
			System.out.println("its weekend");
			return true;
		}
		else
		return false;
	}

}
