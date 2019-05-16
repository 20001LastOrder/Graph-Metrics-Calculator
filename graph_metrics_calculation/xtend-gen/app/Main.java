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
    final ArrayList<EMFGraph> models = new ArrayList<EMFGraph>();
    for (int i = 1; (i <= 20); i++) {
      {
        models.addAll(GraphReader.readModels((("viatraInput/VS+i/models/" + "run") + Integer.valueOf(i))));
        models.addAll(GraphReader.readModels((("viatraInput/VS-i/models/" + "run") + Integer.valueOf(i))));
      }
    }
    InputOutput.<String>println("Reading Models Ended");
    for (final EMFGraph model : models) {
      {
        String _name = model.getName();
        String _plus = ("evaluating for " + _name);
        InputOutput.<String>println(_plus);
        model.setMetaModel(YakindummPackageImpl.eNAME);
        ArrayList<ArrayList<String>> _evaluateAllMetrics = model.evaluateAllMetrics();
        String _name_1 = model.getName();
        String _plus_1 = ("viatraOutput/" + _name_1);
        String _plus_2 = (_plus_1 + ".csv");
        CsvFileWriter.write(_evaluateAllMetrics, _plus_2);
      }
    }
    InputOutput.<String>println("finished");
  }
}
