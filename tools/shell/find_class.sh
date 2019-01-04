#!/bin/sh

find_dir=$1
find_key=$2

jars=`find $find_dir -name '*.jar'`
for jar in $jars
do
    ret=`jar tvf $jar | grep $find_key`
    if [ "$?" = "0" ]; then
        ret=`echo $ret | awk '{print $8}'`
        echo -e "\e[1;34m${jar}\e[0m: \e[2;34m${ret}\e[0m"
    fi
done

wars=`find $find_dir -name '*.war'`
for war in $wars
do
    ret=`jar tvf $war | grep $find_key`
    if [ "$?" = "0" ]; then
        ret=`echo $ret | awk '{print $8}'`
        echo -e "\e[1;34m${war}\e[0m: \e[2;34m${ret}\e[0m"
    fi
done
