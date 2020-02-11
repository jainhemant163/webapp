/**
 * 
 */
package com.cloud.csye6225.assignment.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cloud.csye6225.assignment.entity.FileUpload;
import com.google.gson.Gson;

/**
 * @author jainh
 *
 */
public class SqlFile {
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

    public void insertFile(FileUpload files) {
        String id = files.getId();
        String url = files.getUrl();
        String fileName = files.getFile_name();
        String uploadDate = files.getUpload_date();

        Gson gson = new Gson();
        FileUpload attachmenttemp = new FileUpload(fileName, id, url, uploadDate);
        String attachment = gson.toJson(attachmenttemp);

        System.out.println(attachment);
        // System.out.println(billId);
        Connection conn = null;
        Statement stmt = null;
        List<FileUpload> result = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            String sql = "insert into File (file_name,id,url,upload_date) values ('" + fileName + "','" + id + "','"
                    + url + "','" + uploadDate + "')";
            stmt.execute(sql);


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




	public boolean deleteFile(String id) {

		Connection conn = null;
		Statement stmt = null;
		boolean isSuccess = false;
		try {
			{
				conn = getConnection();
				stmt = conn.createStatement();
				String query = String.format("delete from File " + "where id = \'%s\'; ", id);
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

	public FileUpload getFileById(String id) {
		Connection conn = null;
		Statement stmt = null;
		List<FileUpload> result = new ArrayList<FileUpload>();
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sql = "select * from File where id = '" + id + "';";
			ResultSet res = stmt.executeQuery(sql);

			while (res.next()) {
				String fileid = res.getString("id");
				String url = res.getString("url");
				String file_name = res.getString("file_name");
				String upload_date = res.getString("upload_date");

				FileUpload file = new FileUpload(file_name,fileid,url,upload_date);
				result.add(file);
			}
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
	
	public void updateFiles(String id, String url) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            String sql = "update File set id =" + "'" + id + "'" + ", url=" + "'" + url + "'" + "where id=" + "'" + id
                    + "'" + ";";
            stmt.execute(sql);

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




}