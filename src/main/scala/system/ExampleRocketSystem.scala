// See LICENSE.SiFive for license details.

package freechips.rocketchip.system

import Chisel._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.coreplex._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.util.DontTouch
import freechips.rocketchip.devices.debug.{HasPeripheryDebug, HasPeripheryDebugModuleImp}

/** Example Top with periphery devices and ports, and a Rocket coreplex */
class ExampleRocketSystem(implicit p: Parameters) extends RocketCoreplex
    with HasAsyncExtInterrupts
    with HasMasterAXI4MemPort
    with HasMasterAXI4MMIOPort
    with HasMasterAXI4MMIOPort2
    with HasSlaveAXI4Port
    with HasPeripheryBootROM
    with HasPeripheryDebug
    with HasSystemErrorSlave {
  override lazy val module = new ExampleRocketSystemModule(this)
}

class ExampleRocketSystemModule[+L <: ExampleRocketSystem](_outer: L) extends RocketCoreplexModule(_outer)
    with HasRTCModuleImp
    with HasExtInterruptsModuleImp
    with HasMasterAXI4MemPortModuleImp
    with HasMasterAXI4MMIOPortModuleImp
    with HasMasterAXI4MMIOPort2ModuleImp
    with HasSlaveAXI4PortModuleImp
    with HasPeripheryBootROMModuleImp
    with HasPeripheryDebugModuleImp
    with DontTouch
