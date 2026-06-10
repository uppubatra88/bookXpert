#!/bin/sh

APP_HOME=$(cd "$(dirname "$0")" && pwd -P)
JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$JAR" ]; then
  echo "gradle-wrapper.jar is missing. Open the project in Android Studio or regenerate the wrapper with: gradle wrapper" >&2
  exit 1
fi

exec java -jar "$JAR" "$@"
