package fr.tim.smpbank.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tim.smpbank.utils;

public class HistorySQL {

	public static void createHistory() {
		PreparedStatement ps;
		try {
			ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS history" +
					"(Pseudo VARCHAR(100),UUID VARCHAR(100),PRIMARY KEY (Pseudo))");
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createPlayer(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			if (!exists(uuid)) {
				PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INTO history" +
			"(Pseudo,UUID) VALUES (?,?)");
				ps2.setString(1,player.getName());
				ps2.setString(2,uuid.toString());
				ps2.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTaux() {
		try {
				PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INTO history" +
			"(Pseudo,UUID) VALUES (?,?)");
				ps2.setString(1,"Taux");
				ps2.setString(2,"Taux");
				ps2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean exists(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM history WHERE UUID=?");
			ps.setString(1, uuid.toString());
			
			ResultSet results = ps.executeQuery();
			if (results.next() ) {
				//Joueur existe
				return true;
			}
			
			} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static void addDay() {
		try {
			String statement = "ALTER TABLE history ADD COLUMN " + utils.getPrevDay(0) + " DECIMAL(15,3) NOT NULL";
			PreparedStatement ps = MySQL.getConnection().prepareStatement(statement);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void setPlayerDay(UUID uuid) {
		String date = utils.getPrevDay(1);
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE history SET " + date + "=? WHERE UUID=?");
			ps.setFloat(1,(SQLGetter.getPoints(uuid)));
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setTaux(float Taux) {
		String date = utils.getPrevDay(0);
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE history SET " + date + "=? WHERE UUID=?");
			ps.setFloat(1,Taux);
			ps.setString(2, "Taux");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveDay() {
			List<UUID> players = SQLGetter.getPlayers();
			addDay();
			for (UUID p : players) {
				setPlayerDay(p);
		}
			players.clear();
		
	}
	
	public static float getBanque(int minusDay) {
		float banque = 0;
		String day = utils.getPrevDay(minusDay);
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT " + day + " FROM history");
			ResultSet rs = ps.executeQuery();
			while (rs.next() ) {
				banque = banque + rs.getFloat(day);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return banque-getTaux(minusDay);
	}
	
	public static float getVariation() {
		return getBanque(1) - getBanque(2);
	}
	
	public static float getTaux(int day) {
		String date = utils.getPrevDay(day);
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT " + date + " FROM history WHERE UUID=?");
			ps.setString(1, "Taux");
			ResultSet rs = ps.executeQuery();
			float taux = 0;
			if (rs.next() ) {
				taux = rs.getFloat(date);
				return taux;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
