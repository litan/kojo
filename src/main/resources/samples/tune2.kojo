val score = MusicScore(
    Melody("Acoustic_Grand", "C6q D#6q F6q G6q D#6q F6h Rq D#6q F6q G6q F6q C6q D#6h Rq"),
    Melody("Pan_Flute", "X[Volume]=15000 Ri C6q D#6q F6q G6q D#6q F6h Ri Ri D#6q F6q G6q F6q C6q D#6h Ri"),
    Melody("Guitar", "X[Volume]=12000 Rw D#4majw Rw C4minw"),
    Rhythm("Acoustic_Bass_Drum", "q", "o.o.o.o.o.o.o.o."),
    Rhythm("Hand_Clap", "q", ".^.^.^.^.^.^.^.^")
)

playMusic(score)
