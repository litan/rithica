#
# This ProGuard configuration file illustrates how to process applets.
# Usage:
#     java -jar proguard.jar @applet.pro
#

# Specify the input jars, output jars, and library jars.

-injars  ../../dist/rithica.jar; ../../lib/table-layout.jar; <scala.home>/lib/scala-library.jar
-outjars rithicaw-sub.jar

-libraryjars <java.home>/lib/rt.jar

# Preserve all public applets.

-keep public class net.kogics.rithica.subtraction.GameApplet

-target 1.6

# Print out a list of what we're preserving.

-printseeds
-ignorewarnings

-dontskipnonpubliclibraryclasses
# -dontskipnonpubliclibraryclassmembers	
#-dontshrink
#-dontobfuscate

# Preserve all annotations.

-keepattributes *Annotation*

-keepclassmembers class java.awt.Component {
    public java.awt.ComponentOrientation getComponentOrientation();
}

# Preserve all native method names and the names of their classes.

-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve the special static methods that are required in all enumeration
# classes.

-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
# You can comment this out if your library doesn't use serialization.
# If your code contains serializable classes that have to be backward
# compatible, please refer to the manual.

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Your application may contain more items that need to be preserved;
# typically classes that are dynamically created using Class.forName:

# -keep public class mypackage.MyClass
# -keep public interface mypackage.MyInterface
# -keep public class * implements mypackage.MyInterface

