// Daha çok örnek ve bilgi için şuraya bakabilirsin: http://www.jfugue.org/examples.html
dez müzik = MusicScore(
    Melody("ACOUSTIC_Grand", "C6q D#6q F6q G6q D#6q F6h Rq D#6q F6q G6q F6q C6q D#6h Rq"),
    Melody("Pan_Flute", "X[Volume]=15000 Ri C6q D#6q F6q G6q D#6q F6h Ri Ri D#6q F6q G6q F6q C6q D#6h Ri"),
    Melody("GUITAR", "X[Volume]=12000 Rw D#4majw Rw C4w"),
    Rhythm("ACOUSTIC_Bass_Drum", "q", "o.o.o.o.o.o.o.o."),
    Rhythm("Hand_Clap", "q", ".^.^.^.^.^.^.^.^")
)
// C4minw çalışmadı. C4w çalıştı
playMusic(müzik)
