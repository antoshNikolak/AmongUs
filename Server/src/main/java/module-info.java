module Server {
    requires kryonet;
    requires kryo;

    requires Shared;
    requires java.sql;

    opens World;
}