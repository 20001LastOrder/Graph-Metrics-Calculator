package app;

import graph.EMFGraph;
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.impl.YakindummPackageImpl;
import input.GraphReader;
import java.util.ArrayList;
import org.eclipse.xtext.xbase.lib.InputOutput;
import output.CsvFileWriter;
import socialnetwork.impl.SocialnetworkPackageImpl;

@SuppressWarnings("all")
public class Main {
  public static void main(final String[] args) {
    GraphReader.init();
    SocialnetworkPackageImpl.eINSTANCE.eClass();
    YakindummPackageImpl.eINSTANCE.eClass();
    InputOutput.<String>println("Start Reading Models...");
    for (int i = 1; (i <= 10); i++) {
      {
        final ArrayList<EMFGraph> models = new ArrayList<EMFGraph>();
        models.addAll(GraphReader.readModels((("viatraInput/" + "run") + Integer.valueOf(i))));
        InputOutput.<String>println("Reading Models Ended");
        for (final EMFGraph model : models) {
          String _name = model.getName();
          String _plus = ("viatraOutput/" + _name);
          String _plus_1 = (_plus + "_run_");
          String _plus_2 = (_plus_1 + Integer.valueOf(i));
          String _plus_3 = (_plus_2 + ".csv");
          Main.calculateAndOutputMetrics(model, YakindummPackageImpl.eNAME, _plus_3);
        }
      }
    }
    InputOutput.<String>println("finished");
  }
  
  public static void calculateAndOutputMetrics(final EMFGraph model, final String metaModel, final String fileName) {
    String _name = model.getName();
    String _plus = ("evaluating for " + _name);
    InputOutput.<String>println(_plus);
    model.setMetaModel(metaModel);
    CsvFileWriter.write(model.evaluateAllMetrics(), fileName);
  }
}
