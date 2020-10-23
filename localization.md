Kojo can be localized at three levels.

* Level 1 - localization of the UI
* Level 2 - localization of the basic turtle and looping commands (so that children can program in the target language).
* Level 3 - localization of the existing turtle graphics samples.

The following are the steps that need to be followed to do the localization:

The first step is to fork and then clone the kojo repo. You can then make the required localization enhancements in your repo and send pull requests.

In the discussion below, let's assume that the language code for the target language is `xx`.

### Level 1
Create `Bundle_xx.properties` by tanslating the following file:  
https://github.com/litan/kojo/blob/master/src/main/resources/net/kogics/kojo/lite/Bundle.properties  

Add your language code to the list here:  
https://github.com/litan/kojo/blob/master/src/main/scala/net/kogics/kojo/lite/LangMenuFactory.scala#L31

Add your language (localized) name to the map here:  
https://github.com/litan/kojo/blob/master/src/main/scala/net/kogics/kojo/lite/LangMenuFactory.scala#L75

Then send a pull request.

### Level 2
Create `level2_xx.properties` by translating the following file:  
https://github.com/litan/kojo/blob/master/l10n-level2/level2.properties

Then send a pull request.

FYI, with the help of `level2_xx.properties`, the following files will be modified by a Kojo core-developer to wire in the level-2 changes:
* xxInit.scala (generated from the above properties file).
* xx.tw.scala
* LangInit.scala

Here's an example checkin for a wiring-in:  
https://github.com/litan/kojo/commit/852c18a6124fe773063f846db8fda9b7b705ab4c

### Level 3
This is best explained with an example:

if you want to localize the following sample for Swedish:  
`src/main/resources/samples/spiral.kojo`  
Then you just need to just create the following localized version of the sample:  
`src/main/resources/samples/sv/spiral.kojo`  
The version of spiral.kojo under the sv directory will get picked up when Kojo is running in Swedish mode.

