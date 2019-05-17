package app

import graph.EMFGraph
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.impl.YakindummPackageImpl
import input.GraphReader
import java.util.ArrayList
import java.util.List
import output.CsvFileWriter
import socialnetwork.impl.SocialnetworkPackageImpl

class Main {
	def static void main(String[] args){
		GraphReader.init();
		//init model
		SocialnetworkPackageImpl.eINSTANCE.eClass;
		YakindummPackageImpl.eINSTANCE.eClass;
		
		println("Start Reading Models...");
		
		for(var i = 1; i <= 10; i++){
			val models = new ArrayList<EMFGraph>();
//			models.addAll(GraphReader.readModels("viatraInput/VS+i/models/" + "run"+i));
//			models.addAll(GraphReader.readModels("viatraInput/VS-i/models/" + "run"+i));
			models.addAll(GraphReader.readModels("viatraInput/" + "run" + i));
			println("Reading Models Ended")
			for(model : models){
				calculateAndOutputMetrics(model, YakindummPackageImpl.eNAME, "viatraOutput/"+model.name+"_run_"+i+".csv");
			}
		}

		println("finished");
	}
	
	static def calculateAndOutputMetrics(EMFGraph model, String metaModel, String fileName){
		println("evaluating for " + model.name);
		model.metaModel = metaModel;
		CsvFileWriter.write(model.evaluateAllMetrics(), fileName);
	}
}