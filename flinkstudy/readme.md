mvn archetype:generate                              \
  -DarchetypeGroupId=org.apache.flink               \
  -DarchetypeArtifactId=flink-quickstart-scala      \
  -DarchetypeVersion=1.8.0                          \
  -DgroupId=my.flinkstudy                           \
  -DartifactId=flinkstudy                           \
  -Dversion=0.1                                     \
  -Dpackage=my.flinkstudy                           \
  -DinteractiveMode=false

${FLINK_HOME}/bin/flink run -c my.flink.quickstart.WordCount target/quickstart-0.1.jar ./
