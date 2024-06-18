package org.hse.moodactivities.data.entities.mongodb;

import dev.morphia.annotations.Entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class FitnessData implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;

    private int steps = 0;

    public int getSteps() {
        return steps;
    }

    public FitnessData(int _steps) {
        steps = _steps;
    }

    public FitnessData() {
    }

    public void setSteps(int _steps) {
        steps = _steps;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FitnessData) obj;
        return this.steps == that.steps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps);
    }

    @Override
    public String toString() {
        return "FitnessData[" +
                "steps=" + steps + ", " +
                ']';
    }
}
