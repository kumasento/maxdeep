package single_conv_k7;

import com.custom_computing_ic.maxdeep.manager.ConvLayerEngineParameters;

public class SingleConvK7EngineParameters extends ConvLayerEngineParameters {

  public SingleConvK7EngineParameters(String[] args) {
    super(args);
  }
  
  @Override
  public String getBuildName() {
    return String.format("%s_%s_%s_FREQ_%d%s", getMaxFileName(), getDFEModel(), getTarget(), getFreq(), getDebug() ? "_DBG" : ""); 
  }
}
