package com.example.appspringdata.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.appspringdata.Utils.DeviceOutput;
import com.example.appspringdata.Utils.ShelfOutput;
import com.example.appspringdata.Utils.ShelfPositionInput;
import com.example.appspringdata.Utils.ShelfPositionOutput;

@Repository
public class ShelfPositionRepository {
    @Autowired
    private Driver driver;

    private static final String DATABASE = "neo4j";

    public Optional<List<ShelfPositionOutput>> getAllShelfPositions(Long pageNum) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (d:Device {isDeleted:FALSE})-[:HAS]->(s:ShelfPosition {isDeleted:FALSE})
                    OPTIONAL MATCH (sh:Shelf {isDeleted:FALSE})<-[r:HAS {isDeleted:FALSE}]-(s:ShelfPosition {isDeleted:FALSE})
                    RETURN
                        s.createdAt AS createdAt_shelfPosition,
                        s.shelfPosId AS shelfPosId,
                        d.deviceId AS deviceId,
                        s.updatedAt AS updatedAt_shelfPosition,
                        sh.shelfId AS shelfId,
                        sh.partNumber AS partNumber_Shelf,
                        sh.shelfName AS shelfName,
                        sh.createdAt AS createdAt_Shelf,
                        sh.updatedAt AS updatedAt_Shelf
                    SKIP $pageNum LIMIT 50;
                    """;

            List<ShelfPositionOutput> list = session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("pageNum", pageNum)).list();
                List<ShelfPositionOutput> deviceList = new ArrayList<>();
                for (Record record : records) {
                    deviceList.add(mapToShelfPositionOutput(record));
                }
                return deviceList;
            });

            return list.isEmpty() ? Optional.empty() : Optional.of(list);
        } catch (Exception e) {
            System.out.println("**********");
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public Optional<ShelfPositionOutput> getShelfPositionById(String shelfPositionId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (d:Device {isDeleted:FALSE})-[:HAS]->(s:ShelfPosition {shelfPosId: $shelfPositionId ,isDeleted:FALSE})
                    OPTIONAL MATCH (sh:Shelf {isDeleted:FALSE})<-[r:HAS {isDeleted:FALSE}]-(s:ShelfPosition {isDeleted:FALSE})
                    RETURN
                        s.createdAt AS createdAt_shelfPosition,
                        s.shelfPosId AS shelfPosId,
                        d.deviceId AS deviceId,
                        s.updatedAt AS updatedAt_shelfPosition,
                        sh.shelfId AS shelfId,
                        sh.partNumber AS partNumber_Shelf,
                        sh.shelfName AS shelfName,
                        sh.createdAt AS createdAt_Shelf,
                        sh.updatedAt AS updatedAt_Shelf
                    """;

