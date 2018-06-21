package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.DriverIdMap;
import it.polito.tdp.formulaone.model.FantaPilota;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {

		String sql = "SELECT year FROM races ORDER BY year";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Integer> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rs.getInt("year"));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Season> getAllSeasons() {

		String sql = "SELECT year, url FROM seasons ORDER BY year";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Driver> getAllDrivers(DriverIdMap dim) {

		String sql = "Select DISTINCT drivers.driverId, forename, surname from drivers";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			List<Driver> drivers = new ArrayList<>();
			while (rs.next()) {
				drivers.add(
						dim.get(new Driver(rs.getInt("driverId"), rs.getString("forename"), rs.getString("surname"))));
			}

			conn.close();
			return drivers;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Driver> getAllDriversBySeason(Season s, DriverIdMap dim) {

		String sql = "Select DISTINCT drivers.driverId, forename, surname\n" + "from drivers, races, results\n"
				+ "where races.year = ?\n" + "and results.raceId = races.raceId\n"
				+ "and results.driverId is not null\n" + "and results.driverId = drivers.driverId";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s.getYear());
			ResultSet rs = st.executeQuery();

			List<Driver> drivers = new ArrayList<>();
			while (rs.next()) {
				drivers.add(
						dim.get(new Driver(rs.getInt("driverId"), rs.getString("forename"), rs.getString("surname"))));
			}

			conn.close();
			return drivers;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	
	public List<FantaPilota> getFantaPiloti(int circuitId, Driver driver) {
		/*devo prendere tutte le gare relative ad un particolare circuito
		 * dove un particolare driver ha gareggiato, uso le 4 mape necessarie per i coolegamenti
		 * selezionando circuito e driver, ho tutti i tempi di giro del pilota selezionato per tutti gli anni,
		 * mi faccio ridare gli ani diversi, ad ogni anno corrisponde
		 * un fantapilota */
		
		String sql = "select distinct year\n" + 
				"from races, laptimes, circuits, drivers\n" + 
				"where circuits.circuitId = ?\n" + 
				"and drivers.driverId = ?\n" + 
				"and laptimes.driverId = drivers.driverId\n" + 
				"and races.circuitId = circuits.circuitId\n" + 
				"and laptimes.raceId = races.raceId";
		
		String sql2 = "select milliseconds\n" + 
				"from races, laptimes, circuits, drivers\n" + 
				"where circuits.circuitId = ?\n" + 
				"and drivers.driverId = ?\n" + 
				"and laptimes.driverId = drivers.driverId\n" + 
				"and races.circuitId = circuits.circuitId\n" + 
				"and laptimes.raceId = races.raceId\n" + 
				"and year = ?\n" + 
				"order by lap";
		
		List<FantaPilota> result = new ArrayList<FantaPilota>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, circuitId);
			st.setInt(2, driver.getDriverId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int year = rs.getInt("year");
				FantaPilota fp = new FantaPilota(year, new ArrayList<Integer>());
				result.add(fp);
			}
			conn.close();
			
			//per ogni fanatapilota mi serve la lista dei tempi
			//riapro nuova connessione e per ogni fantapilota in un anno mi faccio dare la lista dei laptime
			//ovvero i millisecondi ordinati per giro in modo crescente
			for (FantaPilota fp : result) {
				conn = ConnectDB.getConnection();
				st = conn.prepareStatement(sql2);
				st.setInt(1, circuitId);
				st.setInt(2, driver.getDriverId());
				st.setInt(3, fp.getYear());
				rs = st.executeQuery();

				//lista di millisecodni del giro
				List<Integer> lapTimes = new ArrayList<Integer>();
				
				while (rs.next()) {
					lapTimes.add(rs.getInt("milliseconds"));
				}
				conn.close();
				fp.setLapTimes(lapTimes); //vado a salvare la lista di laptime nel fantapilota
			}
			
			//solo alla fine ritorno la lista di fantapiloti
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
}
