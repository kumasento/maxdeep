package resnet_50_a;

import com.custom_computing_ic.maxdeep.manager.ConvLayerEngineParameters;

public class Resnet50AEngineParameters extends ConvLayerEngineParameters {

  public Resnet50AEngineParameters(String[] args) {
    super(args);
  }
  
  @Override
  public String getBuildName() {
    return String.format("%s_%s_%s_FREQ_%d%s", getMaxFileName(), getDFEModel(), getTarget(), getFreq(), getDebug() ? "_DBG" : ""); 
  }
}
