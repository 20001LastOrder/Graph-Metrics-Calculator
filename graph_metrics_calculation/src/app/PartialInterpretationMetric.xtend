package app

import graph.PartialInterpretationGraph
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.PartialInterpretation
import java.io.File
import java.util.ArrayList
import metrics.Metric
import metrics.MultiplexParticipationCoefficientMetric
import metrics.NodeActivityMetric
import metrics.OutDegreeMetric
import output.CsvFileWriter

class PartialInterpretationMetric {
	
	def static void calculateMetric(PartialInterpretation partial, String path, int id){
		val metrics = new ArrayList<Metric>();
		metrics.add(new OutDegreeMetric());
		metrics.add(new NodeActivityMetric());
		metrics.add(new MultiplexParticipationCoefficientMetric());
		
		new File(path).mkdir();
		val filename = path + "/state_"+id+".csv";
		val metricCalculator = new PartialInterpretationGraph(partial, metrics, "name");
		
		CsvFileWriter.write(metricCalculator.evaluateAllMetrics(), filename);
	}
}