package com.example.appspringdata.Repositories;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.appspringdata.Utils.DeviceOutput;
import com.example.appspringdata.Utils.ShelfOutput;
import com.example.appspringdata.Utils.ShelfPositionOutput;

@Repository
public class DeviceRepository {

    @Autowired
    private Driver driver;

    private static final String DATABASE = "neo4j";

    public Optional<List<DeviceOutput>> fetchAllDevices(Long pageNum) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (d:Device {isDeleted:FALSE})
                    OPTIONAL MATCH (d)-[r1 {isDeleted:FALSE}]->(s:ShelfPosition {isDeleted:FALSE})
                    OPTIONAL MATCH (s)-[r2 {isDeleted:FALSE}]->(sh:Shelf {isDeleted:FALSE})
                    RETURN d.deviceId AS deviceId,
                           d.deviceName AS deviceName,
                           d.partNumber AS partNumber,
                           d.buildingName AS buildingName,
                           d.deviceType AS deviceType,
                           d.isDeleted AS isDeleted,
                           d.createdAt AS createdAt,
                           d.updatedAt AS updatedAt,
                           d.numShelfPositions AS numberOfShelfPositions,
                    collect({shelfPosId:s.shelfPosId,shelfPosCreatedAt:s.createdAt,
                    shelfPosDeviceId:s.deviceId,shelfPosUpdatedAt:s.updatedAt,
                    shelfId:sh.shelfId,shelfPartNumber:sh.partNumber,
                    shelfName:sh.shelfName,shelfUpdatedAt:sh.updatedAt,
                    shelfCreatedAt:sh.createdAt}) AS shelfPositions
                    SKIP $pageNum LIMIT 50;
                    """;

            List<DeviceOutput> list = session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("pageNum", pageNum)).list();
                List<DeviceOutput> deviceList = new ArrayList<>();
                for (Record record : records) {
                    deviceList.add(mapToDevice(record));
                }
                return deviceList;
            });

            return list.isEmpty() ? Optional.empty() : Optional.of(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<DeviceOutput> getDeviceById(String deviceId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {

            String cypher = """
                    MATCH (d:Device {deviceId: $deviceId,isDeleted:FALSE})
                    OPTIONAL MATCH (d)-[r1 {isDeleted:FALSE}]->(s:ShelfPosition {isDeleted:FALSE})
                    OPTIONAL MATCH (s)-[r2 {isDeleted:FALSE}]->(sh:Shelf {isDeleted:FALSE})
                    RETURN d.deviceId AS deviceId,
                           d.deviceName AS deviceName,
                           d.partNumber AS partNumber,
                           d.buildingName AS buildingName,
                           d.deviceType AS deviceType,
                           d.isDeleted AS isDeleted,
                           d.createdAt AS createdAt,
                           d.updatedAt AS updatedAt,
                           d.numShelfPositions AS numberOfShelfPositions,
                    collect({shelfPosId:s.shelfPosId,shelfPosCreatedAt:s.createdAt,
                    shelfPosDeviceId:s.deviceId,shelfPosUpdatedAt:s.updatedAt,
                    shelfId:sh.shelfId,shelfPartNumber:sh.partNumber,
                    shelfName:sh.shelfName,shelfUpdatedAt:sh.updatedAt,
                    shelfCreatedAt:sh.createdAt}) AS shelfPositions;
                    """;

            return session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("deviceId", deviceId)).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(mapToDevice(records.get(0)));
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch device " + deviceId, e);
        }
    }

    public Optional<DeviceOutput> saveDevice(String deviceType, String buildingName,
            String partNumber, String deviceName) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {

            String cypher = """
                    CREATE (d:Device {
                        deviceId: randomUUID(),
                        deviceType: $deviceType,
                        buildingName: $buildingName,
                        partNumber: $partNumber,
                        deviceName: $deviceName,
                        createdAt: datetime(),
                        updatedAt: datetime(),
                        isDeleted: false
                    })
                    RETURN d.deviceId AS deviceId, d.deviceName AS deviceName, d.partNumber AS partNumber,
                           d.buildingName AS buildingName, d.deviceType AS deviceType,
                           d.isDeleted AS isDeleted,
                           d.createdAt AS createdAt, d.updatedAt AS updatedAt,d.numShelfPositions AS numberOfShelfPositions,[] AS shelfPositions;
                    """;

            // Use HashMap to allow null values
            Map<String, Object> params = new HashMap<>();
            params.put("deviceType", deviceType);
            params.put("buildingName", buildingName); // Can be null
            params.put("partNumber", partNumber);
            params.put("deviceName", deviceName);

            System.out.println("Cypher Parameters: " + params);

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, params).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(mapToDevice(records.get(0)));
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to save device", e);
        }
    }

    public Optional<DeviceOutput> updateDevice(String deviceId, String deviceType, String buildingName,
            String partNumber, String deviceName) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {

            String cypher = """
                    MATCH (d:Device {deviceId: $deviceId,isDeleted:FALSE})
                    SET d.deviceType = $deviceType,
                        d.buildingName = $buildingName,
                        d.partNumber = $partNumber,
                        d.deviceName = $deviceName,
                        d.updatedAt = datetime()
                    RETURN d.deviceId AS deviceId, d.deviceName AS deviceName, d.partNumber AS partNumber,
                           d.buildingName AS buildingName, d.deviceType AS deviceType,
                           d.isDeleted AS isDeleted,
                           d.createdAt AS createdAt, d.updatedAt AS updatedAt,d.numShelfPositions AS numberOfShelfPositions, [] AS shelfPositions;
                    """;

            Map<String, Object> params = new HashMap<>();
            params.put("deviceId", deviceId);
            params.put("deviceType", deviceType);
            params.put("buildingName", buildingName);
            params.put("partNumber", partNumber);
            params.put("deviceName", deviceName);

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, params).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(mapToDevice(records.get(0)));
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to update device " + deviceId, e);
        }
    }

    public boolean deleteDevice(String deviceId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {

            String cypher = """
                        MATCH (device:Device {deviceId: $deviceId,isDeleted:FALSE})
                        SET device.isDeleted = true,
                            device.updatedAt=datetime()

