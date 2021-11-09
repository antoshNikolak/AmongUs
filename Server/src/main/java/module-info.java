module Server {
    requires kryonet;
    requires kryo;

    requires Shared;
    requires java.sql;
    requires javafx.graphics;//clean install parent module , add maven compiler when module not found

    opens World;

}