package org.hse.moodactivities.data.entities.mongodb;

import dev.morphia.annotations.*;

import java.util.List;

@Entity("users")
@Indexes(@Index(options = @IndexOptions(name = "id"), fields = @Field("id")))
public class User {

    @Id
    private String id;

    private List<UserDayMeta> metas;

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

    public void updateMeta(final UserDayMeta meta) {
        if (metas.isEmpty()) {
            metas.add(meta);
        }
        UserDayMeta lastMeta = metas.getLast();
        if (lastMeta.getDate().equals(meta.getDate())) {
            for (var activity : meta.getActivityList()) {
                lastMeta.addActivity(activity);
            }
            for (var mood : meta.getMoodList()) {
                lastMeta.addMood(mood);
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
