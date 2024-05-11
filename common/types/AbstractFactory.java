package RSMS.common.types;

public abstract class AbstractFactory<Target, TargetConfig> {

    abstract public Target create(TargetConfig config);
}
