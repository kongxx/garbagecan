#!/bin/sh

N=$1
if [ "x$N" == "x" ]; then
	N=100
fi
echo "scale=$N; a(1)*4" | bc -l
#sleep 5 

