package br.ufc.data.mining.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import br.ufc.data.mining.model.DayDrive;

public class ResultDAO {

	private static Connection con = br.ufc.data.mining.dao.Connection.getConnection();
	
	public static void delete() {
		String delete = "DELETE FROM result";
		java.sql.PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(delete);
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void insert(Set<DayDrive> points, String table) {
		java.sql.PreparedStatement stmt;
		String query = "INSERT INTO " + table + " (taxiid, coordenada, weekday, idstudent, idcluster, iscore) "
				+ "values (?,ST_SetSrid(ST_MakePoint(?,?),4326),?,?,?,?)";
		
		try {
			
			
			for (DayDrive d : points) {
				stmt = con.prepareStatement(query);
				stmt.setLong(1, d.getId());
				stmt.setDouble(2, d.getLongitude());
				stmt.setDouble(3, d.getLatitude());
				stmt.setInt(4, d.getDate().getDay());
				stmt.setInt(5, d.getIdStudent());
				stmt.setLong(6, d.getCluster());
				stmt.setBoolean(7, d.isCore());
				stmt.execute();
			}

		} catch (SQLException e) {
			System.err.println(("ERRO AQUI >> INSERT RESULTDAO"));
			e.printStackTrace();
		}
	}

}
