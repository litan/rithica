java -Xmx1024m -Dscala.home="$SCALA_HOME" -jar `cygpath -w ~/tools/proguard4.3/lib/proguard.jar` -verbose @applet.pro
pack200 rithicaw-add.jar.pack.gz rithicaw-add.jar
ls -ltr
echo Copying jar files to App Engine workspace...
cp rithicaw-add.jar.pack.gz ~/netbeans/KogicsAeApp/src/static/jar/
cp rithicaw-add.jar ~/netbeans/KogicsAeApp/src/static/jar/
echo Done.
ls -ltr ~/netbeans/KogicsAeApp/src/static/jar/

