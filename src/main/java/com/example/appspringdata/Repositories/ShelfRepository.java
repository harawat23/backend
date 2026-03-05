package com.example.appspringdata.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.appspringdata.Exceptions.ConflictException;
import com.example.appspringdata.Exceptions.OperationFailedException;
import com.example.appspringdata.Utils.ShelfOutput;

import org.neo4j.driver.Record;

@Repository
public class ShelfRepository {
    @Autowired
    private Driver driver;

    private static final String DATABASE = "neo4j";

    public Optional<Long> getNumberOfShelves() {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (s:Shelf {isDeleted:FALSE})
                    RETURN
                        count(s) AS numberOfShelves;
                    """;

            Long count = session.executeRead(tx -> {
                var result = tx.run(cypher);
                if (result.hasNext()) {
                    var record = result.next();
                    return record.get("numberOfShelves").asLong();
                }
                return null;
            });

            return Optional.ofNullable(count).map(Long::longValue);
        }catch(Exception e){
            throw new OperationFailedException("Failed to fetch number of shelves "+ e);
        }
    }

    public Optional<List<ShelfOutput>> getShelfByName(String shelfName) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher="""
                    MATCH (s:Shelf {isDeleted:FALSE,shelfName:$shelfName})
                    RETURN
                        s.shelfId as shelfId,
                        s.createdAt as createdAt,
                        s.isDeleted as isDeleted,
                        s.partNumber as partNumber,
                        s.shelfName as shelfName,
                        s.updatedAt as updatedAt
                    """;
            return session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("shelfName", shelfName)).list();
                List<ShelfOutput> deviceList = new ArrayList<>();
                for (Record record : records) {
                    deviceList.add(mapToShelf(record));
                }
                return deviceList.isEmpty() ? Optional.empty() : Optional.of(deviceList);
            });
        } catch (Exception e) {
            throw new OperationFailedException("failed to fetch shelfname "+shelfName);
        }
    }

    public Optional<List<ShelfOutput>> getShelfByPartNumber(String partNumber){
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher="""
                    MATCH (s:Shelf {isDeleted:FALSE,partNumber:$partNumber})
                    RETURN
                        s.shelfId as shelfId,
                        s.createdAt as createdAt,
                        s.isDeleted as isDeleted,
                        s.partNumber as partNumber,
                        s.shelfName as shelfName,
                        s.updatedAt as updatedAt
                    """;
            return session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("partNumber", partNumber)).list();
                List<ShelfOutput> deviceList = new ArrayList<>();
                for (Record record : records) {
                    deviceList.add(mapToShelf(record));
                }
                return deviceList.isEmpty() ? Optional.empty() : Optional.of(deviceList);
            });
        } catch (Exception e) {
            throw new OperationFailedException("failed to fetch shelf with part number "+partNumber);
        }
    }

    public Optional<List<ShelfOutput>> getAllShelfPositions(Long pageNum) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (s:Shelf {isDeleted:FALSE})
                    RETURN
                        s.shelfId as shelfId,
                        s.createdAt as createdAt,
                        s.isDeleted as isDeleted,
                        s.partNumber as partNumber,
                        s.shelfName as shelfName,
                        s.updatedAt as updatedAt
                    SKIP $pageNum LIMIT 50;
                    """;

            List<ShelfOutput> list = session.executeRead(tx -> {
                List<Record> records = tx.run(cypher, Map.of("pageNum", pageNum)).list();
                List<ShelfOutput> deviceList = new ArrayList<>();
                for (Record record : records) {
                    deviceList.add(mapToShelf(record));
                }
                return deviceList;
            });

            return list.isEmpty() ? Optional.empty() : Optional.of(list);
        } catch (Exception e) {
            throw new OperationFailedException("failed to fetch all shelves");
        }
    }

    public Optional<ShelfOutput> getShelfById(String shelfId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.READ)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                        MATCH (s:Shelf {shelfId:$shelfId,isDeleted:FALSE})
                        RETURN
                        s.shelfId as shelfId,
                        s.createdAt as createdAt,
                        s.isDeleted as isDeleted,
                        s.partNumber as partNumber,
                        s.shelfName as shelfName,
                        s.updatedAt as updatedAt;
                    """;

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, Map.of("shelfId", shelfId)).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(mapToShelf(records.get(0)));
            });
        } catch (Exception e) {
            throw new OperationFailedException("Failed to fetch device " + shelfId);
        }
    }

    public Optional<ShelfOutput> saveShelf(String partNumber, String shelfName) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                        CREATE (s:Shelf {
                            shelfId:randomUUID(),
                            isDeleted:false,
                            createdAt:datetime(),
                            partNumber:$partNumber,
                            shelfName:$shelfName,
                            updatedAt:datetime()
                        })

                        RETURN
                        s.shelfId as shelfId,
                        s.createdAt as createdAt,
                        s.isDeleted as isDeleted,
                        s.partNumber as partNumber,
                        s.shelfName as shelfName,
                        s.updatedAt as updatedAt;
                    """;

            HashMap<String, Object> params = new HashMap<>();
            params.put("partNumber", partNumber);
            params.put("shelfName", shelfName);

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, params).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(mapToShelf(records.get(0)));
            });
        } catch (Exception e) {
            throw new OperationFailedException("failed to save shelf");
        }
    }

    public boolean deleteShelf(String shelfId) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String query1="""
                    MATCH (s:Shelf {shelfId:$shelfId})
                    RETURN s.isDeleted as isDeleted;
                    """;
            
            Optional<Boolean> deletionState=session.executeRead(tx->{
                List<Record> records=tx.run(query1,Map.of("shelfId",shelfId)).list();
                if (records.isEmpty()){
                    Optional.empty();
                }
                return Optional.of(records.get(0).get("isDeleted").asBoolean());
            });

            if (deletionState.get()==true){
                throw new ConflictException("shelf Already Deleted");
            }

            String cypher = """
                    MATCH (s:Shelf {shelfId:$shelfId,isDeleted:FALSE})
                    OPTIONAL MATCH (s)<-[r:HAS {isDeleted:FALSE}]-(sh:ShelfPosition {isDeleted:FALSE})
                    SET s.isDeleted = true,
                        s.updatedAt = datetime(),
                        r.isDeleted = true
                    RETURN s.shelfId;
                    """;
            Map<String, Object> params = Map.of("shelfId", shelfId);

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, params).list();
                return !records.isEmpty();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ShelfOutput> updateShelf(String shelfId, String shelfName, String partNumber) {
        SessionConfig sessionConfig = SessionConfig.builder()
                .withDatabase(DATABASE)
                .withDefaultAccessMode(AccessMode.WRITE)
                .build();

        try (Session session = driver.session(sessionConfig)) {
            String cypher = """
                    MATCH (s:Shelf {shelfId:$shelfId ,isDeleted:FALSE})
                    SET s.shelfName=$shelfName,
                        s.partNumber=$partNumber,
                        s.updatedAt=datetime()
                    RETURN
                        s.shelfId as shelfId,
                        s.createdAt as createdAt,
                        s.isDeleted as isDeleted,
                        s.partNumber as partNumber,
                        s.shelfName as shelfName,
                        s.updatedAt as updatedAt;
                    """;
            Map<String, Object> params = new HashMap<>();
            params.put("shelfId", shelfId);
            params.put("shelfName", shelfName);
            params.put("partNumber", partNumber);

            return session.executeWrite(tx -> {
                List<Record> records = tx.run(cypher, params).list();
                if (records.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(mapToShelf(records.get(0)));
                }
            });
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete shelf "+shelfId);
        }
    }

    private ShelfOutput mapToShelf(Record record) {
        ShelfOutput s = new ShelfOutput(null, null);
        System.out.println(record.get("shelfId").asString());
        s.setShelfId(record.get("shelfId").asString());
        s.setCreatedAt(record.get("createdAt").asZonedDateTime());
        s.setPartNumber(record.get("partNumber").asString());
        s.setShelfName(record.get("shelfName").asString());
        s.setUpdatedAt(record.get("updatedAt").asZonedDateTime());

        return s;
    }
}