/**
 * 
 */
package com.cloud.csye6225.assignment.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.cloud.csye6225.assignment.entity.Bill;
import com.cloud.csye6225.assignment.entity.Bill.Status;
import com.cloud.csye6225.assignment.entity.FileUpload;

/**
 * @author jainh
 *
 */
public class SqlBill {

	public Connection getConnection() {
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/users_database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String name = "root";
		String pwd = "Hemant@123";
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, name, pwd);
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Need query
	public FileUpload getFiles(String billId, Statement stmt) {
		List<FileUpload> files = new ArrayList<FileUpload>();
		try {

			String query = "select * from File where billId='" + billId + "'";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return files.size() == 0 ? null : files.get(0);

	}

	public List<Bill> getBill() {
		Connection conn = null;
		Statement stmt = null;
		List<Bill> result = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sqlrecipe = "select id,created_ts,updated_ts,owner_id,"
					+ " vendor,bill_date,due_date,amount_due,categories,paymentStatus from Bill;";

			ResultSet rs = stmt.executeQuery(sqlrecipe);
			result = new ArrayList<Bill>();

			while (rs.next()) {
				String id = rs.getString("id");
				String files1 = rs.getString("attachment");
				String created_ts = rs.getString("created_ts");
				String updated_ts = rs.getString("updated_ts");
				String owner_id = rs.getString("owner_id");
				String vendor = rs.getString("vendor");
				String bill_date = rs.getString("bill_date");
				String due_date = rs.getString("due_date");
				double amount_due = rs.getDouble("amount_due");
				String paymentStatus = rs.getString("paymentStatus");
				String categories = rs.getString("categories");
				String[] categoryElement = categories.split(",");
				// Files attachment = rs.getString("attachment");

				Bill item = new Bill(id, created_ts, updated_ts, owner_id, vendor, bill_date, due_date, amount_due,
						categoryElement, Status.valueOf(paymentStatus));

				item.setAttachment(files1);
				result.add(item);

			}

			return result;

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {

			}
		}
		return result;

	}

