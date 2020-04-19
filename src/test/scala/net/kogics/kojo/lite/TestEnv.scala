package net.kogics.kojo
package lite

import net.kogics.kojo.story.StoryTeller
import javax.swing.JFrame
import net.kogics.kojo.music.KMp3
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.music.FuguePlayer

object TestEnv {
  def apply(kojoCtx: core.KojoCtx) = new {
    val spriteCanvas = new SpriteCanvas(kojoCtx)
    val Tw = new TurtleWorldAPI(spriteCanvas.turtle0)
    val TSCanvas = new DrawingCanvasAPI(spriteCanvas)
    val Staging = new staging.API(spriteCanvas)

    val storyTeller = new StoryTeller(kojoCtx)

    val mp3player = new KMp3(kojoCtx)
    val fuguePlayer = new FuguePlayer(kojoCtx)

    var scriptEditor: ScriptEditor = null
    val execSupport = new CodeExecutionSupport(
      TSCanvas,
      Tw,
      Staging,
      storyTeller,
      mp3player,
      fuguePlayer,
      spriteCanvas,
      scriptEditor, // lazy
      kojoCtx
    )
    scriptEditor = new ScriptEditor(execSupport, new JFrame)
    execSupport.initPhase2(scriptEditor)
  }
}