package utils
import org.apache.hadoop.hbase.client.HTablePool
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration

object DataType extends Enumeration {
  type DataType = Value
  val dByteSeq, dByte, dInt, dLong, dFloat, dDouble, dString, dBoolean, dTime = Value
}

class HBridgeConfig(
  val configuration: Option[Configuration],
  val htablePool: Option[HTablePool])

object HBridgeConfig {
  private val POOL_SIZE: Int = 100
  private val SCANNER_CACHING: String = "30"
  private val LEASE_PERIOD: String = "900000"
  private val RPC_TIMEOUT: String = "900000"  

  private var htablePool: Option[HTablePool] = None

  def setConfig(hbaseZookeeperQuorum: String,
    hbaseZookeeperClientPort: String,
    hbaseWriteBufferSize: String,
    hbaseMaster: String, hbaseScannerCaching: String, hbaseRegLeasePeriod: String, hbaseRpcTimeout : String) = {
    val conf: Configuration = HBaseConfiguration.create()
    conf.clear()
    conf.set("hbase.zookeeper.quorum", hbaseZookeeperQuorum)
    conf.set("hbase.zookeeper.property.clientPort", hbaseZookeeperClientPort)
    conf.set("hbase.rootdir", hbaseMaster)
    conf.set("hbase.client.write.buffer", hbaseWriteBufferSize)
    conf.set("hbase.client.scanner.caching", hbaseScannerCaching)
    conf.set("hbase.regionserver.lease.period", hbaseRegLeasePeriod)
    conf.set("hbase.rpc.timeout", hbaseRpcTimeout)
    htablePool = Option(new HTablePool(conf, POOL_SIZE))
    new HBridgeConfig(
      Option(conf), htablePool)
  }

  def apply(hbaseZookeeperQuorum: String,
    hbaseZookeeperClientPort: String,
    hbaseWriteBufferSize: String,
    hbaseMaster: String, hbaseScannerCaching: String, hbaseRegLeasePeriod: String,hbaseRpcTimeout : String) = {
    setConfig(hbaseZookeeperQuorum, hbaseZookeeperClientPort, hbaseWriteBufferSize, hbaseMaster, hbaseScannerCaching, hbaseRegLeasePeriod,hbaseRpcTimeout)
  }

  def apply(hbaseZookeeperQuorum: String,
    hbaseZookeeperClientPort: String,
    hbaseWriteBufferSize: String,
    hbaseMaster: String) = {
    setConfig(hbaseZookeeperQuorum, hbaseZookeeperClientPort, hbaseWriteBufferSize, hbaseMaster, SCANNER_CACHING, LEASE_PERIOD,RPC_TIMEOUT)
  }
}