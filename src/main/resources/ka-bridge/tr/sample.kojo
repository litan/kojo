// #include ~/kojo-includes/ka-bridge.kojo
// aşağıda kullandığımız pinMode, digitalRead/Write ve delay komutlarını yükledik ilk satırda

def setup() {  // kurulum için gerekli komutları buraya koyarız. Arduinomuz bununla başlar.
    pinMode(2, INPUT) // bir girdi
    pinMode(3, OUTPUT) // üç çıktımız var
    pinMode(4, OUTPUT)
    pinMode(5, OUTPUT)
}

def loop() { // Bir döngü tanımlarız. Arduino böylece çalışır durur bizim için.
    val dügmeninEvresi = digitalRead(2)  // ikinciden oku
    if (dügmeninEvresi == LOW) {
        digitalWrite(3, HIGH)  // üçüncüyü yükselt
        digitalWrite(4, LOW)  // dördüncu ve beşinciyi düşür
        digitalWrite(5, LOW)
    }
    else {
        digitalWrite(3, LOW)
        digitalWrite(4, LOW)
        digitalWrite(5, HIGH)
        delay(250)  // çeyrek saniye bekle
        digitalWrite(4, HIGH) // dördüncü ve beşinciyi tersine çevir
        digitalWrite(5, LOW)
        delay(250)
    }
}

