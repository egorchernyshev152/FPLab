//..1..
@main def hello() =
  println("Hello, Egor!")

//..2..
def helloNTimes(n: Int) = {
  for (i <- 1 to n)
  if (i%2 == 0) println(s"hello $i") else println(s"hello ${n - i}") 
}
//..3_1..
def splittingIndexes(collection: Seq[Int]): (Seq[Int], Seq[Int]) = {
  val evenIndexed = collection.zipWithIndex.filter ((_, index) => index % 2 == 0 ).map(_._1)
  val oddIndexed = collection.zipWithIndex.filter ((_, index) => index % 2 != 0 ).map(_._1)
  (evenIndexed, oddIndexed)
}
//..3_2..
def findMax(collection: Seq[Int]): Int = {
  collection.reduce ((x, y) => if (x > y) x else y)
}
//..5..
def whatType(x: Any): String = x match {
  case i: Int => s"Целое число: $i"
  case s: String => s"Строка: $s"
  case d: Double => s"Дробное число: $d"
  case _ => "Непонятна: "
}
//..6..
def compose[A, B, C](f: B => C, g: A => B): A => C = {
  (x: A) => f(g(x))
}

// Пример вызова функции
@main def start() = {
  //..2..
helloNTimes(5)

println()

//..3_1..
val (evens, odds) = splittingIndexes(1 to 10)
println(s"Нечеткие индексы: $evens") 
println(s"Четкие индексы: $odds") 

println()

//..3_2..
val maxElement = findMax(Seq(4, 8, -2, 0, 11))
println(s"Максимальный элемент: $maxElement")

println()

//..4..
val newHelloNTimes: Int => Unit = helloNTimes
println(newHelloNTimes) // Вывелась ссылка на функцию
newHelloNTimes(5) // Функция по этой переменной вызвана

println()

//..5..
println()
println(whatType(5))
println(whatType(5.1))
println(whatType("Да, это строчка)"))
println(whatType(true))

println()

//..6..
val addOne: Int => Int = x => x + 1
val multiplyByTwo: Int => Int = x => x * 2
val composedFunction: Int => Int = compose(multiplyByTwo, addOne)
println(s"Композиция 2х функций: ${composedFunction(3)}") // Вывод: 8, так как (3 + 1) * 2 = 8
}