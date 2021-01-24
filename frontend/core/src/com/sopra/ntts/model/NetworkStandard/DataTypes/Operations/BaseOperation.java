package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.Objects;

/**
 * BaseOperation provides a fundament for Operation and is not being used directly itself!
 * <p>
 * type: describes the type of an Operation (see OperationEnum)
 * successful: ells us weather an operation was successful or not
 * target: rovides information about the target field/character of an operation
 */
public class BaseOperation {
    private OperationEnum type;
    private Boolean successful;
    private Point target;

    public BaseOperation(OperationEnum type, Boolean successful, Point target) {
        this.type = type;
        this.successful = successful;
        this.target = target;
    }

    public OperationEnum getType() {
        return type;
    }

    public void setType(OperationEnum type) {
        this.type = type;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseOperation that = (BaseOperation) o;
        return getType() == that.getType() &&
                Objects.equals(getSuccessful(), that.getSuccessful()) &&
                Objects.equals(getTarget(), that.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getSuccessful(), getTarget());
    }

    @Override
    public String toString() {
        return "BaseOperation{" +
                "type=" + type +
                ", successful=" + successful +
                ", target=" + target +
                '}';
    }
}
