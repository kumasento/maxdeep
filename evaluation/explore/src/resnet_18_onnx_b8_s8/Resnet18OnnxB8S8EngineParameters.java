package resnet_18_onnx_b8_s8;

import com.custom_computing_ic.maxdeep.manager.ConvLayerEngineParameters;

public class Resnet18OnnxB8S8EngineParameters extends ConvLayerEngineParameters {

  public Resnet18OnnxB8S8EngineParameters(String[] args) {
    super(args);
  }
  
  @Override
  public String getBuildName() {
    return String.format("%s_%s_%s_FREQ_%d%s", getMaxFileName(), getDFEModel(), getTarget(), getFreq(), getDebug() ? "_DBG" : ""); 
  }
}
