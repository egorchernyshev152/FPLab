file:///C:/FP/FPLab/lab_2/src/main/scala/Main.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/toorr/AppData/Local/Coursier/cache/arc/https/github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%25252B11/OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11.zip/jdk-17.0.13+11/lib/src.zip!/java.base/java/lang/String.java

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1676
uri: file:///C:/FP/FPLab/lab_2/src/main/scala/Main.scala
text:
```scala
import scala.compiletime.ops.boolean
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.io.StdIn.readLine
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
//..3..
 def readPassword(): Future[String] = {
  Future{
    print("Введите пароль: ")
    readLine(
    ).m@@
  }
 }


@main def start() = {

//..1..
val result = integral(x => x*x, 0, 10, 1000) // интеграл от 0 до 10
println(s"Результат вычисления ингерала: $result") // Вывод результата
println()
//..2..
println(goodEnouhgPasswordOptions("Password1!")) // true
println(goodEnouhgPasswordOptions("pass"))        // false
println(goodEnouhgPasswordOptions("PASSWORD1!"))  // false
println(goodEnouhgPasswordOptions("Password"))    // false
println(goodEnouhgPasswordOptions("Password1"))   // false
println(goodEnouhgPasswordOptions(""))   // None



//..3..
println(goodEnoughPasswordTry("Password1!")) // true
println(goodEnoughPasswordTry("pass"))        
println(goodEnoughPasswordTry("PASSWORD1!"))  
println(goodEnoughPasswordTry("Password"))    
println(goodEnoughPasswordTry("Password1"))   
println(goodEnoughPasswordTry(""))   
}


```



#### Error stacktrace:

```
java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
	java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
	java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
	java.base/java.nio.file.Path.of(Path.java:147)
	java.base/java.nio.file.Paths.get(Paths.java:69)
	scala.meta.io.AbsolutePath$.apply(AbsolutePath.scala:58)
	scala.meta.internal.metals.MetalsSymbolSearch.$anonfun$definitionSourceToplevels$2(MetalsSymbolSearch.scala:70)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.metals.MetalsSymbolSearch.definitionSourceToplevels(MetalsSymbolSearch.scala:69)
	dotty.tools.pc.completions.CaseKeywordCompletion$.sortSubclasses(MatchCaseCompletions.scala:326)
	dotty.tools.pc.completions.CaseKeywordCompletion$.matchContribute(MatchCaseCompletions.scala:276)
	dotty.tools.pc.completions.Completions.advancedCompletions(Completions.scala:307)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:109)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:90)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:146)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/toorr/AppData/Local/Coursier/cache/arc/https/github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%25252B11/OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11.zip/jdk-17.0.13+11/lib/src.zip!/java.base/java/lang/String.java