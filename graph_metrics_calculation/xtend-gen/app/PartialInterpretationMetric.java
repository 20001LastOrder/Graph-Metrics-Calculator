package app;

import graph.PartialInterpretationGraph;
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.PartialInterpretation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import metrics.Metric;
import metrics.MultiplexParticipationCoefficientMetric;
import metrics.NodeActivityMetric;
import metrics.OutDegreeMetric;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.viatra.dse.api.Solution;
import org.eclipse.viatra.dse.api.SolutionTrajectory;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
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
  
  public static void outputTrajectories(final PartialInterpretation empty, final List<Solution> solutions) {
    for (final Solution solution : solutions) {
      {
        final PartialInterpretation emptySolutionCopy = EcoreUtil.<PartialInterpretation>copy(empty);
        final SolutionTrajectory trajectory = solution.getShortestTrajectory();
        trajectory.setModelWithEditingDomain(emptySolutionCopy);
        final ArrayList<String> stateCodes = CollectionLiterals.<String>newArrayList();
        while (trajectory.doNextTransformation()) {
          {
            InputOutput.<Object>println(trajectory.getStateCoder().createStateCode());
            stateCodes.add(trajectory.getStateCoder().createStateCode().toString());
          }
        }
        try {
          String _string = trajectory.getStateCoder().createStateCode().toString();
          String _plus = ("debug/metric/trajectories/trajectory" + _string);
          final String path = (_plus + ".csv");
          File _file = new File(path);
          final PrintWriter writer = new PrintWriter(_file);
          final StringBuilder output = new StringBuilder();
          for (final String stateCode : stateCodes) {
            output.append((stateCode + "\n"));
          }
          writer.write(output.toString());
          writer.close();
        } catch (final Throwable _t) {
          if (_t instanceof FileNotFoundException) {
            final FileNotFoundException e = (FileNotFoundException)_t;
            e.printStackTrace();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    }
  }
}
