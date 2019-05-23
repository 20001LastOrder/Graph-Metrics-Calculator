package app;

import graph.PartialInterpretationGraph;
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.PartialInterpretation;
import java.io.File;
import java.util.ArrayList;
import metrics.Metric;
import metrics.MultiplexParticipationCoefficientMetric;
import metrics.NodeActivityMetric;
import metrics.OutDegreeMetric;
import output.CsvFileWriter;

@SuppressWarnings("all")
public class PartialInterpretationMetric {
  public static void calculateMetric(final PartialInterpretation partial, final String path, final int id, final String currentStateId) {
    final ArrayList<Metric> metrics = new ArrayList<Metric>();
    OutDegreeMetric _outDegreeMetric = new OutDegreeMetric();
    metrics.add(_outDegreeMetric);
    NodeActivityMetric _nodeActivityMetric = new NodeActivityMetric();
    metrics.add(_nodeActivityMetric);
    MultiplexParticipationCoefficientMetric _multiplexParticipationCoefficientMetric = new MultiplexParticipationCoefficientMetric();
    metrics.add(_multiplexParticipationCoefficientMetric);
    new File(path).mkdir();
    final String filename = (((path + "/state_") + Integer.valueOf(id)) + ".csv");
    final PartialInterpretationGraph metricCalculator = new PartialInterpretationGraph(partial, metrics, currentStateId);
    CsvFileWriter.write(metricCalculator.evaluateAllMetrics(), filename);
  }
}
