#!/usr/bin/bash
if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo Your java version is "$version"
    if [[ "$version" < "11" ]]; then
        echo "You need at least Java 11 to run it"
        exit 0 
    fi
fi

mkdir -p out

echo Compiling from sources
javac -d out/ @sources
echo Java file compiled in ./out/

java -cp out Main $1 $2 $3 $4
