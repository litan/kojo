/*
 * Copyright (C) 2022 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.music

object Instrument {

  // constants from jmusic (https://explodingart.com/jmusic/index.html)

  val PIANO = 0

  val ACOUSTIC_GRAND = 0

  val BRIGHT_ACOUSTIC = 1

  val ELECTRIC_GRAND = 2

  val HONKYTONK = 3

  val HONKYTONK_PIANO = 3

  val EPIANO = 4

  val ELECTRIC_PIANO = 4

  val ELPIANO = 4

  val RHODES = 4

  val EPIANO2 = 5

  val DX_EPIANO = 5

  val HARPSICHORD = 6

  val CLAV = 7

  val CLAVINET = 7

  val CELESTE = 8

  val CELESTA = 8

  val GLOCKENSPIEL = 9

  val GLOCK = 9

  val MUSIC_BOX = 10

  val VIBRAPHONE = 11

  val VIBES = 11

  val MARIMBA = 12

  val XYLOPHONE = 13

  val TUBULAR_BELL = 14

  val TUBULAR_BELLS = 14

  val ORGAN = 16

  val ELECTRIC_ORGAN = 16

  val ORGAN2 = 17

  val JAZZ_ORGAN = 17

  val ORGAN3 = 18

  val HAMMOND_ORGAN = 17

  val CHURCH_ORGAN = 19

  val PIPE_ORGAN = 19

  val REED_ORGAN = 20

  val ACCORDION = 21

  val PIANO_ACCORDION = 21

  val CONCERTINA = 21

  val HARMONICA = 22

  val BANDNEON = 23

  val NYLON_GUITAR = 24

  val NGUITAR = 24

  val GUITAR = 24

  val ACOUSTIC_GUITAR = 24

  val AC_GUITAR = 24

  val STEEL_GUITAR = 25

  val SGUITAR = 25

  val JAZZ_GUITAR = 26

  val JGUITAR = 26

  val CLEAN_GUITAR = 27

  val CGUITAR = 27

  val ELECTRIC_GUITAR = 27

  val EL_GUITAR = 27

  val MUTED_GUITAR = 28

  val MGUITAR = 28

  val OVERDRIVE_GUITAR = 29

  val OGUITAR = 29

  val DISTORTED_GUITAR = 30

  val DGUITAR = 30

  val DIST_GUITAR = 30

  val GUITAR_HARMONICS = 31

  val GT_HARMONICS = 31

  val HARMONICS = 31

  val ACOUSTIC_BASS = 32

  val ABASS = 32

  val FINGERED_BASS = 33

  val BASS = 33

  val FBASS = 33

  val ELECTRIC_BASS = 33

  val EL_BASS = 33

  val EBASS = 33

  val PICKED_BASS = 34

  val PBASS = 34

  val FRETLESS_BASS = 35

  val FRETLESS = 35

  val SLAP_BASS = 36

  val SBASS = 36

  val SLAP = 36

  val SLAP_BASS_1 = 36

  val SLAP_BASS_2 = 37

  val SYNTH_BASS = 38

  val SYNTH_BASS_1 = 38

  val SYNTH_BASS_2 = 39

  val VIOLIN = 40

  val VIOLA = 41

  val CELLO = 42

  val VIOLIN_CELLO = 42

  val CONTRABASS = 43

  val CONTRA_BASS = 43

  val DOUBLE_BASS = 43

  val TREMOLO_STRINGS = 44

  val TREMOLO = 44

  val PIZZICATO_STRINGS = 45

  val PIZZ = 45

  val PITZ = 45

  val PSTRINGS = 45

  val HARP = 46

  val TIMPANI = 47

  val TIMP = 47

  val STRINGS = 48

  val STR = 48

  val STRING_ENSEMBLE_1 = 48

  val STRING_ENSEMBLE_2 = 49

  val SYNTH_STRINGS = 50

  val SYNTH_STRINGS_1 = 50

  val SLOW_STRINGS = 51

  val SYNTH_STRINGS_2 = 51

  val AAH = 52

  val AHHS = 52

  val CHOIR = 52

  val OOH = 53

  val OOHS = 53

  val VOICE = 53

  val SYNVOX = 54

  val VOX = 54

  val ORCHESTRA_HIT = 55

  val TRUMPET = 56

  val TROMBONE = 57

  val TUBA = 58

  val MUTED_TRUMPET = 59

  val FRENCH_HORN = 60

  val HORN = 60

  val BRASS = 61

  val SYNTH_BRASS = 62

  val SYNTH_BRASS_1 = 62

  val SYNTH_BRASS_2 = 63

  val SOPRANO_SAX = 64

  val SOPRANO = 64

  val SOPRANO_SAXOPHONE = 64

  val SOP = 64

  val ALTO_SAX = 65

  val ALTO = 65

  val ALTO_SAXOPHONE = 65

  val TENOR_SAX = 66

  val TENOR = 66

  val TENOR_SAXOPHONE = 66

  val SAX = 66

  val SAXOPHONE = 66

  val BARITONE_SAX = 67

  val BARI = 67

  val BARI_SAX = 67

  val BARITONE = 67

  val BARITONE_SAXOPHONE = 67

  val OBOE = 68

  val ENGLISH_HORN = 69

  val BASSOON = 70

  val CLARINET = 71

  val CLAR = 71

  val PICCOLO = 72

  val PIC = 72

  val PICC = 72

  val FLUTE = 73

  val RECORDER = 74

  val PAN_FLUTE = 75

  val PANFLUTE = 75

  val BOTTLE_BLOW = 76

  val BOTTLE = 76

  val SHAKUHACHI = 77

  val WHISTLE = 78

  val OCARINA = 79

  val GMSQUARE_WAVE = 80

  val SQUARE = 80

  val GMSAW_WAVE = 81

  val SAW = 81

  val SAWTOOTH = 81

  val SYNTH_CALLIOPE = 82

  val CALLOPE = 82

  val SYN_CALLIOPE = 82

  val CHIFFER_LEAD = 83

  val CHIFFER = 83

  val CHARANG = 84

  val SOLO_VOX = 85

  val FANTASIA = 88

  val WARM_PAD = 89

  val PAD = 89

  val POLYSYNTH = 90

  val POLY_SYNTH = 90

  val SPACE_VOICE = 91

  val BOWED_GLASS = 92

  val METAL_PAD = 93

  val HALO_PAD = 94

  val HALO = 94

  val SWEEP_PAD = 95

  val SWEEP = 95

  val ICE_RAIN = 96

  val ICERAIN = 96

  val SOUNDTRACK = 97

  val CRYSTAL = 98

  val ATMOSPHERE = 99

  val BRIGHTNESS = 100

  val GOBLIN = 101

  val ECHO_DROPS = 102

  val DROPS = 102

  val ECHOS = 102

  val ECHO = 102

  val ECHO_DROP = 102

  val STAR_THEME = 103

  val SITAR = 104

  val BANJO = 105

  val SHAMISEN = 106

  val KOTO = 107

  val KALIMBA = 108

  val THUMB_PIANO = 108

  val BAGPIPES = 109

  val BAG_PIPES = 109

  val BAGPIPE = 109

  val PIPES = 109

  val FIDDLE = 110

  val SHANNAI = 111

  val TINKLE_BELL = 112

  val BELL = 112

  val BELLS = 112

  val AGOGO = 113

  val STEEL_DRUMS = 114

  val STEELDRUMS = 114

  val STEELDRUM = 114

  val STEEL_DRUM = 114

  val WOODBLOCK = 115

  val WOODBLOCKS = 115

  val TAIKO = 116

  val DRUM = 116

  val TOM = 119

  val TOMS = 119

  val TOM_TOM = 119

  val TOM_TOMS = 119

  val SYNTH_DRUM = 118

  val SYNTH_DRUMS = 118

  val REVERSE_CYMBAL = 119

  val CYMBAL = 119

  val FRETNOISE = 120

  val FRET_NOISE = 120

  val FRET = 120

  val FRETS = 120

  val BREATHNOISE = 121

  val BREATH = 121

  val SEASHORE = 122

  val SEA = 122

  val RAIN = 122

  val THUNDER = 122

  val WIND = 122

  val STREAM = 122

  val SFX = 122

  val SOUNDEFFECTS = 122

  val SOUNDFX = 122

  val BIRD = 123

  val TELEPHONE = 124

  val PHONE = 124

  val HELICOPTER = 125

  val APPLAUSE = 126

  val GUNSHOT = 127
}
