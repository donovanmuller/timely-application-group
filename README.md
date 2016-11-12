# Timely Sample Application Group

This sample application is the subject of a blog post describing
a take on the *application group* feature for [Spring Cloud Data Flow](https://cloud.spring.io/spring-cloud-dataflow/).

See below for more detail:

https://blog.switchbit.io/a-take-on-application-groups-with-spring-cloud-data-flow

## Usage

This project is a multi-module Maven project containing the definitions
and implementations for all stream, task and standalone applications used in the
implementation of the sample application.

## Branches

Each Git branch contains the incremental addition of features to the sample application.
The following branches are available:

| Branch | Description |
| --- | --- |
| master (*current*) | The most basic stream definition. `time-source | log-sink` |
| [timezone-processor](https://github.com/donovanmuller/timely-application-group/tree/timezone-processor) | timezone-processor added to stream. `time-source | timezone-processor ... | log-sink` |
| [timely-file-tap](https://github.com/donovanmuller/timely-application-group/tree/timely-file-tap) | timely-file-tap added. `:timely-stream.timezone-processor > file-sink ...` |
| [timely-task](https://github.com/donovanmuller/timely-application-group/tree/timely-task) | timely-task added and triggered. `trigger-task ... | tasklauncher-local` |
| [timely-events](https://github.com/donovanmuller/timely-application-group/tree/timely-events) | config-server and timely-events added. `trigger-events ...` |

## Deploying

To deploy the application group to a local Data Flow server running on the default ports:

```console
$ ./mvnw scdf:deploy
```

where `scdf:deploy` refers to the [Spring Cloud Data Flow Maven plugin](https://github.com/donovanmuller/spring-cloud-dataflow-maven-plugin)
goal configured in the `application-group` module.

If the Data Flow server is running remotely, the URI can be configured as follows:

```console
$ ./mvnw scdf:deploy -DdeployerServerUri=http://another-server:9393
```

Please refer the to [blog post](https://blog.switchbit.io/a-take-on-application-groups-with-spring-cloud-data-flow)
for examples of both local and a "production" environment.

