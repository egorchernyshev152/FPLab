package com.example

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.AddingServer.Addable

// Актор-сервер, выполняющий сложение
object AddingServer {
    
  // Определим тип суммируемого значения как Int или Double
  type Addable = Int | Double
  
  // Опишем сообщение, которое принимает актор: два суммируемых значения
  // и ссылка на актора, которому нужно будет вернуть результат
  case class AddMessage(a: Addable, b: Addable, replyTo: ActorRef[Addable])
  
  // Опишем поведение при приеме сообщения
  def apply(): Behavior[AddMessage] = Behaviors.receive { (context, message) =>

    // Выполняем сложение и отправляем результат обратно
    val result: Addable = (message.a, message.b) match {
      case (a: Int, b: Int) => a + b
      case (a: Double, b: Double) => a + b
      case (a: Int, b: Double) => a + b
      case (a: Double, b: Int) => a + b
    }
    
    // Отправляем результат обратно клиенту
    message.replyTo ! result
    
    // Поведение после обработки сообщения не меняется
    Behaviors.same
  }
}

object AddingClient {
  def apply(server: ActorRef[AddingServer.AddMessage]): Behavior[Addable] = 
    Behaviors.setup { context =>
      def generateAndSend(): Unit = {
        val a = scala.util.Random.nextInt(100)
        val b = scala.util.Random.nextInt(100)
        server ! AddingServer.AddMessage(a, b, context.self)
      }

      // Генерация и отправка сообщения на сервер
      generateAndSend()

      Behaviors.receiveMessage { result =>
        // Логируем результат сложения
        context.log.info(s"Received result: $result")
        // После получения результата снова генерируем и отправляем сообщение
        generateAndSend()
        Behaviors.same // Продолжаем без остановки
      }
    }
}

object AddingSystem {
  def apply(): Behavior[Unit] = Behaviors.setup { context =>
    // Создаем актор-сервер
    val server = context.spawn(AddingServer(), "addingServer")
    
    // Создаем актор-клиента
    context.spawn(AddingClient(server), "addingClient")
    
    // Поведение системы остается неизменным
    Behaviors.empty
  }
}

@main def AddingMain(): Unit = {
  // Создаем ActorSystem с поведением AddingSystem
  val system = ActorSystem(AddingSystem(), "addingSystem")
}