            return session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("shelfPositionId", shelfPositionId)).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(mapToShelfPositionOutput(records.get(0)));
                }
            });
        } catch (Exception e) {
            System.out.println("**********");
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public Optional<ShelfPositionOutput> createShelfPosition(String deviceId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (d:Device {deviceId:$deviceId,isDeleted:FALSE})
                    CREATE (s:ShelfPosition{
                        shelfPosId:randomUUID(),
                        createdAt:datetime(),
                        updatedAt:datetime(),
                        isDeleted:false,
                        deviceId:$deviceId
                    })
                    MERGE (s)<-[r:HAS {isDeleted:FALSE}]-(d)
                    SET r.createdAt=datetime()
                    SET r.updatedAt=datetime()
                    SET d.createdAt=datetime()
                    RETURN
                        s.createdAt AS createdAt_shelfPosition,
                        s.shelfPosId AS shelfPosId,
                        d.deviceId AS deviceId,
                        s.updatedAt AS updatedAt_shelfPosition;
                    """;

            return session.executeWrite(tx -> {
                List<Record> record = tx.run(cypher, Map.of("deviceId", deviceId)).list();
                if (record.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(mapToShelfPositionOutput(record.get(0)));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteShelfPosition(String shelfPositionId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();
        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (s:ShelfPosition {shelfPosId: $shelfPositionId , isDeleted:FALSE})
                    MATCH (s)<-[r1:HAS]-(d:Device)
                    WHERE r1.isDeleted = false AND d.isDeleted = false AND s.isDeleted = false
                    OPTIONAL MATCH (sh:Shelf)<-[r2:HAS]-(s)
                    WHERE r2.isDeleted = false AND sh.isDeleted = false
                    SET r1.isDeleted = true,
                        r1.updatedAt = datetime(),
                        s.isDeleted = true,
                        s.updatedAt = datetime(),
                        r2.isDeleted = true,
                        r2.updatedAt = datetime(),
                        d.numShelfPositions=d.numShelfPositions-1
                    RETURN s.shelfPosId, d.deviceId, sh.shelfId
                    """;

            return session.executeWrite(tx -> {
                List<Record> record = tx.run(cypher, Map.of("shelfPositionId", shelfPositionId)).list();
                return !record.isEmpty();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<ShelfPositionOutput> attachShelf(String shelfPositionId, String shelfId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String check1 = """
                    MATCH (s:ShelfPosition {shelfPosId: $shelfPositionId,isDeleted:FALSE})-[r]->()
                    WHERE r.isDeleted=false
                    RETURN s;
                    """;

            String check2 = """
                    MATCH (sh:Shelf {shelfId:$shelfId})<-[r]-()
                    WHERE r.isDeleted=false
                    RETURN sh;
                    """;

            boolean flag1 = session.executeWrite(tx -> {
                List<Record> record = tx.run(check1, Map.of("shelfPositionId", shelfPositionId))
                        .list();
                if (record.isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            });

            boolean flag2 = session.executeWrite(tx -> {
                List<Record> record = tx.run(check2, Map.of("shelfId", shelfId))
                        .list();
                if (record.isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            });

            if (!(flag1 && flag2)) {
                return Optional.empty();
            }

            String cypher = """
                    MATCH (s:ShelfPosition {shelfPosId: $shelfPositionId,isDeleted:FALSE})
                    MATCH (sh:Shelf {shelfId: $shelfId,isDeleted:FALSE})
                    WHERE s.isDeleted = false AND sh.isDeleted = false

                    MERGE (s)-[r:HAS]->(sh)
                    SET r.createdAt = datetime(),
                        r.updatedAt = datetime(),
                        sh.updatedAt = datetime(),
                        s.updatedAt = datetime(),
                        r.isDeleted = false
                    RETURN
                        s.createdAt AS createdAt_shelfPosition,
                        s.shelfPosId AS shelfPosId,
                        s.updatedAt AS updatedAt_shelfPosition,
                        sh.shelfId AS shelfId,
                        sh.partNumber AS partNumber_Shelf,
                        sh.shelfName AS shelfName,
                        sh.createdAt AS createdAt_Shelf,
                        sh.updatedAt AS updatedAt_Shelf;
                    """;

            return session.executeWrite(tx -> {
                List<Record> record = tx.run(cypher, Map.of("shelfPositionId", shelfPositionId, "shelfId", shelfId))
                        .list();
                if (record.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(mapToShelfPositionOutput(record.get(0)));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ShelfPositionOutput> detachShelf(String shelfPositionId, String shelfId) {
        System.out.println(shelfPositionId + "  " + shelfId);
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                        MATCH (s:ShelfPosition {shelfPosId:$shelfPositionId,isDeleted:FALSE})-[r:HAS {isDeleted:FALSE}]->(sh:Shelf {shelfId:$shelfId,isDeleted:FALSE})
                        MATCH (s)<-[r1:HAS {isDeleted:FALSE}]-(d:Device)
                        SET r.isDeleted=true,
                            s.updatedAt=datetime(),
                            r.updatedAt=datetime()
                        RETURN
                            s.createdAt AS createdAt_shelfPosition,
                            s.shelfPosId AS shelfPosId,
                            s.updatedAt AS updatedAt_shelfPosition,
                            sh.shelfId AS shelfId,
                            sh.partNumber AS partNumber_Shelf,
                            sh.shelfName AS shelfName,
                            sh.createdAt AS createdAt_Shelf,
                            sh.updatedAt AS updatedAt_Shelf;
                    """;

            return session.executeWrite(tx -> {
                List<Record> record = tx.run(cypher, Map.of("shelfPositionId", shelfPositionId, "shelfId", shelfId))
                        .list();
                if (record.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(mapToShelfPositionOutput(record.get(0)));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ShelfPositionOutput mapToShelfPositionOutput(Record record) {
        ShelfPositionOutput shelfPositionOutput = new ShelfPositionOutput();

        shelfPositionOutput.setCreatedAt(record.get("createdAt_shelfPosition").isNull() ? null
                : record.get("createdAt_shelfPosition").asZonedDateTime());
        shelfPositionOutput
                .setShelfPosId(record.get("shelfPosId").isNull() ? null : record.get("shelfPosId").asString());
        shelfPositionOutput.setDeviceId(record.get("deviceId").isNull() ? null : record.get("deviceId").asString());
        shelfPositionOutput.setUpdatedAt(record.get("updatedAt_shelfPosition").isNull() ? null
                : record.get("updatedAt_shelfPosition").asZonedDateTime());

        if (!record.get("shelfId").isNull()) {
            ShelfOutput shelfOutput = new ShelfOutput();
            shelfOutput.setShelfId(record.get("shelfId").isNull() ? null : record.get("shelfId").asString());
            shelfOutput.setShelfName(record.get("shelfName").isNull() ? null : record.get("shelfName").asString());
            shelfOutput.setPartNumber(
                    record.get("partNumber_Shelf").isNull() ? null : record.get("partNumber_Shelf").asString());
            shelfOutput
                    .setCreatedAt(record.get("createdAt_Shelf").isNull() ? null
                            : record.get("createdAt_Shelf").asZonedDateTime());
            shelfOutput
                    .setUpdatedAt(record.get("updatedAt_Shelf").isNull() ? null
                            : record.get("updatedAt_Shelf").asZonedDateTime());
            shelfPositionOutput.setShelfOutput(shelfOutput);
        }

        return shelfPositionOutput;
    }

}
