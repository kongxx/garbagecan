
``` shell
mvn archetype:generate                              \
  -DarchetypeGroupId=org.apache.flink               \
  -DarchetypeArtifactId=flink-quickstart-scala      \
  -DarchetypeVersion=1.8.0                          \
  -DgroupId=my.flinkstudy                           \
  -DartifactId=flinkstudy                           \
  -Dversion=0.1                                     \
  -Dpackage=my.flinkstudy                           \
  -DinteractiveMode=false
```

``` shell
${FLINK_HOME}/bin/flink run -c my.flinkstudy.WordCount target/flinkstudy-0.1.jar ./
```

``` shell
${FLINK_HOME}/bin/flink run -c my.flinkstudy.BatchJob target/flinkstudy-0.1.jar ./
```

``` shell
nc -lk 9001

${FLINK_HOME}/bin/flink run -c my.flinkstudy.StreamingJob target/flinkstudy-0.1.jar 9001

cd ${FLINK_HOME}/log
tail -f flink-*-taskexecutor-*.out
```
