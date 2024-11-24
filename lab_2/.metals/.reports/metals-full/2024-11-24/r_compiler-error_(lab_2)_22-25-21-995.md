file:///C:/FP/FPLab/lab_2/src/main/scala/Main.scala
### dotty.tools.dotc.MissingCoreLibraryException: Could not find package scala from compiler core libraries.
Make sure the compiler core libraries are on the classpath.
   

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/FP/FPLab/lab_2/src/main/scala/Main.scala
text:
```scala
import scala.compiletime.ops.boolean
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

//..1..
def integral(f: Double => Double, l: Double, r: Double, steps: Int): Double = {
  val stepSize = (r - l) / steps
  (0 until steps).map(i => f(l + i * stepSize)).reduce(_ + _) * stepSize
}

//..2_1..
def goodEnouhgPasswordOptions(password: String): Option[Boolean] = {
 if(password.isEmpty) None
 else Some(
   Seq(
    password.length >= 8,
    password.exists(_.isUpper),
    password.exists(_.isLower),
    password.exists(_.isDigit),
    password.exists("!@#$%^&*()-_=+[]{};:'\",.<>?/`~".contains)
  ).reduce(_&&_)
 )
}

//..2_2..
def goodEnoughPasswordTry(password: String): Either[Boolean, String] = {
 Try{
    Seq(
          (password.length >= 8, "Пароль должен содержать не менее 8 символов"),
          (password.exists(_.isUpper), "Пароль должен содержать хотя бы одну заглавную букву"),
          (password.exists(_.isLower), "Пароль должен содержать хотя бы одну строчную букву"),
          (password.exists(_.isDigit), "Пароль должен содержать хотя бы одну цифру"),
          (password.exists("!@#$%^&*()-_=+[]{};:'\",.<>?/`~".contains), "Пароль должен содержать хотя бы один специальный символ")
        ).collectFirst{
          case (false, errorMsg) => Right(errorMsg)
        }.getOrElse(Left(true))                       
  } match {
    case Success(result) => result
    case Failure(_) => Right("Ошибочка()")
  }
}

//..2_3..
def readPassword(): Future[String] = {
  // Чтение пароля (асинхронно)
  Future {
    printf("Введите ваш пароль: ")
    readLine()
  }.flatMap { password =>
    goodEnoughPasswordTry(password) match {
      case Left(_) => Future.successful(password) // Пароль удовлетворяет условиям
      case Right(error) =>
        println(s"Error: $error")
        readPassword() // Рекурсивно вызываем readPassword, если пароль недействителен
    }
  }
}

//..3..
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Monad[M[_]] extends Functor[M] {
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
}

@main def start() = {
//..1..
val result = integral(x => x*x, 0, 10, 1000) // интеграл от 0 до 10
println(s"Результат вычисления ингерала: $result") // Вывод результата
println()
//..2_1..
println(goodEnouhgPasswordOptions("Password1!")) // true
println(goodEnouhgPasswordOptions("pass"))        // false
println(goodEnouhgPasswordOptions("PASSWORD1!"))  // false
println(goodEnouhgPasswordOptions("Password"))    // false
println(goodEnouhgPasswordOptions("Password1"))   // false
println(goodEnouhgPasswordOptions(""))   // None
//..2_2..
println(goodEnoughPasswordTry("Password1!")) // true
println(goodEnoughPasswordTry("pass"))        
println(goodEnoughPasswordTry("PASSWORD1!"))  
println(goodEnoughPasswordTry("Password"))    
println(goodEnoughPasswordTry("Password1"))   
println(goodEnoughPasswordTry(""))   
println()
//..2_3..
val password = Await.result(readPassword(), Duration.Inf)
println("Пароль подходит!")
}


```



#### Error stacktrace:

```
dotty.tools.dotc.core.Denotations$.select$1(Denotations.scala:1320)
	dotty.tools.dotc.core.Denotations$.recurSimple$1(Denotations.scala:1348)
	dotty.tools.dotc.core.Denotations$.recur$1(Denotations.scala:1350)
	dotty.tools.dotc.core.Denotations$.staticRef(Denotations.scala:1354)
	dotty.tools.dotc.core.Symbols$.requiredPackage(Symbols.scala:939)
	dotty.tools.dotc.core.Definitions.ScalaPackageVal(Definitions.scala:215)
	dotty.tools.dotc.core.Definitions.ScalaPackageClass(Definitions.scala:218)
	dotty.tools.dotc.core.Definitions.AnyClass(Definitions.scala:280)
	dotty.tools.dotc.core.Definitions.syntheticScalaClasses(Definitions.scala:2131)
	dotty.tools.dotc.core.Definitions.syntheticCoreClasses(Definitions.scala:2145)
	dotty.tools.dotc.core.Definitions.init(Definitions.scala:2161)
	dotty.tools.dotc.core.Contexts$ContextBase.initialize(Contexts.scala:899)
	dotty.tools.dotc.core.Contexts$Context.initialize(Contexts.scala:523)
	dotty.tools.dotc.interactive.InteractiveDriver.<init>(InteractiveDriver.scala:41)
	dotty.tools.pc.MetalsDriver.<init>(MetalsDriver.scala:32)
	dotty.tools.pc.ScalaPresentationCompiler.newDriver(ScalaPresentationCompiler.scala:99)
```
#### Short summary: 

dotty.tools.dotc.MissingCoreLibraryException: Could not find package scala from compiler core libraries.
Make sure the compiler core libraries are on the classpath.
   