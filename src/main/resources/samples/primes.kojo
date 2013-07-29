// #worksheet (that makes this script run as a worksheet)

switchToWorksheetPerspective() 
// Click on 'Window -> Default Perspective' whenever you are done playing with 
// this worksheet - to get back to the default Kojo window configuration. 
 
// This sample shows you different ways of generating prime numbers

// (1) Find primes using the Sieve of Eratosthenes    
// (1a) List based
def sieve(nums: List[Int]): List[Int] = nums match {
    case Nil     => Nil
    case n :: ns => n :: sieve(ns.filter { e => e % n != 0 })
}
sieve((2 to 50).toList)

// (1b) Convert List based solution to Stream based solution
def sieve2(nums: Stream[Int]): Stream[Int] = nums match {
    case n #:: ns => n #:: sieve2(ns.filter { e => e % n != 0 })
}
sieve2(Stream.from(2)).take(15).toList

// (1c) A pure Stream based solution
def sieve3(nums: Stream[Int]): Stream[Int] = {
    nums.head #:: sieve3(nums.tail.filter { e => e % nums.head != 0 })
}
sieve3(Stream.from(2)).take(15).toList

// (2) A more efficient Stream based solution
val primes: Stream[Int] = 2 #:: Stream.from(3, 2).filter { isPrime }
def isPrime(n: Int) = primes.takeWhile { j => j * j <= n }.forall { p => n % p != 0 }
primes.take(15).toList

