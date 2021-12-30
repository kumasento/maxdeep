package squeezenet1_1_s4;

import com.custom_computing_ic.maxdeep.kernel.conv2d.ConvLayerParameters;
import com.custom_computing_ic.maxdeep.kernel.conv2d.ConvLayerParameters.CompSeq;
import com.custom_computing_ic.maxdeep.kernel.conv2d.ConvLayerParameters.Output;
import com.custom_computing_ic.maxdeep.kernel.conv2d.ConvLayerParameters.OutputType;
import com.custom_computing_ic.maxdeep.kernel.conv2d.ConvLayerParameters.Pooling;
import com.custom_computing_ic.maxdeep.kernel.conv2d.ConvLayerParameters.Type;
import com.custom_computing_ic.maxdeep.manager.ConvLayerEngineParameters;
import com.custom_computing_ic.maxdeep.manager.ConvLayerManagerUtils;
import com.custom_computing_ic.maxdeep.manager.ManagerInterface;
import com.custom_computing_ic.maxdeep.manager.ManagerUtils;
import com.custom_computing_ic.maxdeep.manager.Max5LMemManager;
import com.maxeler.maxcompiler.v2.managers.custom.CustomManager;
import com.maxeler.maxcompiler.v2.managers.engine_interfaces.CPUTypes;
import com.maxeler.maxcompiler.v2.managers.engine_interfaces.EngineInterface;
import com.maxeler.maxcompiler.v2.managers.engine_interfaces.InterfaceParam;
import com.maxeler.platform.max5.manager.BuildConfig;
import com.maxeler.platform.max5.manager.BuildConfig.Effort;
import com.maxeler.platform.max5.manager.BuildConfig.OptimizationGoal;
import com.maxeler.platform.max5.manager.ImplementationStrategy;
import java.util.ArrayList;
import java.util.List;

public class Squeezenet11S4Manager extends Max5LMemManager implements ManagerInterface {
  public Squeezenet11S4Manager(ConvLayerEngineParameters params, List<ConvLayerParameters> cps) {
    super(params);

    getCurrentKernelConfig().debug.setEnableLatencyAnnotation(true);
    this.setAllowNonMultipleTransitions(true);
    this.setDefaultStreamClockFrequency(params.getFreq());

    ConvLayerManagerUtils.createKernelBlocks(this, cps, /* numCoeffFifoSplits= */ 1, true);
    ConvLayerManagerUtils.setupConstants(this, cps, params);
  }

  public EngineInterface interfaceDefault(
      List<ConvLayerParameters> cps, ConvLayerEngineParameters ep) {
    EngineInterface ei = new EngineInterface();

    InterfaceParam batchSize = ei.addParam("batch_size", CPUTypes.UINT64);

    ConvLayerManagerUtils.setupStreams(ei, cps, batchSize, true, this);
    ManagerUtils.ignoreLMemStreams(ei);

    return ei;
  }

