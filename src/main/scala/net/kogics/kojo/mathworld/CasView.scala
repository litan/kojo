package net.kogics.kojo.mathworld

import net.kogics.kojo.util.Utils
import geogebra.common.kernel.geos.GeoCasCell
import geogebra.common.euclidian.EuclidianConstants
import java.util.concurrent.Future
import net.kogics.kojo.util.FutureResult
import geogebra.common.kernel.StringTemplate
import geogebra.common.kernel.arithmetic.ValidExpression
import geogebra.common.kernel.ModeSetter

class CasView(_Mw: MathWorld) {
  import _Mw._

  val cview = _guim.getCasView()
  val cast = cview.getConsoleTable()

  def clear() = Utils.runInSwingThread {

    //    for (i <- 0 to cast.getRowCount - 1) {
    //      _kernel.getConstruction.removeFromConstructionList(cast.getGeoCasCell(i))
    //    }

    cview.clearView()
  }

  def doer(input: String, mode: Int): Future[ValidExpression] = {
    val ret = new FutureResult[ValidExpression]
    Utils.runInSwingThread {
      val cell = new GeoCasCell(_kernel.getConstruction)
      cell.setInput(input)
      cast.insertRow(cell, true)

      Utils.runLaterInSwingThread {
        cview.setMode(mode, ModeSetter.TOOLBAR)
        try {
          ret.set(cell.getOutputValidExpression)
        }
        catch {
          case t: Exception =>
            ret.setException(t)
        }
        _kojoCtx.activateScriptEditor()
      }
    }
    ret
  }

  def keep(input: String) = doer(input, EuclidianConstants.MODE_CAS_KEEP_INPUT)
  def evaluate(input: String) = doer(input, EuclidianConstants.MODE_CAS_EVALUATE)
}