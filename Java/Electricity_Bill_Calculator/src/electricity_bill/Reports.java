package electricity_bill;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Reports 
{

	public static void consumerBillByCity() throws ClassNotFoundException, SQLException
	{
		Connection con = DatabaseConnectivity.getConnection();
		PreparedStatement pst = con.prepareStatement("select * from consumer");
		Scanner in = new Scanner(System.in);
		System.out.print("Enter city:");
		String city = in.nextLine();
		System.out.print("Enter area:");
		String area = in.nextLine();

		ResultSet rs = pst.executeQuery();
		List<Consumer> consumerList = new ArrayList<Consumer>();
		List<Bill> billList = new ArrayList<Bill>();
		while(rs.next())
		{
			consumerList.add(new Consumer(rs.getInt("id"), rs.getString("name"), rs.getString("city"),  rs.getString("area"), rs.getString("type_name")));
		}
		
		PreparedStatement pst2 = con.prepareStatement("select * from bill");
		ResultSet rs1 = pst2.executeQuery();

		while(rs1.next())
		{
			billList.add(new Bill(rs1.getInt("bill_id"), rs1.getInt("year"), rs1.getInt("month"), rs1.getDouble("units_consumed"), rs1.getDouble("total_bill"),
					rs1.getInt("consumer_id")));
		}
		
		
		List<Integer> filteredConsumerList = new ArrayList<Integer>();
		consumerList.stream().filter(obj -> (obj.getCity().equals(city) && obj.getArea().equals(area))).
				forEach(obj -> filteredConsumerList.add(obj.getConsumerId()));
		billList.stream().filter(obj -> filteredConsumerList.contains(obj.getConsumerId())).forEach(obj -> obj.display());
		
		con.close();
	}
	
	public static void consumerBillByYear() throws ClassNotFoundException, SQLException
	{
		Connection con = DatabaseConnectivity.getConnection();
		PreparedStatement pst = con.prepareStatement("select * from bill");
		Scanner in = new Scanner(System.in);
		System.out.print("Enter Consumer ID:");
		int consumerId = in.nextInt();
		System.out.print("Enter Year:");
		int year = in.nextInt();
		System.out.print("Enter Month:");
		int month = in.nextInt();

		ResultSet rs = pst.executeQuery();
		List<Bill> billList = new ArrayList<Bill>();
		while(rs.next())
		{
			billList.add(new Bill(rs.getInt("bill_id"), rs.getInt("year"), rs.getInt("month"), rs.getDouble("units_consumed"), rs.getDouble("total_bill"),
					rs.getInt("consumer_id")));
		}
		
		billList.stream().filter(obj -> (obj.getYear() == year && obj.getMonth() == month && obj.getConsumerId() == consumerId)).forEach(obj -> obj.display());		
		con.close();
	}
}