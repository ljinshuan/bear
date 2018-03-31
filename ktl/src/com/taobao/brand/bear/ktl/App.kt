package com.taobao.brand.bear.ktl

/**
 * @author jinshuan.li 31/03/2018 08:10
 */
class App


fun main(args: Array<String>) {


    val fruits = listOf("banana", "avocado", "apple", "kiwifruit")

    fruits.filter { it.startsWith("a") }.sortedBy { it }.map { it.toUpperCase() }.forEach { println(it) }


    val customer = Customer("ljinshuan")
}


fun sum(a: Int, b: Int) = a + b


fun printSum(a: Int, b: Int): Int {

    println("sum of $a and $b is ${a + b}")

    return a + b
}


fun parseInt(x: String): Int? {

    return x.toIntOrNull()
}


data class Customer(val name: String)
