// #worksheet (that makes this script run as a worksheet)           

switchToWorksheetPerspective() 
// Click on 'Window -> Default Perspective' whenever you are done playing with 
// this worksheet - to get back to the default Kojo window configuration. 

// The infinite stream of primes from the 'Prime Numbers' sample 
val primes: Stream[Int] = 2 #:: Stream.from(3, 2).filter { isPrime }
def isPrime(n: Int) = primes.takeWhile { j => j * j <= n }.forall { p => n % p != 0 }

// Find all the prime factors of a number. 
// Look up the 'Fundamental theorem of arithmetic' on Wikipedia for more information on the subject. 
def primeFactors(n: Int) = primes.takeWhile(p => p <= n).filter { p => n % p == 0 }.toList
primeFactors(40)

// Find all the prime factors of a number, along with their multiplicity
def primeFactorsM(n: Int): List[Int] = {
    if (n == 1) Nil
    else {
        val pfactor = primes.dropWhile { p => n % p != 0 }.head // can you make this more efficient?
        pfactor :: primeFactorsM(n / pfactor)
    }
}
primeFactorsM(40)