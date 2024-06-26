package org.hse.moodactivities.data.entities.mongodb;

import java.util.ArrayList;
import java.util.List;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;

@Entity("users")
@Indexes(@Index(options = @IndexOptions(name = "id"), fields = @Field("id")))
public class User {

    @Id
    private String id;

    private List<UserDayMeta> metas;

    private String promptMetaUpdateDate;
    private String promptMeta;

    private String negativeFeedback;
    private String positiveFeedback;

    public User(final String id, List<UserDayMeta> metas) {
        this.id = id;
        this.metas = metas;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<UserDayMeta> getMetas() {
        return this.metas;
    }

    public void setMetas(final List<UserDayMeta> metas) {
        this.metas = metas;
    }

    public String getPromptMetaUpdateDate() {
        return promptMetaUpdateDate;
    }

    public void setPromptMetaUpdateDate(String promptMetaUpdateDate) {
        this.promptMetaUpdateDate = promptMetaUpdateDate;
    }

    public String getPromptMeta() {
        return promptMeta;
    }

    public void setPromptMeta(String promptMeta) {
        this.promptMeta = promptMeta;
    }

    public String getNegativeFeedback() {
        return negativeFeedback;
    }

    public void setNegativeFeedback(String negativeFeedback) {
        this.negativeFeedback = negativeFeedback;
    }

    public String getPositiveFeedback() {
        return positiveFeedback;
    }

    public void setPositiveFeedback(String positiveFeedback) {
        this.positiveFeedback = positiveFeedback;
    }

    public void updateMeta(final UserDayMeta meta) {
        if (metas == null) {
            metas = new ArrayList<>();
        }
        if (metas.isEmpty()) {
            metas.add(meta);
        }
        UserDayMeta lastMeta = metas.getLast();
        if (lastMeta.getDate().equals(meta.getDate())) {
            for (var incomingRecord : meta.getRecords()) {
                boolean contains = false;
                for (var existingRecord : lastMeta.getRecords()) {
                    if (incomingRecord.getTime().equals(existingRecord.getTime())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    lastMeta.addRecord(incomingRecord);
                }
            }
        } else {
            metas.add(meta);
        }
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", metas=" + metas +
                '}';
    }
}
