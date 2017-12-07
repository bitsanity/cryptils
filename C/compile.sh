#!/bin/bash

javah -cp ../java:. tbox.Secp256k1

gcc -D __int64="long long" -c -fPIC -I"$HOME/secp256k1/include" -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -shared tbox_Secp256k1.c

gcc -shared -o ../lib/libtbox.so tbox_Secp256k1.o $HOME/secp256k1/.libs/libsecp256k1.so

