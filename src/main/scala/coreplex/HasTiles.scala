// See LICENSE.SiFive for license details.

package freechips.rocketchip.coreplex

import Chisel._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.interrupts._
import freechips.rocketchip.tile.{BaseTile, TileParams, SharedMemoryTLEdge, HasExternallyDrivenTileConstants}
import freechips.rocketchip.util._

class ClockedTileInputs(implicit val p: Parameters) extends ParameterizedBundle
    with HasExternallyDrivenTileConstants
    with Clocked

trait HasTiles extends HasSystemBus {
  val tiles: Seq[BaseTile]
  protected def tileParams: Seq[TileParams] = tiles.map(_.tileParams)
  def nTiles: Int = tileParams.size
  def hartIdList: Seq[Int] = tileParams.map(_.hartId)
  def localIntCounts: Seq[Int] = tileParams.map(_.core.nLocalInterrupts)
}

trait HasTilesBundle {
  val tile_inputs: Vec[ClockedTileInputs]
}

trait HasTilesModuleImp extends LazyModuleImp
    with HasTilesBundle
    with HasResetVectorWire {
  val outer: HasTiles

  def resetVectorBits: Int = {
    // Consider using the minimum over all widths, rather than enforcing homogeneity
    val vectors = outer.tiles.map(_.module.constants.reset_vector)
    require(vectors.tail.forall(_.getWidth == vectors.head.getWidth))
    vectors.head.getWidth
  }

  val tile_inputs = Wire(Vec(outer.nTiles, new ClockedTileInputs()(p.alterPartial {
    case SharedMemoryTLEdge => outer.sharedMemoryTLEdge
  })))

  // Unconditionally wire up the non-diplomatic tile inputs
  outer.tiles.map(_.module).zip(tile_inputs).foreach { case(tile, wire) =>
    tile.clock := wire.clock
    tile.reset := wire.reset
    tile.constants.hartid := wire.hartid
    tile.constants.reset_vector := wire.reset_vector
  }
}

