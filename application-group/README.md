# Application Group Descriptor

This project contains the Application Group descriptor file which defines the
apps, streams and standalone applications that are included in this Application Group.

## Descriptor file

The descriptor file has the following structure:

```yaml
# The 'apps' section translates directly into 'dataflow:>app import --uri ...'
# Each app listed will be imported as part of this application group.
# See http://docs.spring.io/spring-cloud-dataflow/docs/current/reference/html/spring-cloud-dataflow-register-apps.html
apps:
  - name: some-source # the name that the app will be registered with
    type: source # the application type
    uri:  some-source-kafka # the shorthand version of the resource URI. This gets explanded to the full `maven://groupId:artifactId:version` by the Spring Data Flow Maven Plugin
  - name: some-sink
    type: sink
    uri:  some-sink-kafka
    
# The 'standalone' section translates directly into 'dataflow:>standalone create ...'
# See https://blog.switchbit.io/introducing-standalone-applications-to-spring-cloud-data-flow
# for more on the standalone application type
standalone:
  - name: some-app
    dsl: some-app --server.port=8080

# The 'stream' section  translates directly into 'dataflow:>stream create ...'
# Each stream listed below will be created and deployed.
# See the 'deploymentProperties' section under the 'spring-cloud-dataflow-maven-plugin'
# configuration section in the 'pom.xml.'
# See http://docs.spring.io/spring-cloud-dataflow/docs/current/reference/html/spring-cloud-dataflow-create-stream.html
stream:
  - name: some-stream
    dsl: some-source | some-sink
```
