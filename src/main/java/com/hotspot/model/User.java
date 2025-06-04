package com.hotspot.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
    private List<VoteRecord> voteRecords;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.voteRecords = new ArrayList<>();
    }

    public void addVoteRecord(VoteRecord voteRecord) {
        this.voteRecords.add(voteRecord);
    }

    public void removeVoteRecord(VoteRecord voteRecord) {
        this.voteRecords.remove(voteRecord);
    }

    @Getter
    @AllArgsConstructor
    public static class VoteRecord {
        private String hotspotId;
        private VoteType voteType;
    }

    public enum VoteType {
        DOWNVOTE,
        UPVOTE
    }
}
