#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# محاولة تحديد الأمر المناسب لتشغيل JVM
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

# تحديد مسار السكريبت الحالي
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" > /dev/null
BASEDIR="`pwd`"
cd "$SAVED"

# إعداد متغيرات البيئة لـ Gradle Wrapper
APP_BASE_NAME=`basename "$0"`
APP_HOME=`cd "$BASEDIR" && pwd`

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
# عند وجود JAVA_HOME
if [ -n "$JAVA_HOME" ] ; then
    exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
else
    exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
fi
