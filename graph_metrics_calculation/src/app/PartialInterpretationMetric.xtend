package app

import graph.PartialInterpretationGraph
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.PartialInterpretation
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.ArrayList
import java.util.List
import metrics.Metric
import metrics.MultiplexParticipationCoefficientMetric
import metrics.NodeActivityMetric
import metrics.OutDegreeMetric
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.viatra.dse.api.Solution
import output.CsvFileWriter

class PartialInterpretationMetric {
	
	def static void calculateMetric(PartialInterpretation partial, String path, int id, String currentStateId){
		val metrics = new ArrayList<Metric>();
		metrics.add(new OutDegreeMetric());
		metrics.add(new NodeActivityMetric());
		metrics.add(new MultiplexParticipationCoefficientMetric());
		
		new File(path).mkdir();
		val filename = path + "/state_"+id+".csv";
		val metricCalculator = new PartialInterpretationGraph(partial, metrics, currentStateId);
		
		CsvFileWriter.write(metricCalculator.evaluateAllMetrics(), filename);
	}
	
	def static void outputTrajectories(PartialInterpretation empty, List<Solution>  solutions){
		
		for(solution : solutions){
			val emptySolutionCopy = EcoreUtil.copy(empty)
			val trajectory = solution.shortestTrajectory;
			trajectory.modelWithEditingDomain = emptySolutionCopy
			val stateCodes = newArrayList()
			
			while(trajectory.doNextTransformation){
				println(trajectory.stateCoder.createStateCode)
				stateCodes.add(trajectory.stateCoder.createStateCode.toString)
			}
			
			try{
				val path = "debug/metric/trajectories/trajectory"+trajectory.stateCoder.createStateCode.toString+".csv"
				val PrintWriter writer = new PrintWriter(new File(path))
				val output = new StringBuilder
				for(stateCode : stateCodes){
					output.append(stateCode+'\n')
				}
				writer.write(output.toString())
				writer.close()
			}catch(FileNotFoundException e) {
				e.printStackTrace()
			}
		}			
	}
}