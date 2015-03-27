// Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
// The contents of this file are subject to 
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

val pageStyle = "background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:dark-gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

def pgHeader(hdr: String) =
    <p style={ headerStyle }>
        { hdr }
        <hr/>
        <br/> 
    </p>

val scripts = Vector(
    net.kogics.kojo.util.Utils.loadResource("/ka-bridge/ka_bridge/ka_bridge.ino"),
    net.kogics.kojo.util.Utils.loadResource("/ka-bridge/ka-bridge.kojo"),
    net.kogics.kojo.util.Utils.loadResource("/ka-bridge/sample.kojo")
)

def runLink(n: Int) = "http://runhandler/script/" + n
def code(n: Int) = {
    <div style="background-color:CCFFFF;margin-top:10px"> 
        <hr/>
        <pre><code><a href={ runLink(n) } style="text-decoration: none;font-size:x-small;">
{ scripts(n) }</a></code></pre>
        <hr/>
    </div>
}

var pages = Vector.empty[StoryPage]
var pg: StoryPage = _

pages :+= Page(
    name = "intro",
    body =
        <body style={ pageStyle }>
            { pgHeader("Setting up Kojo for Arduino Programming") }
            To program an Arduino board with Kojo, you need to set up a bridge between 
            Kojo (running on your computer) and the Arduino board. This bridge has two
            components:
            <ul>
                <li>A program called <tt>ka_bridge.ino</tt> that runs on the Arduino board.</li>
                <li>A file called <tt>ka-bridge.kojo</tt> that you can include within your Kojo programs.</li>
            </ul>
            These two components talk to each other to do whatever Arduino specific things you 
            want to do in your Kojo based Arduino programs.<br/><br/> 
            
            To set up the Kojo-Arduino bridge, you need to do the following:
            <ol>
                <li><a href="http://localpage/bridge-arduino">Upload <tt>ka_bridge.ino</tt></a> to your Arduino board.</li>
                <li><a href="http://localpage/bridge-kojo">Save <tt>ka-bridge.kojo</tt></a> in a directory on your machine, 
                so that you can include it within your Kojo based Arduino programs.</li>
            </ol>

            Once the Kojo-Arduino bridge is set up, you can start writing <a href="http://localpage/bridge-sample">Kojo based Arduino programs</a>.
        </body>,
    code = {}
)

pages :+= Page(
    name = "bridge-arduino",
    body =
        <body style={ pageStyle }>
            { pgHeader("Uploading ka_bridge.ino to your Arduino board") }
            To upload <tt>ka_bridge.ino</tt> to your Arduino board, do the following:
            <ol>
                <li>Fire up the <a href="http://arduino.cc/en/Guide/Environment">Arduino Programming Environment</a> (<a href="http://arduino.cc/en/Main/OldSoftwareReleases">version 1.0.5</a> is recommended), and make sure the Arduino board is connected to your computer.</li>
                <li>Click on the code below to copy it into the script editor.</li>
                <li>Select all the code in the script editor (<em>Ctrl+A</em>), copy it (<em>Ctrl+C</em>), and then paste it into the Arduino IDE (<em>Ctrl+V</em>).</li>
                <li>Click the Upload button in the Arduino IDE -- to get the Kojo bridge code running on your Arduino board.</li>
            </ol>
            <br/> 
            <tt>ka_bridge.ino</tt>:
            { code(0) }
        </body>,
    code = {}
)

pages :+= Page(
    name = "bridge-kojo",
    body =
        <body style={ pageStyle }>
            { pgHeader("Saving ka-bridge.kojo") }
            To Save <tt>ka-bridge.kojo</tt> on your machine, so that it is available to your programs, do the following:
            <ol>
                <li>Click on the code below to copy it into the script editor.</li>
                <li>Click on <em>'File -> Save As'</em> to save the code in the script editor into a file.</li>
                <li>Navigate to your home folder ({homeDir}), and create a new folder (if it does not already exist) under this folder called <tt>kojo-includes</tt>.</li>
                <li>Go inside the <tt>kojo-includes</tt> folder, enter ka-bridge.kojo in the <em>File Name</em> field, and click the <em>Save</em> button.</li>
            </ol>
            <br/> 
            <tt>ka-bridge.kojo</tt>:
            { code(1) }
        </body>,
    code = {}
)

pages :+= Page(
    name = "bridge-sample",
    body =
        <body style={ pageStyle }>
            { pgHeader("Using the Kojo Arduino bridge") }
            Once the Kojo-Arduino bridge is set up, you can use the bridge to start writing Kojo based Arduino programs.
            Here is an example:
            { code(2) }
        </body>,
    code = {}
)

val story = Story(pages: _*)
stClear()
stAddLinkHandler("script", story) { idx: Int =>
    stSetScript(scripts(idx))
}
stPlayStory(story)