  @SuppressWarnings("deprecation")
  public static void main(String[] args) {
    Squeezenet11S4EngineParameters params = new Squeezenet11S4EngineParameters(args);

    List<ConvLayerParameters> cps = new ArrayList<ConvLayerParameters>();

    cps.add(new ConvLayerParameters.Builder(111, 111, 3, 64, 3)
                .input("")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv0fwd")
                .pad(0)
                .stride(2)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(1)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 64, 64, 3)
                .input("squeezenet0conv0fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.POOLING)
                .name("squeezenet0pool0fwd")
                .pad(0)
                .stride(2)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 64, 16, 1)
                .input("squeezenet0pool0fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv1fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 16, 64, 1)
                .input("squeezenet0conv1fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv2fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 16, 64, 3)
                .input("squeezenet0conv1fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv3fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 64, 128, 1)
                .input("squeezenet0conv2fwd")
                .input("squeezenet0conv3fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat0")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 128, 16, 1)
                .input("squeezenet0concat0")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv4fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 16, 64, 1)
                .input("squeezenet0conv4fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv5fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 16, 64, 3)
                .input("squeezenet0conv4fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv6fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(55, 55, 64, 128, 1)
                .input("squeezenet0conv5fwd")
                .input("squeezenet0conv6fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat1")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 128, 128, 3)
                .input("squeezenet0concat1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.POOLING)
                .name("squeezenet0pool1fwd")
                .pad(0)
                .stride(2)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 128, 32, 1)
                .input("squeezenet0pool1fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv7fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 32, 128, 1)
                .input("squeezenet0conv7fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv8fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 32, 128, 3)
                .input("squeezenet0conv7fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv9fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 128, 256, 1)
                .input("squeezenet0conv8fwd")
                .input("squeezenet0conv9fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat2")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 256, 32, 1)
                .input("squeezenet0concat2")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv10fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 32, 128, 1)
                .input("squeezenet0conv10fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv11fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 32, 128, 3)
                .input("squeezenet0conv10fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv12fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(27, 27, 128, 256, 1)
                .input("squeezenet0conv11fwd")
                .input("squeezenet0conv12fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat3")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 256, 256, 3)
                .input("squeezenet0concat3")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.POOLING)
                .name("squeezenet0pool2fwd")
                .pad(0)
                .stride(2)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 256, 48, 1)
                .input("squeezenet0pool2fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv13fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 48, 192, 1)
                .input("squeezenet0conv13fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv14fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 48, 192, 3)
                .input("squeezenet0conv13fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv15fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 192, 384, 1)
                .input("squeezenet0conv14fwd")
                .input("squeezenet0conv15fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat4")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 384, 48, 1)
                .input("squeezenet0concat4")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv16fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 48, 192, 1)
                .input("squeezenet0conv16fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv17fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 48, 192, 3)
                .input("squeezenet0conv16fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv18fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 192, 384, 1)
                .input("squeezenet0conv17fwd")
                .input("squeezenet0conv18fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat5")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 384, 64, 1)
                .input("squeezenet0concat5")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv19fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 64, 256, 1)
                .input("squeezenet0conv19fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv20fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 64, 256, 3)
                .input("squeezenet0conv19fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv21fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 256, 512, 1)
                .input("squeezenet0conv20fwd")
                .input("squeezenet0conv21fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat6")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 512, 64, 1)
                .input("squeezenet0concat6")
                .output(new Output(OutputType.OFMAP, 0))
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv22fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 64, 256, 1)
                .input("squeezenet0conv22fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv23fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 64, 256, 3)
                .input("squeezenet0conv22fwd_1")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv24fwd")
                .pad(1)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 256, 512, 1)
                .input("squeezenet0conv23fwd")
                .input("squeezenet0conv24fwd")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.CONCAT)
                .name("squeezenet0concat7")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[1])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(8)
                .PC(4)
                .PC(4)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    cps.add(new ConvLayerParameters.Builder(13, 13, 512, 1000, 1)
                .input("squeezenet0concat7")
                .output(new Output(OutputType.OFMAP, 0))
                .BW(16)
                .WBW(16)
                .numFracBits(8)
                .type(Type.STANDARD)
                .name("squeezenet0conv25fwd")
                .pad(0)
                .stride(1)
                .seq(CompSeq.values()[0])
                .dbg(params.getDebug())
                .coeffOnChip(true)
                .coeffFile(params.getCoeffFile())
                .residual("")
                .PF(4)
                .PC(8)
                .PK(1)
                .namedRegion("")
                .pooling(Pooling.MAX)
                .dspFactor(0.5)
                .build());

    Squeezenet11S4Manager mgr = new Squeezenet11S4Manager(params, cps);
    mgr.createSLiCinterface(mgr.interfaceDefault(cps, params));
    mgr.createSLiCinterface(ManagerUtils.dramRead(mgr, mgr.iface));
    mgr.createSLiCinterface(ManagerUtils.dramWrite(mgr, mgr.iface));

    BuildConfig buildConfig = mgr.getBuildConfig();
    buildConfig.setBuildEffort(Effort.VERY_HIGH);
    buildConfig.setOptimizationGoal(BuildConfig.OptimizationGoal.BALANCED);
    buildConfig.addImplementationStrategy(ImplementationStrategy.MAXELER1);
    buildConfig.addImplementationStrategy(ImplementationStrategy.MAXELER2);
    buildConfig.addImplementationStrategy(ImplementationStrategy.MAXELER3);
    buildConfig.addImplementationStrategy(ImplementationStrategy.MAXELER4);
    buildConfig.addImplementationStrategy(ImplementationStrategy.PERFORMANCE_EARLY_BLOCK_PLACEMENT);
    buildConfig.addImplementationStrategy(ImplementationStrategy.PERFORMANCE_EXPLORE);
    buildConfig.addImplementationStrategy(ImplementationStrategy.PERFORMANCE_EXTRA_TIMING_OPT);
    buildConfig.addImplementationStrategy(ImplementationStrategy.PERFORMANCE_NET_DELAY_HIGH);
    buildConfig.addImplementationStrategy(ImplementationStrategy.PERFORMANCE_REFINE_PLACEMENT);
    buildConfig.setParallelism(4);

    mgr.build();
  }
}
