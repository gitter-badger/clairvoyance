package clairvoyance.specs2

import clairvoyance.{CapturedInputsAndOutputs, InterestingGivens}
import clairvoyance.state.{TestState, TestStates}
import org.specs2.mutable.After

trait ClairvoyantContext extends After with InterestingGivens with CapturedInputsAndOutputs {

  def tearDown(): Unit = {}

  def after: Unit = {
    tearDown()

    TestStates += (keyNameOf(getClass) -> TestState(interestingGivens.toList, gatherCapturedValues))
    clearCapturedValues()
  }

  private def keyNameOf(spec: Class[_]) = spec.getName.split("\\$")(0)

  implicit def stringToStep(description: String) = new ClairvoyantStep(description)
  class ClairvoyantStep(description: String) {
    def ===>[T](step: T): T = step
  }
}
