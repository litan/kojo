Kojo can be localized at three levels.

* Level 1 - localization of the UI
* Level 2 - localization of the basic turtle and looping commands (so that children can program in the target language).
* Level 3 - localization of the existing turtle graphics samples.

The following are the steps that need to be followed to do the localization:

The first step is to fork and then clone the kojo repo. You can then make the required localization enhancements in your repo and send pull requests.

### Level 1
Tanslate the following file:  
https://github.com/litan/kojo/blob/master/src/main/resources/net/kogics/kojo/lite/Bundle.properties

### Level 2
Tanslate the following file:  
https://github.com/litan/kojo/blob/master/l10n-level2/level2.properties

### Level 3
This is best explained with an example:

if you want to localize the following sample for Swedish:  
`src/main/resources/samples/spiral.kojo`  
Then you just need to just create the following localized version of the sample:  
`src/main/resources/samples/sv/spiral.kojo`  
The version of spiral.kojo under the sv directory will get picked up when Kojo is running in Swedish mode.

