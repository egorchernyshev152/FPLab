package com.example

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import scala.math._

// Актор для вычисления значения интеграла на заданном отрезке
object CalculationActor:
    case class Task(segment: IntegralSegment, replyTo: ActorRef[Double])

    def apply(): Behavior[Task] = Behaviors.receive { (context, task) =>
        task match
            case Task(IntegralSegment(f, a, b, precision), replyTo) =>
                val steps = precision * 2
                val stepSize = (b - a) / steps

                val integralValue = f(a) + f(b) + 
                    (1 until steps).map(k => f(a + k * stepSize)).sum * 2
                
                val result = integralValue * (stepSize / 2)

                replyTo ! result
        Behaviors.same
    }

// Актор для суммирования результатов
object AggregatorActor:
    def apply(totalTasks: Int, finalReplyTo: ActorRef[Double]): Behavior[Double] = Behaviors.setup { context =>
        def accumulate(currentSum: Double, remaining: Int): Behavior[Double] =
            Behaviors.receiveMessage { partialResult =>
                val newSum = currentSum + partialResult
                if (remaining > 1) then
                    accumulate(newSum, remaining - 1)
                else
                    finalReplyTo ! newSum
                    Behaviors.stopped
            }

        accumulate(0.0, totalTasks)
    }

// Логгер для вывода результатов
object ResultLogger:
    def apply(): Behavior[Double] = Behaviors.receive { (context, result) =>
        context.log.info(s"Результат: $result")
        Behaviors.same
    }

// Система управления задачами интегрирования
object IntegrationManager:
    case class Compute(integral: IntegralSegment, tasks: Int, replyTo: ActorRef[Double])

    def apply(): Behavior[Compute] = Behaviors.setup { context =>
        val workers = Vector(
            context.spawn(CalculationActor(), "worker-1"),
            context.spawn(CalculationActor(), "worker-2"),
            context.spawn(CalculationActor(), "worker-3"),
            context.spawn(CalculationActor(), "worker-4")
        )

        Behaviors.receiveMessage { case Compute(integral, tasks, replyTo) =>
            val aggregator = context.spawn(AggregatorActor(tasks, replyTo), s"aggregator-${System.nanoTime()}")
            val stepSize = (integral.end - integral.start) / tasks

            for (i <- 0 until tasks) do
                val subSegment = IntegralSegment(
                    integral.f,
                    integral.start + i * stepSize,
                    integral.start + (i + 1) * stepSize,
                    integral.precision
                )
                workers(i % workers.size) ! CalculationActor.Task(subSegment, aggregator)

            Behaviors.same
        }
    }

// Кейс-класс для представления отрезка интегрирования
case class IntegralSegment(f: Double => Double, start: Double, end: Double, precision: Int)

@main def runIntegration(): Unit = {
    val managerSystem = ActorSystem(IntegrationManager(), "IntegrationManager")
    val loggerSystem = ActorSystem(ResultLogger(), "ResultLogger")

    // Новый интеграл для функции cos(x) на отрезке от 0 до Pi
    managerSystem ! IntegrationManager.Compute(IntegralSegment(x => cos(x), 0, Pi, 100), 100, loggerSystem)

    // Новый интеграл для функции x^2 на отрезке от 0 до 1
    managerSystem ! IntegrationManager.Compute(IntegralSegment(x => x * x, 0, 1, 100), 100, loggerSystem)
}