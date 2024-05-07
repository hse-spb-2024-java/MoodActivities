package org.hse.moodactivities.data.entities.mongodb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserTest {

    @Test
    public void testUpdateMeta_AddNewMeta() {
        User user = new User("1", new ArrayList<>());
        UserDayMeta meta = new UserDayMeta(LocalDate.of(2024, 5, 7));
        meta.addRecord(new MoodFlowRecord("12:00"));
        user.updateMeta(meta);
        Assertions.assertEquals(1, user.getMetas().size());
    }

    @Test
    public void testUpdateMeta_AddExistingMetaRecord() {
        List<UserDayMeta> metas = new ArrayList<>();
        UserDayMeta meta = new UserDayMeta(LocalDate.of(2024, 5, 7));
        meta.addRecord(new MoodFlowRecord("09:00"));
        metas.add(meta);

        User user = new User("1", metas);
        UserDayMeta newMeta = new UserDayMeta(LocalDate.of(2024, 5, 7));
        newMeta.addRecord(new MoodFlowRecord("09:00"));
        user.updateMeta(newMeta);
        Assertions.assertEquals(1, user.getMetas().size());
        Assertions.assertEquals(1, user.getMetas().get(0).getRecords().size());
    }

    @Test
    public void testUpdateMeta_AddNewMetaWithNewRecord() {
        List<UserDayMeta> metas = new ArrayList<>();
        UserDayMeta meta = new UserDayMeta(LocalDate.of(2024, 5, 7));
        meta.addRecord(new MoodFlowRecord("09:00"));
        metas.add(meta);

        User user = new User("1", metas);
        UserDayMeta newMeta = new UserDayMeta(LocalDate.of(2024, 5, 7));
        newMeta.addRecord(new MoodFlowRecord("10:00"));
        user.updateMeta(newMeta);
        Assertions.assertEquals(2, user.getMetas().getLast().getRecords().size());
    }
}
