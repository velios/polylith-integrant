# Polylith + Integrant

This example demonstrates a basic setup of a stateful system (implemented as a
[Polylith](https://polylith.gitbook.io/) `base`) whose lifecycle is handled by
the [Integrant](https://github.com/weavejester/integrant). It caters for both
in-REPL development and production use cases.

## System Components

The most frequently asked system was taken as an illustrative example. It uses
several single-purpose components to work with a traditional database, in this
case PostgreSQL.

Unfortunately, the term "component" becomes overloaded in the current context.
It can both mean a Polylith component (a type of brick) and an Integrant system
component (a.k.a. a "key" in Integrant's parlance). In order not to go nuts and
at the same time not to introduce new terms further complicating understanding,
we will use this term with qualifiers — "stateful" and "stateless".

By "stateful" we mean components that are part of the Integrant system (used at
runtime) and that may also have Polylith counterparts (used at build time). And
by "stateless" we mean regular Polylith components that do not become a part of
the Integrant system's state.

The minimal set of system components:

| Component     | Polylith name | Integrant system key            | Description                                                                                                                                                                               |
|---------------|---------------|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Config        | `config`      | `:integrant.system/config`      | A "stateful" component encapsulating the system runtime configuration. Every system should start with this one. Here we have it for completeness and keep its implementation dead simple. |
| Embedded DB   | `embedded-pg` | `:integrant.system/embedded-pg` | A "stateful" component which should be divided into two parts along the boundary between the component and the Integrant system that merely prepares arguments and calls its methods.     |
| DataSource    | n/a           | `:integrant.system/data-source` | A "stateful" component which is only required at runtime (to be started and stopped properly), i.e. lacks a Polylith counterpart.                                                         |
| DB Operations | `pg-ops`      | n/a                             | A regular "stateless" component whose methods are parametrized by the required system state (e.g. `data-source`) or its derivatives.                                                      |

### Run the Application

You can run the application from the root of the workspace:

```
clojure -M:dev -m integrant.system.core
;; or for run with "test" implementation
clojure -M:dev -m integrant.system.core test-ig-config
```

### Run the tests with:

```
poly test
```

## Build an Uberjar

For production deployment, you typically want to build an "uberjar" -- a `.jar` file that contains Clojure itself and all of the code from your application and its dependencies, so that you can run it with the `java -jar` command.

```
cd projects/system-first && clj -T:build uber
;; or
cd projects/system-second && clj -T:build uber
```

It will also include the standalone `.jar` file which you can run like this:

```
java -jar projects/target/systemfirst-standalone.jar
;; or
java -jar projects/target/systemsecond-standalone.jar
```