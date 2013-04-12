package pl.edu.agh.istuff.internal

import org.osgi.framework.{ServiceEvent, ServiceListener, BundleContext, BundleActivator}


class Activator extends BundleActivator with ServiceListener{
  def start(context: BundleContext) {
    println("Starting to listen for service events.")
    context.addServiceListener(this)
  }

  def stop(context: BundleContext) {
    context.removeServiceListener(this)
    println("Stopped listening for service events.")

  }

  def serviceChanged(event: ServiceEvent) {
    val objectClass  =    event.getServiceReference().getProperty("objectClass")

    if (event.getType() == ServiceEvent.REGISTERED)
    {
      System.out.println(
        "Ex1: Service of type " + objectClass+ " registered phiu phiu.")
    }
    else if (event.getType() == ServiceEvent.UNREGISTERING)
    {
      System.out.println(
        "Ex1: Service of type " + objectClass+ " unregistered.")
    }
    else if (event.getType() == ServiceEvent.MODIFIED)
    {
      System.out.println(
        "Ex1: Service of type " + objectClass + " modified.")
    }
  }
}
