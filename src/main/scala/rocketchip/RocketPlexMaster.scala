// See LICENSE.SiFive for license details.

package rocketchip

import Chisel._
import coreplex.RocketPlex
import diplomacy.LazyModule

trait RocketPlexMaster extends HasTopLevelNetworks {
  val module: RocketPlexMasterModule

  val coreplex = LazyModule(new RocketPlex)

  coreplex.l2in :=* fsb.node
  bsb.node :*= coreplex.l2out
  socBus.node := coreplex.mmio
  coreplex.mmioInt := intBus.intnode

  require (mem.size == coreplex.mem.size)
  (mem zip coreplex.mem) foreach { case (xbar, channel) => xbar.node :=* channel }
}

trait RocketPlexMasterBundle extends HasTopLevelNetworksBundle {
  val outer: RocketPlexMaster
}

trait RocketPlexMasterModule extends HasTopLevelNetworksModule {
  val outer: RocketPlexMaster
  val io: RocketPlexMasterBundle
  val clock: Clock
  val reset: Bool

  outer.coreplex.module.io.tcrs.foreach { case tcr =>
    tcr.clock := clock
    tcr.reset := reset
  }
}