                        WITH device
                        MATCH (device)-[r1:HAS]->(shelfPosition:ShelfPosition)
                        SET shelfPosition.isDeleted = true , r1.isDeleted=true,r1.updatedAt=datetime(),shelfPosition.updatedAt=datetime()

                        WITH shelfPosition
                        MATCH (shelfPosition)-[r2:HAS]->(shelf:Shelf)
                        SET r2.isDeleted = true, r2.updatedAt=datetime()
                    """;

            Map<String, Object> params = Map.of("deviceId", deviceId);

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, params).list();
                return records.isEmpty(); // Return true if the device was found and marked as deleted
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete device " + deviceId, e);
        }
    }

    private DeviceOutput mapToDevice(Record rec) {
        DeviceOutput d = new DeviceOutput();
        System.out.println("111*******");
        d.setDeviceId(getString(rec, "deviceId"));
        d.setDeviceName(getString(rec, "deviceName"));
        d.setPartNumber(getString(rec, "partNumber"));
        d.setBuildingName(getString(rec, "buildingName"));
        d.setDeviceType(getString(rec, "deviceType"));
        d.setCreatedAt(getZonedDateTime(rec, "createdAt"));
        d.setUpdatedAt(getZonedDateTime(rec, "updatedAt"));
        d.setNumberOfShelfPositions(getLong(rec,"numberOfShelfPositions"));
        Iterable<Value> values = rec.get("shelfPositions").values();
        System.out.println(values);
        List<ShelfPositionOutput> shelfPositions = new ArrayList<>();

        for (Value value : values) {
            Map<String, Object> props = value.asMap();
            ShelfPositionOutput shelfPositionOutput = new ShelfPositionOutput();

            shelfPositionOutput.setShelfPosId((String) props.get("shelfPosId"));
            shelfPositionOutput.setCreatedAt((ZonedDateTime) props.get("shelfPosCreatedAt"));
            shelfPositionOutput.setUpdatedAt((ZonedDateTime) props.get("shelfPosUpdatedAt"));
            shelfPositionOutput.setDeviceId(d.getDeviceId());

            ShelfOutput shelfOutput = new ShelfOutput();

            shelfOutput.setCreatedAt((ZonedDateTime) props.get("shelfCreatedAt"));
            shelfOutput.setUpdatedAt((ZonedDateTime) props.get("shelfUpdatedAt"));
            shelfOutput.setPartNumber((String) props.get("shelfPartNumber"));
            shelfOutput.setShelfName((String) props.get("shelfName"));
            shelfOutput.setShelfId((String) props.get("shelfId"));

            shelfPositionOutput.setShelfOutput(shelfOutput);

            shelfPositions.add(shelfPositionOutput);
        }

        d.setShelfPosition(shelfPositions);

        // Map shelf positions

        return d;
    }

    // Helper methods for null-safe extraction
    private String getString(Record rec, String key) {
        Value v = rec.get(key);
        return v.isNull() ? null : v.asString();
    }

    private Long getLong(Record rec, String key) {
        Value v = rec.get(key);
        return v.isNull() ? null : v.asLong();
    }

    private ZonedDateTime getZonedDateTime(Record rec, String key) {
        Value v = rec.get(key);
        return v.isNull() ? null : v.asZonedDateTime();
    }
}