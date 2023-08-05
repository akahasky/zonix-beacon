package net.zonixmc.beacon.dao;

import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.SneakyThrows;
import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.model.CustomBeacon;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class BeaconDAO {

    private final boolean useMySQL;
    private final Connection connection;
    private final ExecutorService executorService;

    @SneakyThrows
    public BeaconDAO() {

        YamlConfiguration yamlConfiguration = BeaconApi.INSTANCE.getConfiguration();

        this.useMySQL = yamlConfiguration.getBoolean("MySQL.enable");
        this.executorService = Executors.newSingleThreadExecutor();

        if (useMySQL) {

            String host = yamlConfiguration.getString("Storage.host");
            String database = yamlConfiguration.getString("Storage.database");
            String user = yamlConfiguration.getString("Storage.user");
            String password = yamlConfiguration.getString("Storage.password");

            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", host, database), user, password);

        }

        else {

            File file = BeaconApi.INSTANCE.getPlugin().getDataFolder().toPath().resolve("database.sql").toFile();

            if (!file.exists()) file.createNewFile();

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());

        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists zonixBeacons (locationKey long primary key not null, beacon text not null)")) {

            preparedStatement.executeUpdate();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    private void saveBeaconSync(long locationKey, CustomBeacon  customBeacon) {

        String finalQuery = useMySQL
                ? "insert into zonixBeacons (locationKey, beacon) values(?,?) on duplicate key update beacon=values(beacon)"
                : "insert or replace into zonixBeacons (locationKey, beacon) values(?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(finalQuery)) {

            preparedStatement.setLong(1, locationKey);
            preparedStatement.setString(2, CustomBeacon.toObject(customBeacon).toJSONString());

            preparedStatement.executeUpdate();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    public void saveBeacon(long locationKey, CustomBeacon customBeacon, boolean async) {

        if (async)
            CompletableFuture.runAsync(() -> saveBeaconSync(locationKey, customBeacon), executorService);

        else saveBeaconSync(locationKey, customBeacon);

    }

    public void deleteBeacon(long locationKey) {

        CompletableFuture.runAsync(() -> {

            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from zonixBeacons where locationKey=?")) {

                preparedStatement.setLong(1, locationKey);

                preparedStatement.executeUpdate();

            }

            catch (Exception exception) { exception.printStackTrace(); }

        }, executorService);

    }

    public HashBiMap<Long, CustomBeacon> getAllBeacons() {

        HashBiMap<Long, CustomBeacon> customBeacons = HashBiMap.create();

        try (PreparedStatement preparedStatement = connection.prepareStatement("select *  from zonixBeacons")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                customBeacons.put(resultSet.getLong("locationKey"), CustomBeacon.fromObject(resultSet.getString("beacon")));

        }

        catch (Exception exception) { exception.printStackTrace(); }

        return customBeacons;

    }

}
