package me.gleeming.skywars.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.gleeming.skywars.leaderboard.Leaderboard;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class MongoDB {
    @Getter private static MongoDB instance;
    @Getter private MongoCollection<Document> playerData;
    @Getter private MongoCollection<Document> kitData;
    @Getter private MongoDatabase database;
    public MongoDB() {
        instance = this;

        database = new MongoClient(new MongoClientURI("mongodb://admin:d0tjarisfat4231@athen.cc:5555")).getDatabase("zSkywars");
        playerData = database.getCollection("playerdata");
        kitData = database.getCollection("kitdata");

        new Leaderboard("WINS", playerData.find());
        new Leaderboard("KILLS", playerData.find());
    }
    public Document getDocument(UUID uuid) {
        try {
            return playerData.find(new Document("UUID", uuid.toString())).first();
        } catch(Exception e) {
            return null;
        }
    }
    public boolean hasProfile(UUID uuid) {
        return getDocument(uuid) != null;
    }
    public void createProfile(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        Document doc = new Document("UUID", uuid.toString());
        doc.append("NAME", offlinePlayer.getName());
        doc.append("KILLS", 0);
        doc.append("DEATHS", 0);
        doc.append("WINS", 0);
        doc.append("COINS", 0);
        doc.append("CAGE", "DEFAULT");
        doc.append("KIT", "NONE");
        playerData.insertOne(doc);
    }
    public void addKill(UUID uuid) {
        Document doc = getDocument(uuid);
        playerData.deleteOne(doc);
        doc.append("KILLS", doc.getInteger("KILLS") + 1);
        playerData.insertOne(doc);
    }
    public String getCage(UUID uuid) {
        return getDocument(uuid).getString("CAGE");
    }
    public void addCoins(UUID uuid, int amount) {
        Document doc = getDocument(uuid);
        playerData.deleteOne(doc);
        doc.append("COINS", doc.getInteger("COINS") + amount);
        playerData.insertOne(doc);
    }
    public boolean hasKit(UUID uuid, String kit) {
        try {
            return kitData.find(new Document("UUID", uuid.toString())).first().getBoolean(kit.toUpperCase());
        } catch(Exception e) {
            return false;
        }
    }
    public void addKit(UUID uuid, String kit) {
        Document doc;
        try {
            doc = kitData.find(new Document("UUID", uuid.toString())).first();
            kitData.deleteOne(doc);
        } catch(Exception e) {
            doc = new Document("UUID", uuid.toString());
        }
        doc.append(kit.toUpperCase(), true);
        kitData.insertOne(doc);
    }
    public void addDeath(UUID uuid) {
        Document doc = getDocument(uuid);
        playerData.deleteOne(doc);
        doc.append("DEATHS", doc.getInteger("DEATHS") + 1);
        playerData.insertOne(doc);
    }
    public void addWin(UUID uuid) {
        Document doc = getDocument(uuid);
        playerData.deleteOne(doc);
        doc.append("WINS", doc.getInteger("WINS") + 1);
        playerData.insertOne(doc);
    }
    public String getKit(UUID uuid) {
        try {
            return getDocument(uuid).getString("KIT");
        } catch(Exception e) {
            return "NONE";
        }
    }
    public void setKit(UUID uuid, String kit) {
        Document doc = getDocument(uuid);
        playerData.deleteOne(doc);
        doc.append("KIT", kit);
        playerData.insertOne(doc);
    }
    public void setCage(UUID uuid, String cage) {
        Document doc = getDocument(uuid);
        playerData.deleteOne(doc);
        doc.append("CAGE", cage.toUpperCase());
        playerData.insertOne(doc);
    }
    public boolean hasCage(String cage, UUID uuid) {
        try {
            return getDocument(uuid).getBoolean(cage.toUpperCase());
        } catch(Exception e) {
            return false;
        }
    }
}
