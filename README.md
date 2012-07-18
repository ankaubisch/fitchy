fitchy - Feature toggles for Java
=================================

A simple java library to use [feature toggles / feature switches](http://en.wikipedia.org/wiki/Feature_toggle) in your application. It provides a simple annotation based configuration to implement toggles into your code. Currently it supports only simple java projects where you manually add toggle support to a class. You mustn't use annotation to enable toggling, you can also use an anonymous class that provide a possibility to switch between enabled and disabled status.

With this early version you have the ability to configure your provided features in a property file an load it at runtime. There is also a possibility to configure your feature with pure java only.

This library uses two types of proxy mechanics to enable toggles on an object. You can configure your fitchy to proxying an object with [cglib](http://cglib.sourceforge.net/) or just the [standard java proxy](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/reflect/Proxy.html). The default behaviour is to create a proxy with the standard java library.

This library is built with [maven](http://maven.apache.org/) and is available under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0 Apache License 2.0).

Basic Usage
-----------
Create a property file filled with features. You can define your own notation of feature names.

    test.feature.1=on
    test.feature.2=off

Create a FeatureContext from property file

    FeatureContext context = Fitchy.loadFromResource("/features.properties");

Proxying your target object (please don't forget that this object needs to implement an interface

    ImplementedInterface proxy = Fitchy.observe(targetObject, context);

Add the FeatureSwitch annotation to your target class that implements ImplementedInterface

    public class TargetClass implements ImplementedInterface {
      @FeatureSwitch("test.feature.1")
      @Override
      public Object method() {
        return seomething;
      }
    }

Now you are ready to use this toggles. In this example the implemented method will return `something`. You can edit this annotation and use `test.feature.2` and you will see that something isn't returned. It will return null instead.