	public Bill getBillById(String id) {
		Connection conn = null;
		Statement stmt = null;
		List<Bill> result = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sqlrecipe = "select id,created_ts,updated_ts,owner_id,vendor,bill_date,due_date,amount_due,categories,paymentStatus,attachment from Bill where id='"
					+ id + "'";
			ResultSet rs = stmt.executeQuery(sqlrecipe);
			result = new ArrayList<Bill>();

			while (rs.next()) {
				// String id = rs.getString("id");
				String created_ts = rs.getString("created_ts");
				String updated_ts = rs.getString("updated_ts");
				String owner_id = rs.getString("owner_id");
				String vendor = rs.getString("vendor");
				String bill_date = rs.getString("bill_date");
				String due_date = rs.getString("due_date");
				double amount_due = rs.getDouble("amount_due");
				String paymentStatus = rs.getString("paymentStatus");
				String categories = rs.getString("categories");
				String[] categoryElement = categories.split(",");
				String attachment = rs.getString("attachment");

				Bill item = new Bill(id, created_ts, updated_ts, owner_id, vendor, bill_date, due_date, amount_due,
						categoryElement, Status.valueOf(paymentStatus), attachment);

				result.add(item);

			}
			return result.get(0);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {

			}
		}
		return result.get(0);

	}

	public void updateBill(Bill bill) {
		String id = bill.getId();
		// String created_ts = bill.getCreated_ts();
		// String updated_ts = bill.getUpdated_ts();
		String owner_id = bill.getOwner_id();
		String vendor = bill.getVendor();

		String bill_date = bill.getBill_date();
		String due_date = bill.getDue_date();
		double amount_due = bill.getAmount_due();

		String attachment = bill.getAttachment();

		Calendar calendar = Calendar.getInstance();
		java.util.Date time = calendar.getTime();
		// String user_updated = time.toString();

		Connection conn = null;
		Statement stmt = null;
		List<Bill> result = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();

			String query = String.format(
					"update Bill set " + "bill_date = \"%s\", " + "due_date = \"%s\", " + "amount_due = \"%f\", "
							+ "vendor =\"%s\", " + "owner_id=\"%s\" where id=\"%s\"; ",
					bill_date, due_date, amount_due, vendor, owner_id, id);
			stmt.execute(query);

			// Update query for the attachment

			String query1 = "update Bill set attachment = " + "'" + attachment + "'" + "where id =" + "'" + id + "'" + ";";
			stmt.execute(query1);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {

			}
		}

	}

	public void addBill(Bill bill) {
		String id = bill.getId();
		String created_ts = bill.getCreated_ts();
		String updated_ts = bill.getUpdated_ts();
		String owner_id = bill.getOwner_id();
		String vendor = bill.getVendor();
		String bill_date = bill.getBill_date();
		String due_date = bill.getDue_date();
		String amount_due = String.valueOf(bill.getAmount_due());
		String paymentStatus = String.valueOf(bill.getPaymentStatus());
		// double amount_due = bill.getAmount();
		String[] categoriesElement = bill.getCategories();
		String categories = String.join(",", categoriesElement);
		// String categories = categoriesElement.

		// Calendar calendar = Calendar.getInstance();
		// Date time = calendar.getTime();
		// String user_created = time.toString();
		// Date userCreate = calendar.getTime();
		// userCreate sql;
		Connection conn = null;
		Statement stmt = null;
		List<Bill> result = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();

//String sql = "insert into users (id, email, password, firstName, lastName, account_created, account_updated) values ('"+id+"','"+email+"','"+password+"','"+firstName+"','"+lastName+"','"+user_created+"','"+user_created+"')";	            

			String sqlBill = "insert into Bill(id,created_ts,updated_ts,owner_id,vendor,bill_date,due_date,amount_due,categories,paymentStatus) values ('"
					+ id + "','" + created_ts + "','" + updated_ts + "','" + owner_id + "','" + vendor + "','"
					+ bill_date + "','" + due_date + "','" + amount_due + "','" + categories + "','" + paymentStatus
					+ "')";

//	            String sqlRecipe = String.format("insert into bill values (" +
//	                            "\"%s\", " +
//	                            "\"%s\", " +
//	                            "\"%s\", " +
//	                            "\"%s\", "+
//	                            "\"%s\", "+
//	                            "\"%s\", " +
//	                            "\"%s\" " +
//	                            "\"%s\" );",
//	                    id,
//	                    created_ts,
//	                    updated_ts,
//	                    owner_id,
//	                    vendor,
//	                    bill_date,
//	                    due_date,
//	                    amount_due
//	                    
//	            );

//	            for(int i = 0; i < categories.size(); i++){
//	                String sqlIngre = String.format("insert into Categories values(" +
//
//	                        "\"%s\" ,"+
//	                        "\"%s\" );",
//	                        id,
//	                        categories.get(i)
//	                        );
//	                
//	                stmt.execute(sqlIngre);
//	            }

			stmt.execute(sqlBill);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {

			}
		}
	}

	public boolean deleteBillById(String id) {
		Connection conn = null;
		Statement stmt = null;
		boolean isSuccess = false;
		try {

			{
				conn = getConnection();
				stmt = conn.createStatement();
				String query = String.format("delete from Bill " + "where id = \'%s\'; ", id);
				stmt.execute(query);
				isSuccess = true;
				return isSuccess;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return isSuccess;
		} finally {
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {

			}
		}
	}

	public void deleteBill(Bill bill) {
		Connection conn = null;
		Statement stmt = null;
		try {

			{
				conn = getConnection();
				stmt = conn.createStatement();
				String query = String.format("delete from Bill" + "where id = %s, ", bill.getId());
				stmt.execute(query);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException ex) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {

			}
		}

	}

	public void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}