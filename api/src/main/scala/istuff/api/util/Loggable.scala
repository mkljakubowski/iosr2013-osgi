package istuff.api.util

import org.osgi.framework.BundleContext
import com.weiglewilczek.scalamodules._
import org.osgi.service.log.LogService
import com.weiglewilczek.slf4s.Logger

trait Loggable {
  val logger = Logger(this.getClass.getName)
}

//class Logger (className: String) {
//
//  def info (message: AnyRef)(implicit context: BundleContext) = {
//    context findService withInterface[LogService] andApplyUnget { _.log(LogService.LOG_INFO, message.toString) } match {
//      case None => println("log error")
//      case _ => println("log ok")
//    }
//  }
//
//  def warn (message: AnyRef)(implicit context: BundleContext) = {
//    context findService withInterface[LogService] andApply { _.log(LogService.LOG_WARNING, message.toString) }
//  }
//
//  def debug (message: AnyRef)(implicit context: BundleContext) = {
//    context findService withInterface[LogService] andApply { _.log(LogService.LOG_DEBUG, message.toString) }
//  }
//
//  def error (message: AnyRef)(implicit context: BundleContext) = {
//    context findService withInterface[LogService] andApply { _.log(LogService.LOG_ERROR, message.toString) }
//  }
//
//}