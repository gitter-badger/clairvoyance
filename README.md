Clairvoyance
============

Clairvoyance is an extension to Specs2, a Scala test library. Clairvoyance provides a few extensions to capture what is happening in your tests, and then produce business and tester friendly documentation.

It's a pastiche of [Yatspec](http://code.google.com/p/yatspec), a Java testing library written by my colleague Dan Bodart. It addresses the deficiencies we experienced with Fit and Concordion.

At the moment it's a major work in progress, so I wouldn't test your nuclear reactor with it just yet.

Example
-------

The full source to this example is [here](https://github.com/rhyskeepence/clairvoyance/blob/master/src/test/scala/clairvoyance/examples/LoggingExample.scala).

It breaks down like this:

* We create a Spec which extends `ClairvoyantSpec`
* We write our spec in the mutable spec style (my team is scared of the immutable style)
* We create a context which extends `ClairvoyantContext`
* `InterestingGivens` can be added with statements such as `interestingGivens += ("Current date" -> "21/12/2012")`
* The spec method is interpreted into a text specification, so keep it readable

```scala
class LoggingExample extends ClairvoyantSpec {
  "The co-ordinator" should {
    "invoke the Doomsday Device on the 21st of December 2012" in new context {
      givenTheDateIs("21/12/2012")
      whenTheCoordinatorRuns
      theDoomsdayDevice should beUnleashed
    }
  }

  trait context extends ClairvoyantContext {
    // test set up and fixtures
  }
}
```

Here is the output of this spec.
![Example output](http://github.com/rhyskeepence/clairvoyance/raw/master/doc/example-output.jpg)

Get This Party Started
----------------------

Add this to your SBT build:

    libraryDependencies ++= Seq(
        "rhyskeepence" %% "clairvoyance" % "5"
    )
    resolvers ++= Seq(
        "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
        "rhys's releases" at "https://github.com/rhyskeepence/mvn-repo/raw/master/releases"
    )

At the moment, clairvoyant relies on a few features in Specs2 1.9-SNAPSHOT - hence the snapshot repository.

Interesting Givens
------------------

These are inputs into your test, which may not be specified in the spec, but should be logged to the output:

```scala
  interestingGivens += ("Current date" -> "21/12/2012")
```

or

```scala
  ("Current date" -> "21/12/2012").isInteresting
```


Captured Inputs And Outputs
---------------------------

These are the inputs or outputs to your system, which may not be practical to assert upon, but should be logged.

Perhaps you are using a stub rather than communicating with a third party in your spec:

```scala
class StubGizmometer extends Gizmometer {
}
```

To capture inputs and outputs, just add the `ProducesCapturedInputsAndOutputs` trait and call `captureValue`:

```scala
class StubGizmometer extends Gizmometer with ProducesCapturedInputsAndOutputs {
  def scan(brain: Brain) {
    captureValue("Brain" -> brain)
  }
}
```

and in your context, register the stub so that clairvoyant knows about it:

```scala
trait context extends ClairvoyantContext {
    val gizmometer = new StubGizmometer
    override def capturedInputsAndOutputs = Seq(gizmometer)
}
```

Custom Rendering of Interesting Givens & Captured Inputs And Outputs
--------------------------------------------------------------------

When you capture a value or an interesting given, it will be rendered to the screen. XML and Strings are formatted nicely by default, but you may wish to capture your own domain objects and have them presented in readable format.

A full example is here: [clairvoyance/examples/CustomRenderingExample.scala](https://github.com/rhyskeepence/clairvoyance/blob/master/src/test/scala/clairvoyance/examples/CustomRenderingExample.scala)

The juicy bits are shown below:

```scala
class CustomRenderingExample extends ClairvoyantSpec with CustomRendering {

  def customRendering = {
    case Brain(iq) => "a Brain with an IQ of %d".format(iq)
  }
}
```

`customRendering` is a partial function, which will be run before the default rendering.

And behold, custom rendering of Brains:

![Custom Rendering of Brains](http://github.com/rhyskeepence/clairvoyance/raw/master/doc/custom-rendering.jpg)

Warning
-------

I'm not completely happy with it so the API will change in the future.

TODO
----

* Table of contents
* Notes
* SVG sequence diagrams
* Scenario tables
* Test output is stored as a mutable queue on the spec class, this is a bit dodgy.
