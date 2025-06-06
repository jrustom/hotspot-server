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
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private HashMap<String, VoteType> voteRecords;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
