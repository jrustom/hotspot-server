package com.hotspot.model;

import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String profilePicture;
    private HashMap<String, VoteType> voteRecords;

    public User(String username, String password, String profilePicture) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.voteRecords = new HashMap<>();
    }

    public void addVoteRecord(String hotspotId, VoteType voteType) {
        this.voteRecords.put(hotspotId, voteType);
    }

    public void removeVoteRecord(String hotspotId, VoteType voteType) {
        this.voteRecords.remove(hotspotId, voteType);
    }

    public enum VoteType {
        DOWNVOTE,
        UPVOTE
    }
}
